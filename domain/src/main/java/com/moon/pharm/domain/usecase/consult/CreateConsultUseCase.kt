package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import javax.inject.Inject

class CreateConsultUseCase @Inject constructor(
    private val repository: ConsultRepository
) {
    operator fun invoke(consultItem: ConsultItem) =
        repository.createConsult(consultItem)
}