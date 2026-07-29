package com.moon.pharm.prescription.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.domain.model.prescription.PrescriptionException
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.prescription.ExtractDrugNamesFromOcrUseCase
import com.moon.pharm.prescription.ocr.OcrTextExtractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val ocrTextExtractor: OcrTextExtractor,
    private val extractDrugNamesUseCase: ExtractDrugNamesFromOcrUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<PrescriptionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow<PrescriptionUiState>(PrescriptionUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private var latestImageUri: Uri? = null
    private var latestOcrText: String? = null

    fun onTextRecognized(text: String) {
        latestImageUri = null
        submitOcrText(text)
    }

    fun analyzeImageFromUri(uri: Uri) {
        if (_uiState.value is PrescriptionUiState.Loading) return
        latestImageUri = uri
        latestOcrText = null
        viewModelScope.launch {
            _uiState.value = PrescriptionUiState.Loading
            ocrTextExtractor.extractTextFromUri(uri)
                .onSuccess { rawText ->
                    processTextWithAi(rawText)
                }
                .onFailure {
                    _uiState.value = PrescriptionUiState.Error(PrescriptionException.OcrFailed.toPrescriptionError())
                }
        }
    }

    fun retry() {
        latestOcrText?.let(::submitOcrText)
            ?: latestImageUri?.let(::analyzeImageFromUri)
    }

    private fun submitOcrText(rawText: String) {
        if (_uiState.value is PrescriptionUiState.Loading) return
        latestOcrText = rawText
        viewModelScope.launch {
            _uiState.value = PrescriptionUiState.Loading
            processTextWithAi(rawText)
        }
    }

    private suspend fun processTextWithAi(rawText: String) {
        when (val result = extractDrugNamesUseCase(rawText)) {
            is DataResourceResult.Success -> {
                _uiEvent.emit(PrescriptionUiEvent.NavigateToCreate(result.resultData))
                _uiState.value = PrescriptionUiState.Idle
            }
            is DataResourceResult.Failure -> {
                _uiState.value = PrescriptionUiState.Error(result.exception.toPrescriptionError())
            }
            DataResourceResult.Loading -> Unit
        }
    }

    private fun Throwable.toPrescriptionError(): PrescriptionError = when (this) {
        PrescriptionException.Network -> PrescriptionError.NETWORK
        PrescriptionException.OcrNoText,
        PrescriptionException.DrugNameNotFound,
        PrescriptionException.OcrFailed -> PrescriptionError.OCR
        else -> PrescriptionError.GEMINI
    }
}
