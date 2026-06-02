package com.moon.pharm.domain.usecase.prescription

import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult

class ExtractDrugNamesFromOcrUseCase(
    private val ddiRepository: DdiRepository
) {
    suspend operator fun invoke(ocrRawText: String): DataResourceResult<List<String>> {
        if (ocrRawText.trim().length < 2) {
            return DataResourceResult.Failure(IllegalArgumentException("?¸ì‹???ìŠ¤?¸ê? ?ˆë¬´ ì§§ìŠµ?ˆë‹¤."))
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