package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.mapper.ConsultUiMapper
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import com.moon.pharm.domain.usecase.consult.UploadConsultImagesUseCase
import com.moon.pharm.domain.usecase.consult.ValidateConsultFormUseCase
import com.moon.pharm.domain.usecase.pharmacy.GetNearbyPharmaciesCurrentLocationUseCase
import com.moon.pharm.domain.usecase.user.GetNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
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
    private val getNicknameUseCase: GetNicknameUseCase,
    private val uploadImagesUseCase: UploadConsultImagesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow(ConsultWriteUiState())
    val uiState = _uiState.asStateFlow()

    private val _moveCameraEvent = MutableSharedFlow<LatLng>()
    val moveCameraEvent = _moveCameraEvent.asSharedFlow()

    private val _writeEvent = MutableSharedFlow<WriteEvent>()
    val writeEvent = _writeEvent.asSharedFlow()

    sealed interface WriteEvent {
        data object MoveToPharmacist : WriteEvent
    }

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500L)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest { query -> searchPharmacies(query) }
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
                        _moveCameraEvent.emit(LatLng(location.lat, location.lng))
                        _uiState.update { it.copy(isLoading = false, searchResults = result.resultData.pharmacies) }
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false, userMessage = UiMessage.LoadDataFailed) }
                        fetchNearbyPharmacies(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL)
                    }
                }
            }
        }
    }

    private fun searchPharmacies(query: String) {
        viewModelScope.launch {
            consultUseCases.searchPharmacy(query).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> {
                        val pharmacies = result.resultData
                        _uiState.update { it.copy(isLoading = false, searchResults = pharmacies) }
                        if (pharmacies.isNotEmpty()) {
                            val first = pharmacies.first()
                            _moveCameraEvent.emit(LatLng(first.latitude, first.longitude))
                        }
                    }
                    is DataResourceResult.Failure -> _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun fetchNearbyPharmacies(lat: Double, lng: Double) {
        viewModelScope.launch {
            consultUseCases.searchNearbyPharmacies(lat, lng).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(isLoading = false, searchResults = result.resultData)
                        is DataResourceResult.Failure -> state.copy(isLoading = false)
                    }
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
            _uiState.update { it.copy(userMessage = error) }
            return
        }

        // 성공 시: 이동 이벤트 발생
        viewModelScope.launch {
            _writeEvent.emit(WriteEvent.MoveToPharmacist)
        }
    }

    fun selectPharmacist(pharmacistId: String) {
        _uiState.update { it.copy(selectedPharmacistId = pharmacistId) }
    }

    fun submitConsult() {
        val state = _uiState.value
        val userId = validateAndGetUserId(state) ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val nickname = getNicknameUseCase.getNickname(userId)
                val uploadedUrls = if (state.images.isNotEmpty()) {
                    uploadImagesUseCase(state.images, userId)
                } else emptyList()

                val newItem = ConsultUiMapper.toDomainModel(
                    writeState = state,
                    currentUserId = userId,
                    currentUserNickname = nickname,
                    selectedPharmacistId = state.selectedPharmacistId!!,
                    uploadedImageUrls = uploadedUrls
                )
                createConsult(newItem)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false, userMessage = ConsultUiMessage.CreateFailed) }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }
    // endregion

    // region Private Helpers
    private fun fetchPharmacistsInPharmacy(pharmacy: Pharmacy) {
        viewModelScope.launch {
            consultUseCases.getPharmacists(pharmacy.placeId).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(isLoading = false, availablePharmacists = result.resultData)
                        is DataResourceResult.Failure -> state.copy(isLoading = false, userMessage = UiMessage.LoadDataFailed)
                    }
                }
            }
        }
    }

    private fun clearForm() {
        _uiState.update { ConsultWriteUiState() }
    }

    fun resetConsultState() {
        _uiState.update { it.copy(isConsultCreated = false) }
    }

    private fun createConsult(consultInfo: ConsultItem) {
        viewModelScope.launch {
            consultUseCases.createConsult(consultInfo).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(isLoading = false, isConsultCreated = true)
                        is DataResourceResult.Failure -> state.copy(isLoading = false, userMessage = ConsultUiMessage.CreateFailed)
                    }
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
            _uiState.update { it.copy(userMessage = error) }
            return null
        }
        if (state.selectedPharmacistId == null) {
            _uiState.update { it.copy(userMessage = ConsultUiMessage.PharmacistRequired) }
            return null
        }
        val userId = consultUseCases.getCurrentUserId()
        if (userId == null) {
            _uiState.update { it.copy(userMessage = ConsultUiMessage.LoginRequired) }
            return null
        }
        return userId
    }
    // endregion
}