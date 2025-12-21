package com.moon.pharm.domain.usecase.consult

data class ConsultUseCases(
    val getConsultItems: GetConsultItemsUseCase,
    val getConsultDetail: GetConsultDetailUseCase,
    val createConsult: CreateConsultUseCase,
    val getPharmacist: GetPharmacistUseCase
)