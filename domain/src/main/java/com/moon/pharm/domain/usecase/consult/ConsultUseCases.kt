package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.usecase.pharmacy.SearchPharmacyUseCase
import javax.inject.Inject

data class ConsultUseCases @Inject constructor(
    val getConsultList: GetConsultItemsUseCase,
    val getConsultDetail: GetConsultDetailUseCase,
    val registerAnswer: RegisterAnswerUseCase,
    val searchPharmacy: SearchPharmacyUseCase,
    val validateConsultForm: ValidateConsultFormUseCase,

    val consultRepository: ConsultRepository,
    val pharmacistRepository: PharmacistRepository,
    val authRepository: AuthRepository
)