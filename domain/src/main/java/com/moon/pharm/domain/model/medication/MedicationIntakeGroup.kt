package com.moon.pharm.domain.model.medication

/** A projection of medications that are notified and displayed together. */
data class MedicationIntakeGroup(
    val id: String,
    val medications: List<Medication>
) {
    init {
        require(id.isNotBlank()) { "A medication intake group requires an id." }
        require(medications.isNotEmpty()) { "A medication intake group requires a medication." }
        require(medications.all { it.intakeGroupId == id }) { "All medications must belong to this group." }
        require(medications.map(Medication::scheduleSignature).distinct().size == 1) {
            "Grouped medications must have the same intake schedule."
        }
    }
}

private fun Medication.scheduleSignature(): List<MedicationScheduleSignature> = schedules
    .map {
        MedicationScheduleSignature(
            time = it.time,
            basis = it.basis,
            mealSlot = it.mealSlot,
            mealInterval = it.mealInterval
        )
    }
    .sortedBy { it.time }

private data class MedicationScheduleSignature(
    val time: String,
    val basis: MedicationScheduleBasis,
    val mealSlot: MealSlot?,
    val mealInterval: MealInterval
)
