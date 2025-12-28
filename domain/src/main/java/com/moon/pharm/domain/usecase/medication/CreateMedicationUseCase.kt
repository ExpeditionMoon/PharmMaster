package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.MedicationItem
import com.moon.pharm.domain.repository.MedicationRepository
import javax.inject.Inject

class CreateMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
){
    suspend operator fun invoke(medicationItem: MedicationItem) =
        repository.createMedication(medicationItem)
}