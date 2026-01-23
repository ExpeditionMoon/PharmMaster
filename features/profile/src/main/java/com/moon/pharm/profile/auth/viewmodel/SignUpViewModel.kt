package com.moon.pharm.profile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.moon.pharm.component_ui.common.DEFAULT_LAT_SEOUL
import com.moon.pharm.component_ui.common.DEFAULT_LNG_SEOUL
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.CheckEmailDuplicateUseCase
import com.moon.pharm.domain.usecase.auth.SignUpUseCase
import com.moon.pharm.domain.usecase.auth.ValidateSignUpFormUseCase
import com.moon.pharm.domain.usecase.pharmacy.GetNearbyPharmaciesCurrentLocationUseCase
import com.moon.pharm.domain.usecase.pharmacy.SearchNearbyPharmaciesUseCase
import com.moon.pharm.domain.usecase.pharmacy.SearchPharmacyUseCase
import com.moon.pharm.profile.auth.common.SignUpUiMessage
import com.moon.pharm.profile.auth.mapper.SignUpUiMapper
import com.moon.pharm.profile.auth.model.SignUpStep
import com.moon.pharm.profile.auth.screen.SignUpUiState
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
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase,
    private val searchPharmacyUseCase: SearchPharmacyUseCase,
    private val searchNearbyPharmaciesUseCase: SearchNearbyPharmaciesUseCase,
    private val getNearbyPharmaciesUseCase: GetNearbyPharmaciesCurrentLocationUseCase,
    private val validateSignUpFormUseCase: ValidateSignUpFormUseCase
) : ViewModel() {

    // region 1. State & Events
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    private val _moveCameraEvent = MutableSharedFlow<LatLng>()
    val moveCameraEvent = _moveCameraEvent.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")
    // endregion

    // region 2. Init
    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(300L)
                .filter { it.isNotBlank() }
                .distinctUntilChanged()
                .collectLatest { query -> executeSearch(query) }
        }
    }
    // endregion

    // region 3. User Actions (Input & Step)
    fun updateUserType(type: UserType) { _uiState.update { it.copy(userType = type) } }
    fun updateEmail(email: String) { _uiState.update { it.copy(email = email, isEmailAvailable = null) } }
    fun updatePassword(password: String) { _uiState.update { it.copy(password = password) } }
    fun updateNickName(name: String) { _uiState.update { it.copy(nickName = name) } }
    fun updateProfileImage(uriString: String?) { _uiState.update { it.copy(profileImageUri = uriString) } }
    fun updatePharmacy(pharmacy: Pharmacy) {
        _uiState.update { it.copy(pharmacyName = pharmacy.name, selectedPharmacy = pharmacy) }
    }
    fun updatePharmacistBio(bio: String) { _uiState.update { it.copy(pharmacistBio = bio) } }

    fun checkEmailDuplicate() {
        val email = _uiState.value.email
        val validation = validateSignUpFormUseCase.validateEmailOnly(email)

        if (validation is ValidateSignUpFormUseCase.Result.Invalid) {
            val errorMsg = when (validation.error) {
                ValidateSignUpFormUseCase.ErrorType.EMPTY_EMAIL -> SignUpUiMessage.EmptyEmail
                ValidateSignUpFormUseCase.ErrorType.INVALID_EMAIL_FORMAT -> SignUpUiMessage.InvalidEmailFormat
                else -> null
            }
            _uiState.update { it.copy(userMessage = errorMsg) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isEmailChecking = true) }
            val isDuplicated = checkEmailDuplicateUseCase(email)
            _uiState.update {
                it.copy(
                    isEmailAvailable = !isDuplicated,
                    isEmailChecking = false,
                    userMessage = if (isDuplicated) SignUpUiMessage.EmailDuplicated else SignUpUiMessage.EmailAvailable
                )
            }
        }
    }

    fun moveToNextStep() {
        val currentState = _uiState.value

        when (currentState.currentStep) {
            SignUpStep.TYPE -> {
                if (currentState.userType != null) { _uiState.update { it.copy(currentStep = SignUpStep.EMAIL) } }
            }

            SignUpStep.EMAIL -> {
                val validation = validateSignUpFormUseCase.validateEmailPassword(
                    currentState.email, currentState.password
                )
                if (validation is ValidateSignUpFormUseCase.Result.Invalid) {
                    val errorMsg = when (validation.error) {
                        ValidateSignUpFormUseCase.ErrorType.EMPTY_EMAIL -> SignUpUiMessage.EmptyEmail
                        ValidateSignUpFormUseCase.ErrorType.INVALID_EMAIL_FORMAT -> SignUpUiMessage.InvalidEmailFormat
                        ValidateSignUpFormUseCase.ErrorType.PASSWORD_TOO_SHORT -> SignUpUiMessage.PasswordTooShort
                        ValidateSignUpFormUseCase.ErrorType.EMPTY_PASSWORD -> SignUpUiMessage.PasswordTooShort
                        else -> null
                    }
                    _uiState.update { it.copy(userMessage = errorMsg) }
                    return
                }
                if (currentState.isEmailAvailable != true) {
                    _uiState.update { it.copy(userMessage = SignUpUiMessage.EmailDuplicated) }
                    return
                }
                _uiState.update { it.copy(currentStep = SignUpStep.NICKNAME) }
            }

            SignUpStep.NICKNAME -> {
                val validation = validateSignUpFormUseCase.validateNickname(currentState.nickName)
                if (validation is ValidateSignUpFormUseCase.Result.Invalid) {
                    _uiState.update { it.copy(userMessage = SignUpUiMessage.EmptyNickname) }
                    return
                }
                if (currentState.userType == UserType.PHARMACIST) {
                    _uiState.update { it.copy(currentStep = SignUpStep.PHARMACIST_INFO) }
                } else {
                    signUpUser()
                }
            }

            SignUpStep.PHARMACIST_INFO -> {
                val validation = validateSignUpFormUseCase.validatePharmacistInfo(
                    pharmacySelected = currentState.selectedPharmacy != null,
                    bio = currentState.pharmacistBio
                )
                if (validation is ValidateSignUpFormUseCase.Result.Invalid) {
                    val errorMsg = when(validation.error) {
                        ValidateSignUpFormUseCase.ErrorType.EMPTY_PHARMACY -> SignUpUiMessage.EmptyPharmacy
                        ValidateSignUpFormUseCase.ErrorType.EMPTY_BIO -> SignUpUiMessage.EmptyBio
                        else -> null
                    }
                    _uiState.update { it.copy(userMessage = errorMsg) }
                    return
                }
                signUpUser()
            }
        }
    }

    fun onPrevStep() {
        _uiState.update { state ->
            val prevStep = when(state.currentStep) {
                SignUpStep.EMAIL -> SignUpStep.TYPE
                SignUpStep.NICKNAME -> SignUpStep.EMAIL
                SignUpStep.PHARMACIST_INFO -> SignUpStep.NICKNAME
                else -> state.currentStep
            }
            state.copy(currentStep = prevStep)
        }
    }
    // endregion

    // region 4. Map & Search Actions
    fun fetchNearbyPharmacies(lat: Double, lng: Double) {
        viewModelScope.launch {
            searchNearbyPharmaciesUseCase(lat, lng).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> {
                            state.copy(
                                isLoading = false,
                                pharmacySearchResults = result.resultData
                            )
                        }
                        is DataResourceResult.Failure -> state.copy(isLoading = false)
                    }
                }
            }
        }
    }

    fun searchPharmacies(query: String) { _searchQuery.value = query }

    fun fetchCurrentLocationAndSearch() {
        viewModelScope.launch {
            getNearbyPharmaciesUseCase().collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is DataResourceResult.Success -> {
                        val (location, pharmacies) = result.resultData
                        _moveCameraEvent.emit(LatLng(location.lat, location.lng))
                        _uiState.update {
                            it.copy(isLoading = false, pharmacySearchResults = pharmacies)
                        }
                    }
                    is DataResourceResult.Failure -> {
                        result.exception.printStackTrace()
                        _uiState.update { it.copy(userMessage = UiMessage.LoadDataFailed) }
                        fetchNearbyPharmacies(DEFAULT_LAT_SEOUL, DEFAULT_LNG_SEOUL)
                    }
                }
            }
        }
    }

    private fun executeSearch(query: String) {
        viewModelScope.launch {
            searchPharmacyUseCase(query).collectLatest { result ->
                when(result) {
                    is DataResourceResult.Loading -> {}
                    is DataResourceResult.Success -> {
                        _uiState.update { it.copy(pharmacySearchResults = result.resultData) }
                        if (result.resultData.isNotEmpty()) {
                            val first = result.resultData.first()
                            _moveCameraEvent.emit(LatLng(first.latitude, first.longitude))
                        }
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(pharmacySearchResults = emptyList()) }
                    }
                }
            }
        }
    }

    fun clearSearchResults() { _uiState.update { it.copy(pharmacySearchResults = emptyList()) } }
    // endregion

    // region 5. System Actions
    fun userMessageShown() { _uiState.update { it.copy(userMessage = null) } }
    // endregion

    // region 6. Private Implementation
    private fun signUpUser() {
        val currentState = _uiState.value
        val user = SignUpUiMapper.toUser(currentState)
        val pharmacist = SignUpUiMapper.toPharmacist(currentState)

        if (currentState.userType == UserType.PHARMACIST && pharmacist == null) return
        viewModelScope.launch {
            signUpUseCase(user, currentState.password, pharmacist, currentState.selectedPharmacy)
                .collectLatest { result ->
                    when (result) {
                        is DataResourceResult.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is DataResourceResult.Success -> {
                            _uiState.update { it.copy(isLoading = false, isComplete = true) }
                        }
                        is DataResourceResult.Failure -> {
                            _uiState.update {
                                it.copy(isLoading = false, userMessage = SignUpUiMessage.SignUpFailed)
                            }
                        }
                    }
                }
        }
    }
    // endregion
}