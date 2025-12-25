package com.moon.pharm.domain.usecase.consult

data class ConsultUseCases(
    val createConsultUseCase: CreateConsultUseCase,
    val getConsultItemsUseCase: GetConsultItemsUseCase,
    val getConsultDetailUseCase: GetConsultDetailUseCase,
    val getPharmacistUseCase: GetPharmacistUseCase
)