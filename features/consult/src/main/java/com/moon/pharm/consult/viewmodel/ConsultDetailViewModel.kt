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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _effect = MutableSharedFlow<ConsultDetailEffect>()
    val effect = _effect.asSharedFlow()

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
                        is DataResourceResult.Failure -> state.copy(isLoading = false)
                    }
                }

                if (result is DataResourceResult.Failure) {
                    showMessage(UiMessage.LoadDataFailed)
                }
            }
        }
    }

    fun onAnswerContentChanged(content: String) {
        _uiState.update { it.copy(answerContent = content) }
    }

    fun startEditingAnswer() {
        val currentAnswer = _uiState.value.selectedItem?.answer?.content ?: ""
        _uiState.update {
            it.copy(
                isEditingAnswer = true,
                answerContent = currentAnswer
            )
        }
    }

    fun registerAnswer(consultId: String) {
        val content = _uiState.value.answerContent
        val questionerId = _uiState.value.selectedItem?.userId
        val pharmacist = _uiState.value.answerPharmacist

        if (content.isBlank() || questionerId == null) return

        if (pharmacist == null) {
            showMessage(UiMessage.LoadDataFailed)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            consultUseCases.registerAnswer(consultId, content, pharmacist).collectLatest { result ->
                when (result) {
                    is DataResourceResult.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is DataResourceResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                selectedItem = result.resultData,
                                canAnswer = false,
                                isEditingAnswer = false,
                                answerContent = ""
                            )
                        }
                        _effect.emit(ConsultDetailEffect.ShowMessage(ConsultUiMessage.AnswerRegisterSuccess))
                        sendNotificationToUser(questionerId, consultId)
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _effect.emit(ConsultDetailEffect.ShowMessage(ConsultUiMessage.CreateFailed))
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
                        _uiState.update { it.copy(isLoading = false) }
                        _effect.emit(ConsultDetailEffect.ShowMessage(ConsultUiMessage.ConsultDeleteSuccess))
                        _effect.emit(ConsultDetailEffect.NavigateBack)
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _effect.emit(ConsultDetailEffect.ShowMessage(ConsultUiMessage.CreateFailed))
                    }
                    is DataResourceResult.Loading -> Unit
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
                        _uiState.update { it.copy(isLoading = false, canAnswer = true) }
                        _effect.emit(ConsultDetailEffect.ShowMessage(ConsultUiMessage.AnswerDeleteSuccess))
                        getConsultDetail(consultId)
                    }
                    is DataResourceResult.Failure -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _effect.emit(ConsultDetailEffect.ShowMessage(ConsultUiMessage.CreateFailed))
                    }
                    is DataResourceResult.Loading -> Unit
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

    private fun showMessage(message: UiMessage) {
        viewModelScope.launch {
            _effect.emit(ConsultDetailEffect.ShowMessage(message))
        }
    }
}
