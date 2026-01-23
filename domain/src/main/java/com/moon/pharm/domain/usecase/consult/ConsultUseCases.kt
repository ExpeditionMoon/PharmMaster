package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.pharmacist.GetPharmacistsByPharmacyUseCase
import com.moon.pharm.domain.usecase.pharmacy.SearchNearbyPharmaciesUseCase
import com.moon.pharm.domain.usecase.pharmacy.SearchPharmacyUseCase
import javax.inject.Inject

data class ConsultUseCases @Inject constructor(
    val getConsultList: GetConsultItemsUseCase,
    val getConsultDetail: GetConsultDetailUseCase,
    val createConsult: CreateConsultUseCase,

    val searchPharmacy: SearchPharmacyUseCase,
    val searchNearbyPharmacies: SearchNearbyPharmaciesUseCase,
    val getPharmacists: GetPharmacistsByPharmacyUseCase,
    val getCurrentUserId: GetCurrentUserIdUseCase,

    val validateConsultForm: ValidateConsultFormUseCase
)