package com.moon.pharm.domain.usecase.prescription

import com.moon.pharm.domain.model.prescription.PrescriptionException
import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult

class ExtractDrugNamesFromOcrUseCase(
    private val ddiRepository: DdiRepository
) {
    suspend operator fun invoke(ocrRawText: String): DataResourceResult<List<String>> {
        if (ocrRawText.trim().length < MINIMUM_OCR_TEXT_LENGTH) {
            return DataResourceResult.Failure(PrescriptionException.OcrNoText)
        }
        val safeText = maskSensitiveInfo(ocrRawText)
        return ddiRepository.extractDrugNamesFromText(safeText)
    }

    private fun maskSensitiveInfo(rawText: String): String {
        var maskedText = rawText
        val ssnRegex = Regex("\\b\\d{6}[-*\\s]?\\d{7}\\b")
        maskedText = maskedText.replace(ssnRegex, "******-*******")

        val phoneRegex = Regex("\\b01[016789][-*\\s]?\\d{3,4}[-*\\s]?\\d{4}\\b")
        maskedText = maskedText.replace(phoneRegex, "010-****-****")

        return maskedText
    }

    private companion object {
        const val MINIMUM_OCR_TEXT_LENGTH = 2
    }
}
