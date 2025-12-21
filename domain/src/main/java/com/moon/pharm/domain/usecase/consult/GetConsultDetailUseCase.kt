package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository

class GetConsultDetailUseCase(private val repository: ConsultRepository) {
    operator fun invoke(id: String) = repository.getConsultDetail(id)
}