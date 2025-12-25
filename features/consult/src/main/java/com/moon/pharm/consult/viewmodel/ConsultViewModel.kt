package com.moon.pharm.consult.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.consult.screen.ConsultUiState
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.consult.screen.ConsultWriteState
import com.moon.pharm.domain.model.ConsultAnswer
import com.moon.pharm.domain.model.ConsultImage
import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.ConsultStatus
import com.moon.pharm.domain.model.Pharmacist
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConsultViewModel(
    private val consultUseCases: ConsultUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConsultUiState())
    private val _pharmacists = MutableStateFlow<List<Pharmacist>>(emptyList())
    val uiState = _uiState.asStateFlow()
    val pharmacists: StateFlow<List<Pharmacist>> = _pharmacists.asStateFlow()

    init {
//        fetchConsultList()
    }

    /* 연결 확인 */
/*    fun testFirestore() {
        viewModelScope.launch {
            consultUseCases.createConsultUseCase(
                ConsultItem(
                    id = "",
                    userId = "user_12", expertId = "expert_1",
                    title = "감기약 복용 후 졸음 증상",
                    content = "졸리지 않은 감기약은 효과가 떨어지나요?",
                    status = ConsultStatus.COMPLETED,
                    createdAt = System.currentTimeMillis().toString(),
                    images = emptyList(),
                    answer = ConsultAnswer(
                        "",
                        "expert_1",
                        "효과가 떨어지는 것이 아니라 부작용만 뺀 것입니다.",
                        System.currentTimeMillis().toString()
                    )
                )
            ).collect { result ->
                Log.d("PHARM_CHECK", "결과: $result")
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
            userId = "current_user_id",
            expertId = writeData.expertId,
            title = writeData.title,
            content = writeData.content,
            status = ConsultStatus.WAITING,
            createdAt = "",
            images = writeData.images.map { ConsultImage(it) }
        )
        createConsult(newItem)
    }

    fun createConsult(consultInfo: ConsultItem) {
        viewModelScope.launch {
            consultUseCases.createConsultUseCase(consultInfo).collect { result ->
                _uiState.update { currentState ->
                    when (result) {
                        is DataResourceResult.Loading -> {
                            currentState.copy(isLoading = true, userMessage = null)
                        }

                        is DataResourceResult.Success -> {
                            currentState.copy(
                                isLoading = false,
                                isConsultCreated = true
                            )
                        }

                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = result.exception.message ?: "상담 등록 실패"
                            )
                        }

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
                                isLoading = false,
                                consultList = result.resultData
                            )
                        }

                        is DataResourceResult.Failure -> {
                            currentState.copy(
                                isLoading = false,
                                userMessage = result.exception.message ?: "데이터를 불러오지 못했습니다."
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
                        selectedItem = item,
                        pharmacist = pharmacistInfo
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