package com.moon.pharm.domain.alarm

import com.moon.pharm.domain.model.medication.Medication

interface AlarmScheduler {
    fun schedule(medication: Medication)

    fun schedule(medication: Medication, groupMedicationNames: List<String>) {
        schedule(medication)
    }

    fun cancel(medicationId: String)
    fun reschedule(alarm: MedicationAlarm)
}
