package com.moon.pharm.domain.usecase.consult

import javax.inject.Inject

data class ConsultUseCases @Inject constructor(
    val createConsultUseCase: CreateConsultUseCase,
    val getConsultItemsUseCase: GetConsultItemsUseCase,
    val getConsultDetailUseCase: GetConsultDetailUseCase,
    val getPharmacistUseCase: GetPharmacistUseCase
)