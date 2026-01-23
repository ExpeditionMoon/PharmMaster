package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.consult.mapper.ConsultUiMapper
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.consult.screen.ConsultUiState
import com.moon.pharm.consult.screen.ConsultWriteState
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import com.moon.pharm.domain.usecase.consult.ValidateConsultFormUseCase
import com.moon.pharm.domain.usecase.pharmacy.GetNearbyPharmaciesCurrentLocationUseCase
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
class ConsultViewModel @Inject constructor(
    private val consultUseCases: ConsultUseCases,
    private val getLocationUseCase: GetNearbyPharmaciesCurrentLocationUseCase
) : ViewModel() {

    // region 1. State & Events
    private val _searchQuery = MutableStateFlow("")

    private val _uiState = MutableStateFlow(ConsultUiState())
    val uiState = _uiState.asStateFlow()

    private val _moveCameraEvent = MutableSharedFlow<LatLng>()
    val moveCameraEvent = _moveCameraEvent.asSharedFlow()
    // endregion

    // region 2. Init
    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500L)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .collectLatest { query ->
                    searchPharmacies(query)
                }
        }
        fetchConsultList()
    }
    // endregion

    // region 3. User Actions (Consult List & Detail)
    fun onTabSelected(tab: ConsultPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun getConsultDetail(id: String) {
        viewModelScope.launch {
            consultUseCases.getConsultDetail(id).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> {
                            state.copy(
                                isLoading = false,
                                selectedItem = result.resultData.consult,
                                answerPharmacist = result.resultData.pharmacist
                            )
                        }

                        is DataResourceResult.Failure -> state.copy(
                            isLoading = false,
                            userMessage = UiMessage.LoadDataFailed
                        )
                    }
                }
            }
        }
    }
    // endregion

    // region 4. User Actions (Search & Map)
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query

        _uiState.update { state ->
            state.copy(
                writeState = state.writeState.copy(
                    searchQuery = query,
                    selectedPharmacy = null,
                    selectedPharmacistId = null,
                    availablePharmacists = emptyList()
                )
            )
        }
    }

    fun fetchCurrentLocationAndPharmacies() {
        viewModelScope.launch {
            getLocationUseCase().collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is DataResourceResult.Success -> {
                        val geoLocation = result.resultData.location
                        val pharmacies = result.resultData.pharmacies

                        _moveCameraEvent.emit(LatLng(geoLocation.lat, geoLocation.lng))

                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                writeState = state.writeState.copy(searchResults = pharmacies)
                            )
                        }
                    }

                    is DataResourceResult.Failure -> {
                        result.exception.printStackTrace()
                        _uiState.update {
                            it.copy(isLoading = false, userMessage = UiMessage.LoadDataFailed)
                        }
                        fetchNearbyPharmacies(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL)
                    }
                }
            }
        }
    }

    fun searchPharmacies(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            consultUseCases.searchPharmacy(query).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is DataResourceResult.Success -> {
                        val pharmacies = result.resultData
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                writeState = state.writeState.copy(searchResults = pharmacies)
                            )
                        }
                        if (pharmacies.isNotEmpty()) {
                            val firstPharmacy = pharmacies.first()
                            _moveCameraEvent.emit(
                                LatLng(
                                    firstPharmacy.latitude,
                                    firstPharmacy.longitude
                                )
                            )
                        }
                    }

                    is DataResourceResult.Failure -> {
                        _uiState.update { state -> state.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    fun fetchNearbyPharmacies(currentLat: Double, currentLng: Double) {
        viewModelScope.launch {
            consultUseCases.searchNearbyPharmacies(currentLat, currentLng).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            writeState = state.writeState.copy(searchResults = result.resultData)
                        )

                        is DataResourceResult.Failure -> state.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun selectPharmacy(pharmacy: Pharmacy) {
        _uiState.update { it.copy(writeState = it.writeState.copy(selectedPharmacy = pharmacy)) }
        fetchPharmacistsInPharmacy(pharmacy)
    }

    fun clearSelectedPharmacy() {
        _uiState.update { it.copy(writeState = it.writeState.copy(selectedPharmacy = null)) }
    }
    // endregion

    // region 5. User Actions (Write Form)
    fun onTitleChanged(newTitle: String) {
        _uiState.update { it.copy(writeState = it.writeState.copy(title = newTitle)) }
    }

    fun onContentChanged(newContent: String) {
        _uiState.update { it.copy(writeState = it.writeState.copy(content = newContent)) }
    }

    fun updateImages(images: List<String>) {
        _uiState.update { it.copy(writeState = it.writeState.copy(images = images)) }
    }

    fun onVisibilityChanged(isPublic: Boolean) {
        _uiState.update { it.copy(writeState = it.writeState.copy(isPublic = isPublic)) }
    }

    fun selectPharmacist(pharmacistId: String) {
        _uiState.update { it.copy(writeState = it.writeState.copy(selectedPharmacistId = pharmacistId)) }
    }

    fun clearWriteState() {
        _uiState.update { it.copy(writeState = ConsultWriteState()) }
    }

    fun submitConsult() {
        val writeData = _uiState.value.writeState

        val validationResult = consultUseCases.validateConsultForm(
            title = writeData.title,
            content = writeData.content
        )

        if (validationResult is ValidateConsultFormUseCase.Result.Invalid) {
            val errorState = when (validationResult.error) {
                ValidateConsultFormUseCase.ErrorType.EMPTY_INPUT -> ConsultUiMessage.InputRequired
                ValidateConsultFormUseCase.ErrorType.TITLE_TOO_SHORT -> ConsultUiMessage.TitleTooShort
            }
            _uiState.update { it.copy(userMessage = errorState) }
            return
        }

        if (writeData.selectedPharmacistId == null) {
            _uiState.update {
                it.copy(userMessage = ConsultUiMessage.PharmacistRequired)
            }
            return
        }

        val currentUserId = consultUseCases.getCurrentUserId()
        if (currentUserId == null) {
            _uiState.update {
                it.copy(userMessage = ConsultUiMessage.LoginRequired)
            }
            return
        }

        val newItem = ConsultUiMapper.toDomainModel(
            writeState = writeData,
            currentUserId = currentUserId,
            selectedPharmacistId = writeData.selectedPharmacistId
        )
        createConsult(newItem)
    }
    // endregion

    // region 6. System & Reset Actions
    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun resetConsultState() {
        _uiState.update { it.copy(isConsultCreated = false) }
    }
    // endregion

    // region 7. Private Implementation (Internal Logic)
    private fun fetchConsultList() {
        viewModelScope.launch {
            consultUseCases.getConsultList().collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            consultList = result.resultData
                        )

                        is DataResourceResult.Failure -> state.copy(
                            isLoading = false,
                            userMessage = UiMessage.LoadDataFailed
                        )
                    }
                }
            }
        }
    }

    private fun fetchPharmacistsInPharmacy(pharmacy: Pharmacy) {
        viewModelScope.launch {
            consultUseCases.getPharmacists(pharmacy.placeId).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            writeState = state.writeState.copy(availablePharmacists = result.resultData)
                        )

                        is DataResourceResult.Failure -> state.copy(
                            isLoading = false,
                            userMessage = UiMessage.LoadDataFailed
                        )
                    }
                }
            }
        }
    }

    private fun createConsult(consultInfo: ConsultItem) {
        viewModelScope.launch {
            consultUseCases.createConsult(consultInfo).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(
                            isLoading = true,
                            userMessage = null
                        )

                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            isConsultCreated = true
                        )

                        is DataResourceResult.Failure -> state.copy(
                            isLoading = false,
                            userMessage = ConsultUiMessage.CreateFailed
                        )
                    }
                }
            }
        }
    }
    // endregion
}