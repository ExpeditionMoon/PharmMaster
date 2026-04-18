package com.moon.pharm.prescription.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.model.ScannedMedication
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.prescription.ExtractDrugNamesFromOcrUseCase
import com.moon.pharm.prescription.ocr.TextRecognitionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val ocrHelper: TextRecognitionHelper,
    private val extractDrugNamesUseCase: ExtractDrugNamesFromOcrUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<PrescriptionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var isProcessing = false

    fun onTextRecognized(text: String) {
        if (isProcessing) return
        if (text.length > 10 && (text.contains("정") || text.contains("회") || text.contains("캡슐"))) {
            isProcessing = true
            viewModelScope.launch {
                _isLoading.value = true
                delay(500)
                processTextWithAi(text)
            }
        }
    }

    fun analyzeImageFromUri(uri: Uri) {
        if (isProcessing) return
        isProcessing = true
        viewModelScope.launch {
            _isLoading.value = true
            ocrHelper.extractTextFromUri(uri)
                .onSuccess { rawText ->
                    processTextWithAi(rawText)
                }
                .onFailure { exception ->
                    exception.printStackTrace()
                    _isLoading.value = false
                    isProcessing = false
                }
        }
    }

    private suspend fun processTextWithAi(rawText: String) {
        when(val result = extractDrugNamesUseCase(rawText)) {
            is DataResourceResult.Success -> {
                val scannedList = result.resultData.map { ScannedMedication(name = it, dailyCount = 1) }
                _uiEvent.emit(PrescriptionUiEvent.NavigateToCreate(scannedList))
            }
            is DataResourceResult.Failure -> {
                result.exception.printStackTrace()
            }
            is DataResourceResult.Loading -> { }
        }
        _isLoading.value = false
        isProcessing = false
    }
}