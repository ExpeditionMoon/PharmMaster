package com.moon.pharm.domain.usecase.ddi

import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult

class AnalyzeDdiUseCase(
    private val ddiRepository: DdiRepository
) {
    suspend operator fun invoke(drugs: List<String>): DataResourceResult<DdiResult> {
        if (drugs.size < 2) {
            return DataResourceResult.Failure(IllegalArgumentException("?í˜¸?‘ìš©??ë¶„ì„?˜ë ¤ë©?ìµœì†Œ 2ê°??´ìƒ???½ë¬¼???„ìš”?©ë‹ˆ??"))
        }
        return ddiRepository.analyzeDrugInteractions(drugs)
    }
}