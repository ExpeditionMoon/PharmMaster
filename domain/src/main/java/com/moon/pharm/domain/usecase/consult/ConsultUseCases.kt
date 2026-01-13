package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.auth.GetPharmacistDetailUseCase
import com.moon.pharm.domain.usecase.auth.GetPharmacistsByPharmacyUseCase
import com.moon.pharm.domain.usecase.pharmacy.SearchPharmacyUseCase
import javax.inject.Inject

data class ConsultUseCases @Inject constructor(
    val getConsultList: GetConsultItemsUseCase,
    val getConsultDetail: GetConsultDetailUseCase,
    val createConsult: CreateConsultUseCase,

    val searchPharmacy: SearchPharmacyUseCase,
    val getPharmacists: GetPharmacistsByPharmacyUseCase,
    val getPharmacistDetail: GetPharmacistDetailUseCase,
    val getCurrentUserId: GetCurrentUserIdUseCase
)