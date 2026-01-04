package com.moon.pharm.consult.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.common.ConsultUiMessage
import com.moon.pharm.consult.screen.ConsultUiState
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.consult.screen.ConsultWriteState
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.model.Pharmacist
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val _pharmacists = MutableStateFlow<List<Pharmacist>>(emptyList())
    val uiState = _uiState.asStateFlow()
    val pharmacists: StateFlow<List<Pharmacist>> = _pharmacists.asStateFlow()

    init {
        fetchConsultList()
    }

    /* 연결 확인 */
/*    fun testFirestore() {
        viewModelScope.launch {
            consultUseCases.createConsultUseCase(
                ConsultItem(
                    id = "",
                    userId = "user_9", expertId = null,
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
    }*/

    fun onTitleChanged(newTitle: String) {
        _uiState.update {
            it.copy(
                writeState = it.writeState.copy(title = newTitle)
            )
        }
    }

    fun onContentChanged(newContent: String) {
        _uiState.update {
            it.copy(
                writeState = it.writeState.copy(content = newContent)
            )
        }
    }

    fun updateExpert(pharmacistId: String) {
        _uiState.update {
            it.copy(
                writeState = it.writeState.copy(
                    expertId = pharmacistId
                )
            )
        }
    }

    fun updateImages(images: List<String>) {
        _uiState.update {
            it.copy(
                writeState = it.writeState.copy(images = images)
            )
        }
    }

    fun clearWriteState() {
        _uiState.update { it.copy(writeState = ConsultWriteState()) }
    }

    fun submitConsult() {
        val writeData = _uiState.value.writeState
        val newItem = ConsultItem(
            id = "",
            userId = "",
            expertId = writeData.expertId,
            title = writeData.title,
            content = writeData.content,
            status = ConsultStatus.WAITING,
            createdAt = System.currentTimeMillis(),
            images = writeData.images.map { ConsultImage(it) })
        saveConsult(newItem)
    }

    fun saveConsult(consultInfo: ConsultItem) {
        viewModelScope.launch {
            consultUseCases.createConsultUseCase(consultInfo).collectLatest { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> currentState.copy(isLoading = true, userMessage = null)
                        is DataResourceResult.Success -> currentState.copy(
                                isLoading = false, isConsultCreated = true
                            )
                        is DataResourceResult.Failure -> currentState.copy(
                                isLoading = false,
                                userMessage = ConsultUiMessage.CreateFailed
                            )
                        else -> currentState
                    }
                }
            }
        }
    }

    private fun fetchConsultList() {
        viewModelScope.launch {
            consultUseCases.getConsultItemsUseCase().collect { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> {
                            currentState.copy(isLoading = true, userMessage = null)
                        }

                        is DataResourceResult.Success -> {
                            currentState.copy(
                                isLoading = false, consultList = result.resultData
                            )
                        }

                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = result.exception.message
                                    ?.let { UiMessage.Error(it) }
                                    ?: UiMessage.LoadDataFailed
                            )
                        }

                        else -> currentState
                    }
                }
            }
        }
    }

    fun onTabSelected(tab: ConsultPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    fun getConsultDetail(id: String) {
        viewModelScope.launch {
            consultUseCases.getConsultDetailUseCase(id).collect { item ->
                val pharmacistInfo = item.answer?.let { answer ->
                    consultUseCases.getPharmacistUseCase.getById(answer.expertId)
                }
                _uiState.update {
                    it.copy(
                        selectedItem = item, pharmacist = pharmacistInfo
                    )
                }
            }
        }
    }

    fun selectPharmacy(pharmacyId: String) {
        viewModelScope.launch {
            val result = consultUseCases.getPharmacistUseCase.getByPharmacy(pharmacyId)
            _pharmacists.value = result
        }
    }

    // Toast 메시지 보여준 후 상태 초기화 (중복 표시 방지)
    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

    // 성공 이벤트 처리 후 상태 초기화 (화면 닫힘 후 재진입 시 문제 방지)
    fun resetDonationState() {
        _uiState.update { it.copy(isConsultCreated = false) }
    }
}