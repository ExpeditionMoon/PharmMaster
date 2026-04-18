package com.moon.pharm.domain.usecase.prescription

import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class ExtractDrugNamesFromOcrUseCase @Inject constructor(
    private val ddiRepository: DdiRepository
) {
    suspend operator fun invoke(ocrRawText: String): DataResourceResult<List<String>> {
        if (ocrRawText.trim().length < 2) {
            return DataResourceResult.Failure(IllegalArgumentException("인식된 텍스트가 너무 짧습니다."))
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
}