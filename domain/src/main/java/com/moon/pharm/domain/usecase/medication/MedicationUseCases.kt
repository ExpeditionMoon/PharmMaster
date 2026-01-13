package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import javax.inject.Inject

data class MedicationUseCases @Inject constructor(
    val getMedications: GetMedicationsUseCase,
    val saveMedication: SaveMedicationUseCase,
    val deleteMedication: DeleteMedicationUseCase,

    val getDailyIntakeRecords: GetDailyIntakeRecordsUseCase,
    val toggleIntakeCheck: ToggleIntakeCheckUseCase,
    val getCurrentUserId: GetCurrentUserIdUseCase
)