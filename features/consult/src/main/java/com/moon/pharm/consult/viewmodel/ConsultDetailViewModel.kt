package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.common.UiMessage
import com.moon.pharm.consult.model.ConsultUiMessage
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsultDetailViewModel @Inject constructor(
    private val consultUseCases: ConsultUseCases,
    private val userRepository: UserRepository,
    private val consultRepository: ConsultRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsultDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _answerContent = MutableStateFlow("")
    val answerContent = _answerContent.asStateFlow()

    fun getConsultDetail(id: String) {
        viewModelScope.launch {
            consultUseCases.getConsultDetail(id).collectLatest { result ->
                _uiState.update { state ->
                    when (result) {
                        is DataResourceResult.Loading -> state.copy(isLoading = true)
                        is DataResourceResult.Success -> state.copy(
                            isLoading = false,
                            selectedItem = result.resultData.consult,
                            answerPharmacist = result.resultData.pharmacist,
                            canAnswer = result.resultData.isMyConsultToAnswer,
                            currentUserId = result.resultData.currentUserId
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

    fun onAnswerContentChanged(content: String) {
        _answerContent.value = content
    }

    fun startEditingAnswer() {
        val currentAnswer = _uiState.value.selectedItem?.answer?.content ?: ""
        _answerContent.value = currentAnswer
        _uiState.update { it.copy(isEditingAnswer = true) }
    }

    fun registerAnswer(consultId: String) {
        val content = _answerContent.value
        val questionerId = _uiState.value.selectedItem?.userId
        val pharmacist = _uiState.value.answerPharmacist

        if (content.isBlank() || questionerId == null) return

        if (pharmacist == null) {
            _uiState.update { it.copy(userMessage = UiMessage.LoadDataFailed) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            consultUseCases.registerAnswer(consultId, content, pharmacist).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> { _uiState.update { it.copy(isLoading = true) } }
                    is DataResourceResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                selectedItem = result.resultData,
                                canAnswer = false,
                                isEditingAnswer = false,
                                userMessage = ConsultUiMessage.AnswerRegisterSuccess
                            )
                        }
                        _answerContent.value = ""
                        sendNotificationToUser(questionerId, consultId)
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false, userMessage = ConsultUiMessage.CreateFailed) }
                    }
                }
            }
        }
    }


    fun deleteConsult(consultId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            consultRepository.deleteConsult(consultId).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, userMessage = ConsultUiMessage.ConsultDeleteSuccess) }
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false, userMessage = ConsultUiMessage.CreateFailed) }
                    }
                    is DataResourceResult.Loading -> { /* 로딩 처리 */ }
                }
            }
        }
    }

    fun deleteAnswer(consultId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            consultRepository.deleteConsultAnswer(consultId).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Success -> {
                        _uiState.update { it.copy(isLoading = false, canAnswer = true, userMessage = ConsultUiMessage.ConsultDeleteSuccess) }
                        getConsultDetail(consultId)
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    is DataResourceResult.Loading -> { }
                }
            }
        }
    }

    private fun sendNotificationToUser(userId: String, consultId: String) {
        viewModelScope.launch {
            val userResult = userRepository.getUserOnce(userId)

            if (userResult is DataResourceResult.Success) {
                val token = userResult.resultData.fcmToken
                if (!token.isNullOrEmpty()) {
                    consultRepository.sendAnswerNotification(token, consultId)
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { it.copy(userMessage = null) }
    }

}