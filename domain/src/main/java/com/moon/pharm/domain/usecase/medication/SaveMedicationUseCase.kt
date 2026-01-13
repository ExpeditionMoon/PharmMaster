package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.repository.MedicationRepository
import javax.inject.Inject

class SaveMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
){
    operator fun invoke(medication: Medication) = repository.saveMedication(medication)
}