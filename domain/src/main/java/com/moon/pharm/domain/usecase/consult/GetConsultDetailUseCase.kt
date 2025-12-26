package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository
import javax.inject.Inject

class GetConsultDetailUseCase @Inject constructor(
    private val repository: ConsultRepository
) {
    operator fun invoke(id: String) = repository.getConsultDetail(id)
}