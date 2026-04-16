package com.moon.pharm.domain.usecase.ddi

import com.moon.pharm.domain.model.ddi.DdiResult
import com.moon.pharm.domain.repository.DdiRepository
import com.moon.pharm.domain.result.DataResourceResult
import javax.inject.Inject

class AnalyzeDdiUseCase @Inject constructor(
    private val ddiRepository: DdiRepository
) {
    suspend operator fun invoke(drugs: List<String>): DataResourceResult<DdiResult> {
        if (drugs.size < 2) {
            return DataResourceResult.Failure(IllegalArgumentException("상호작용을 분석하려면 최소 2개 이상의 약물이 필요합니다."))
        }
        return ddiRepository.analyzeDrugInteractions(drugs)
    }
}