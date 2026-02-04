package com.moon.pharm.prescription.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.component_ui.model.ScannedMedication
import com.moon.pharm.prescription.ocr.TextRecognitionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrescriptionViewModel @Inject constructor(
    private val ocrHelper: TextRecognitionHelper
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<PrescriptionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private var isProcessing = false
    fun onTextRecognized(text: String) {
        if (isProcessing) return

        if (text.length > 10 && (text.contains("정") || text.contains("회"))) {
            isProcessing = true
            processAndNavigate(text)
        }
    }

    fun analyzeImageFromUri(uri: Uri) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = ocrHelper.extractTextFromUri(uri)
            result.onSuccess { text ->
                processAndNavigate(text)
            }.onFailure {
                it.printStackTrace()
                _isLoading.value = false
            }
        }
    }

    private fun processAndNavigate(rawText: String) {
        Log.d("OCR_RAW", rawText)

        val scannedList = mutableListOf<ScannedMedication>()
        val lines = rawText.lines().map { it.trim() }.filter { it.isNotEmpty() }

        val drugKeywords = listOf("정", "캡슐", "연고", "시럽", "액", "겔", "크림", "mg", "g", "ml", "mL", "서방정")
        val frequencyRegex = Regex("1일\\s*(\\d+)회|(\\d+)회")

        val candidateNames = ArrayDeque<String>()

        for (line in lines) {
            val freqMatch = frequencyRegex.find(line)
            val hasDrugKeyword = drugKeywords.any { line.contains(it) }

            val isNoise = listOf(
                "약국", "처방", "조제", "환자", "병원", "합계", "계산서", "납부",
                "영수증", "전화", "Tel", "Fax", "발행", "교부", "번호", "성명", "면허"
            ).any {
                line.contains(it, ignoreCase = true)
            }
            if (freqMatch != null) {
                val count = freqMatch.groupValues[1].toIntOrNull()
                    ?: freqMatch.groupValues[2].toIntOrNull()
                    ?: 1

                var pairedName: String? = null

                if (hasDrugKeyword && !isNoise) {
                    pairedName = line.split(" ").firstOrNull { w -> drugKeywords.any { k -> w.contains(k) } }
                }

                if (pairedName == null && candidateNames.isNotEmpty()) {
                    pairedName = candidateNames.removeFirst()
                }

                if (pairedName != null) {
                    val cleanName = pairedName.split("(")[0].trim()
                    if (scannedList.none { it.name == cleanName }) {
                        scannedList.add(ScannedMedication(cleanName, count))
                    }
                }

            } else {
                if (hasDrugKeyword && !isNoise) {
                    val possibleName = line.split(" ").firstOrNull { w -> drugKeywords.any { k -> w.contains(k) } } ?: line
                    if (possibleName.length < 40) {
                        candidateNames.add(possibleName)
                    }
                }
            }
        }

        while (candidateNames.isNotEmpty()) {
            val name = candidateNames.removeFirst().split("(")[0].trim()
            if (scannedList.none { it.name == name }) {
                scannedList.add(ScannedMedication(name, 3))
            }
        }

        viewModelScope.launch {
            _uiEvent.emit(PrescriptionUiEvent.NavigateToCreate(scannedList))
            _isLoading.value = false
            isProcessing = false
        }
    }
}