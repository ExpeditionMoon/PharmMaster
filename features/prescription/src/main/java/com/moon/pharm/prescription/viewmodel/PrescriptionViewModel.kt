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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val ocrHelper: TextRecognitionHelper,
    private val extractDrugNamesUseCase: ExtractDrugNamesFromOcrUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<PrescriptionEffect>()
    val effect: SharedFlow<PrescriptionEffect> = _effect.asSharedFlow()

    private val _uiState = MutableStateFlow(PrescriptionUiState())
    val uiState: StateFlow<PrescriptionUiState> = _uiState.asStateFlow()

    private var isProcessing = false

    fun onTextRecognized(text: String) {
        if (isProcessing) return
        if (text.length > 10 && (text.contains("정") || text.contains("회") || text.contains("캡슐"))) {
            isProcessing = true
            viewModelScope.launch {
                setLoading(true)
                delay(500)
                processTextWithAi(text)
            }
        }
    }

    fun analyzeImageFromUri(uri: Uri) {
        if (isProcessing) return
        isProcessing = true
        viewModelScope.launch {
            setLoading(true)
            ocrHelper.extractTextFromUri(uri)
                .onSuccess { rawText ->
                    processTextWithAi(rawText)
                }
                .onFailure { exception ->
                    exception.printStackTrace()
                    setLoading(false)
                    isProcessing = false
                }
        }
    }

    private suspend fun processTextWithAi(rawText: String) {
        when (val result = extractDrugNamesUseCase(rawText)) {
            is DataResourceResult.Success -> {
                val scannedList = result.resultData.map { ScannedMedication(name = it, dailyCount = 1) }
                _effect.emit(PrescriptionEffect.NavigateToCreate(scannedList))
            }
            is DataResourceResult.Failure -> {
                result.exception.printStackTrace()
            }
            is DataResourceResult.Loading -> { }
        }
        setLoading(false)
        isProcessing = false
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }
}
