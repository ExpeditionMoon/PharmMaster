package com.moon.pharm.domain.usecase.medication

import javax.inject.Inject

data class MedicationUseCases @Inject constructor(
    val createMedicationUseCase: CreateMedicationUseCase,
    val getMedicationItemsUseCase: GetMedicationItemsUseCase,
)