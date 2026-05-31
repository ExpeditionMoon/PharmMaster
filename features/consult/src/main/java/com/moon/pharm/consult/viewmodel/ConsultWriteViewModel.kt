package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.mapper.ConsultUiMapper
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import com.moon.pharm.domain.usecase.consult.UploadConsultImagesUseCase
import com.moon.pharm.domain.usecase.consult.ValidateConsultFormUseCase
import com.moon.pharm.domain.usecase.pharmacy.GetNearbyPharmaciesCurrentLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ConsultWriteViewModel @Inject constructor(
    private val consultUseCases: ConsultUseCases,
    private val getLocationUseCase: GetNearbyPharmaciesCurrentLocationUseCase,
    private val uploadImagesUseCase: UploadConsultImagesUseCase,
    private val consultRepository: ConsultRepository,
    private val pharmacyRepository: PharmacyRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var editingConsultId: String? = null
    private var pharmacistSearchJob: Job? = null
    private val _searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow(ConsultWriteUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<ConsultWriteEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500L)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest(::searchPharmacies)
        }
    }

    fun setEditMode(consultId: String) {
        if (editingConsultId == consultId) return
        editingConsultId = consultId

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            consultRepository.getConsultDetail(consultId).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> {
                        val consult = result.resultData
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                isEditMode = true,
                                title = consult.title,
                                content = consult.content,
                                isPublic = consult.isPublic,
                                images = consult.images.map { it.imageUrl }
                            )
                        }
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        showMessage(UiMessage.LoadDataFailed)
                    }
                }
            }
        }
    }

    // region Map & Search Actions
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _uiState.update { state ->
            state.copy(
                searchQuery = query,
                selectedPharmacy = null,
                selectedPharmacistId = null,
                availablePharmacists = emptyList()
            )
        }
    }

    fun fetchCurrentLocationAndPharmacies() {
        viewModelScope.launch {
            getLocationUseCase().collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> {
                        val location = result.resultData.location
                        _effect.emit(ConsultWriteEffect.MoveCamera(location.lat, location.lng))
                        _uiState.update { it.copy(isLoading = false, searchResults = result.resultData.pharmacies) }
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        showMessage(UiMessage.LoadDataFailed)
                        fetchNearbyPharmaciesInternal(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL)
                    }
                }
            }
        }
    }

    private suspend fun searchPharmacies(query: String) {
        consultUseCases.searchPharmacy(query).collectLatest { result ->
            when (result) {
                is DataResourceResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                is DataResourceResult.Success -> {
                    val pharmacies = result.resultData
                    _uiState.update { it.copy(isLoading = false, searchResults = pharmacies) }
                    if (pharmacies.isNotEmpty()) {
                        val first = pharmacies.first()
                        _effect.emit(ConsultWriteEffect.MoveCamera(first.latitude, first.longitude))
                    }
                }
                is DataResourceResult.Failure -> _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun fetchNearbyPharmacies(lat: Double, lng: Double) {
        viewModelScope.launch {
            fetchNearbyPharmaciesInternal(lat, lng)
        }
    }

    private suspend fun fetchNearbyPharmaciesInternal(lat: Double, lng: Double) {
        pharmacyRepository.searchNearbyPharmacies(lat, lng).collectLatest { result ->
            _uiState.update { state ->
                when (result) {
                    is DataResourceResult.Loading -> state.copy(isLoading = true)
                    is DataResourceResult.Success -> state.copy(isLoading = false, searchResults = result.resultData)
                    is DataResourceResult.Failure -> state.copy(isLoading = false)
                }
            }
        }
    }

    fun selectPharmacy(pharmacy: Pharmacy) {
        _uiState.update { it.copy(selectedPharmacy = pharmacy) }
        fetchPharmacistsInPharmacy(pharmacy)
    }

    fun clearSelectedPharmacy() {
        _uiState.update { it.copy(selectedPharmacy = null) }
    }
    // endregion

    // region Write & Submit Actions
    fun onTitleChanged(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onContentChanged(newContent: String) {
        _uiState.update { it.copy(content = newContent) }
    }

    fun updateImages(images: List<String>) {
        _uiState.update { it.copy(images = images) }
    }

    fun onVisibilityChanged(isPublic: Boolean) {
        _uiState.update { it.copy(isPublic = isPublic) }
    }

    fun onNextClick() {
        val state = _uiState.value

        val validationResult = consultUseCases.validateConsultForm(state.title, state.content)

        if (validationResult is ValidateConsultFormUseCase.Result.Invalid) {
            val error = when (validationResult.error) {
                ValidateConsultFormUseCase.ErrorType.EMPTY_INPUT -> ConsultUiMessage.InputRequired
                ValidateConsultFormUseCase.ErrorType.TITLE_TOO_SHORT -> ConsultUiMessage.TitleTooShort
            }
            showMessage(error)
            return
        }

        if (state.isEditMode) {
            submitConsult()
        } else {
            viewModelScope.launch {
                _effect.emit(ConsultWriteEffect.MoveToPharmacist)
            }
        }
    }

    fun selectPharmacist(pharmacistId: String) {
        _uiState.update { it.copy(selectedPharmacistId = pharmacistId) }
    }

    fun submitConsult() {
        val state = _uiState.value
        val editId = editingConsultId

        if (editId != null) {
            viewModelScope.launch {
                updateExistingConsult(editId, state)
            }
            return
        }

        val userId = validateAndGetUserId(state) ?: return

        viewModelScope.launch {
            createNewConsult(state, userId)
        }
    }

    private suspend fun sendNotificationToPharmacist(pharmacistId: String, consultId: String) {
        try {
            val pharmacistResult = userRepository.getUserOnce(pharmacistId)

            if (pharmacistResult is DataResourceResult.Success) {
                val token = pharmacistResult.resultData.fcmToken
                if (!token.isNullOrEmpty()) {
                    consultRepository.sendNewConsultNotification(token, consultId)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // endregion

    // region Private Helpers
    private suspend fun updateExistingConsult(consultId: String, state: ConsultWriteUiState) {
        _uiState.update { it.copy(isLoading = true) }
        consultRepository.updateConsult(
            consultId = consultId,
            title = state.title,
            content = state.content,
            isPublic = state.isPublic
        ).collectLatest { result ->
            when (result) {
                is DataResourceResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                is DataResourceResult.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(ConsultWriteEffect.UpdateSuccess)
                }
                is DataResourceResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(ConsultWriteEffect.ShowMessage(ConsultUiMessage.CreateFailed))
                }
            }
        }
    }

    private suspend fun createNewConsult(state: ConsultWriteUiState, userId: String) {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val userResult = userRepository.getUserOnce(userId)
            val nickname = if (userResult is DataResourceResult.Success) userResult.resultData.nickName else ""
            val uploadedUrls = if (state.images.isNotEmpty()) {
                uploadImagesUseCase(state.images, userId)
            } else {
                emptyList()
            }

            val newItem = ConsultUiMapper.toDomainModel(
                writeState = state,
                currentUserId = userId,
                currentUserNickname = nickname,
                selectedPharmacistId = state.selectedPharmacistId!!,
                uploadedImageUrls = uploadedUrls
            )
            createConsultItem(newItem)
        } catch (e: Exception) {
            e.printStackTrace()
            _uiState.update { it.copy(isLoading = false) }
            _effect.emit(ConsultWriteEffect.ShowMessage(ConsultUiMessage.CreateFailed))
        }
    }

    private fun fetchPharmacistsInPharmacy(pharmacy: Pharmacy) {
        pharmacistSearchJob?.cancel()
        pharmacistSearchJob = viewModelScope.launch {
            consultUseCases.pharmacistRepository.getPharmacistsByPlaceId(pharmacy.placeId).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is DataResourceResult.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, availablePharmacists = result.resultData)
                        }
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _effect.emit(ConsultWriteEffect.ShowMessage(UiMessage.LoadDataFailed))
                    }
                }
            }
        }
    }

    private fun clearForm() {
        _uiState.update { ConsultWriteUiState() }
    }

    private suspend fun createConsultItem(consultInfo: ConsultItem) {
        consultRepository.createConsult(consultInfo).collectLatest { result ->
            when (result) {
                is DataResourceResult.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
                is DataResourceResult.Success -> {
                    val pharmacistId = consultInfo.pharmacistId
                    if (pharmacistId != null) {
                        sendNotificationToPharmacist(pharmacistId, consultInfo.id)
                    }
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(ConsultWriteEffect.CreateSuccess)
                }
                is DataResourceResult.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(ConsultWriteEffect.ShowMessage(ConsultUiMessage.CreateFailed))
                }
            }
        }
    }

    private fun validateAndGetUserId(state: ConsultWriteUiState): String? {
        val validationResult = consultUseCases.validateConsultForm(state.title, state.content)
        if (validationResult is ValidateConsultFormUseCase.Result.Invalid) {
            val error = when (validationResult.error) {
                ValidateConsultFormUseCase.ErrorType.EMPTY_INPUT -> ConsultUiMessage.InputRequired
                ValidateConsultFormUseCase.ErrorType.TITLE_TOO_SHORT -> ConsultUiMessage.TitleTooShort
            }
            showMessage(error)
            return null
        }
        if (state.selectedPharmacistId == null) {
            showMessage(ConsultUiMessage.PharmacistRequired)
            return null
        }
        val userId = consultUseCases.authRepository.getCurrentUserId()
        if (userId == null) {
            showMessage(UiMessage.LoginRequired)
            return null
        }
        return userId
    }

    private fun showMessage(message: UiMessage) {
        viewModelScope.launch {
            _effect.emit(ConsultWriteEffect.ShowMessage(message))
        }
    }
    // endregion
}
