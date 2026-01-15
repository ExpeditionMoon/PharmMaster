package com.moon.pharm.consult.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.R
import com.moon.pharm.consult.common.ConsultUiMessage
import com.moon.pharm.consult.screen.ConsultUiState
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.consult.screen.ConsultWriteState
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsultViewModel @Inject constructor(
    private val consultUseCases: ConsultUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConsultUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        fetchConsultList()
    }

    /* 연결 확인 */
    fun testFirestore() {
        viewModelScope.launch {
            val currentUserId = consultUseCases.getCurrentUserId()

            if (currentUserId == null) {
                Log.e("FIRESTORE_TEST", "로그인 상태가 아닙니다. 테스트를 중단합니다.")
                return@launch
            }

            consultUseCases.createConsult(
                ConsultItem(
                    id = "",
                    userId = currentUserId,
                    pharmacistId = null,
                    title = "오메가3 고르는 법",
                    content = "rTG 오메가3가 일반 제품보다 흡수율이 훨씬 높은가요?",
                    status = ConsultStatus.WAITING,
                    createdAt = System.currentTimeMillis(),
                    images = emptyList(),
                    answer = null
                ),
            ).collect { result ->
                Log.d("FIRESTORE_TEST", "결과: $result")
            }
        }
    }


    private fun fetchConsultList() {
        viewModelScope.launch {
            val currentUserId = consultUseCases.getCurrentUserId()

            consultUseCases.getConsultList().collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> {
                            val filteredList = result.resultData.filter { item ->
                                item.isPublic || (currentUserId != null && item.userId == currentUserId)
                            }

                            state.copy(
                                isLoading = false,
                                consultList = filteredList
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

    fun getConsultDetail(id: String) {
        viewModelScope.launch {
            consultUseCases.getConsultDetail(id).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is DataResourceResult.Success -> {
                        val item = result.resultData

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                selectedItem = item,
                                answerPharmacist = null
                            )
                        }

                        item.answer?.let { answer ->
                            consultUseCases.getPharmacistDetail(answer.pharmacistId).collect { pharmacistResult ->
                                if (pharmacistResult is DataResourceResult.Success) {
                                    val pharmacist = pharmacistResult.resultData
                                    _uiState.update { state ->
                                        state.copy(answerPharmacist = pharmacist)
                                    }
                                }
                            }
                        }
                    }

                    is DataResourceResult.Failure -> {
                        _uiState.update {
                            it.copy(isLoading = false, userMessage = UiMessage.LoadDataFailed)
                        }
                    }
                }
            }
        }
    }

    fun onTabSelected(tab: ConsultPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(writeState = it.writeState.copy(searchQuery = query)) }
        searchPharmacies(query)
    }

    fun searchPharmacies(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            delay(500L)

            consultUseCases.searchPharmacy(query).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            writeState = state.writeState.copy(searchResults = result.resultData)
                        )
                        is DataResourceResult.Failure -> state.copy(
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

    fun selectPharmacy(pharmacy: Pharmacy) {
        _uiState.update { it.copy(writeState = it.writeState.copy(selectedPharmacy = pharmacy)) }
        fetchPharmacistsInPharmacy(pharmacy)
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

    fun selectPharmacist(pharmacistId: String) {
        _uiState.update { it.copy(writeState = it.writeState.copy(selectedPharmacistId = pharmacistId)) }
    }

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

    fun validateConsultInput(): Boolean {
        val writeData = _uiState.value.writeState

        if (writeData.title.isBlank() || writeData.content.isBlank()) {
            _uiState.update {
                it.copy(userMessage = ConsultUiMessage.StringResourceError(R.string.consult_error_input_required))
            }
            return false
        }
        return true
    }

    fun submitConsult() {
        val writeData = _uiState.value.writeState
        if (writeData.selectedPharmacistId == null) {
            _uiState.update {
                it.copy(userMessage = ConsultUiMessage.StringResourceError(R.string.consult_error_pharmacist_required))
            }
            return
        }

        val currentUserId = consultUseCases.getCurrentUserId()
        if (currentUserId == null) {
            _uiState.update {
                it.copy(userMessage = ConsultUiMessage.StringResourceError(R.string.consult_error_login_required))
            }
            return
        }

        val newItem = ConsultItem(
            id = "",
            userId = currentUserId,
            pharmacistId = writeData.selectedPharmacistId,
            title = writeData.title,
            content = writeData.content,
            status = ConsultStatus.WAITING,
            isPublic = writeData.isPublic,
            createdAt = System.currentTimeMillis(),
            images = writeData.images.map { path ->
                ConsultImage(path.substringAfterLast("/"), path)
            }
        )

        createConsult(newItem)
    }
    private fun createConsult(consultInfo: ConsultItem) {
        viewModelScope.launch {
            consultUseCases.createConsult(consultInfo).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true, userMessage = null)
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

    fun clearWriteState() {
        _uiState.update { it.copy(writeState = ConsultWriteState()) }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    fun resetConsultState() {
        _uiState.update { it.copy(isConsultCreated = false) }
    }
}