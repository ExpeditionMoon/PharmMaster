package com.moon.pharm.prescription.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.prescription.ocr.TextRecognitionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val ocrHelper: TextRecognitionHelper
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<PrescriptionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onTextRecognized(text: String) {
        processAndNavigate(text)
    }

    fun analyzeImageFromUri(uri: Uri) {
        viewModelScope.launch {
            val result = ocrHelper.extractTextFromUri(uri)
            result.onSuccess { text ->
                processAndNavigate(text)
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    private fun processAndNavigate(rawText: String) {
        val name = rawText.lines().find { it.contains("정") || it.contains("캡슐") } ?: ""
        val count = if (rawText.contains("3회")) 3 else 1

        viewModelScope.launch {
            _uiEvent.emit(PrescriptionUiEvent.NavigateToCreate(name, count))
        }
    }
}