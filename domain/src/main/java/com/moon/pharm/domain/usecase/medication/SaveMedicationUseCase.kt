package com.moon.pharm.domain.usecase.medication

import com.moon.pharm.domain.model.medication.Medication
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveMedicationUseCase @Inject constructor(
    private val medicationRepository: MedicationRepository
) {
    operator fun invoke(medication: Medication): Flow<DataResourceResult<Unit>> {
        return medicationRepository.saveMedication(medication)
    }
}
