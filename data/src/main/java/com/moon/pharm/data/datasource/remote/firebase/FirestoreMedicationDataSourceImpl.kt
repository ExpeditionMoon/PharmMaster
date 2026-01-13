package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.moon.pharm.data.common.FIELD_MEDICATION_ID
import com.moon.pharm.data.common.FIELD_RECORD_DATE
import com.moon.pharm.data.common.FIELD_SCHEDULE_ID
import com.moon.pharm.data.common.FIELD_USER_ID
import com.moon.pharm.data.common.INTAKE_RECORDS_COLLECTION
import com.moon.pharm.data.common.MEDICATION_COLLECTION
import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.datasource.remote.dto.IntakeRecordDTO
import com.moon.pharm.data.datasource.remote.dto.MedicationDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreMedicationDataSourceImpl @Inject constructor(
    val firestore: FirebaseFirestore
) : MedicationDataSource {

    private val medicationCollection = firestore.collection(MEDICATION_COLLECTION)
    private val intakeCollection = firestore.collection(INTAKE_RECORDS_COLLECTION)

    override fun getMedications(userId: String): Flow<List<MedicationDTO>> {
        return medicationCollection
            .whereEqualTo(FIELD_USER_ID, userId)
            .snapshots()
            .map { it.toObjects(MedicationDTO::class.java) }
    }

    override suspend fun saveMedication(medication: MedicationDTO) {
        val docRef = if (medication.id.isEmpty()) {
            medicationCollection.document()
        } else {
            medicationCollection.document(medication.id)
        }
        docRef.set(medication.copy(id = docRef.id)).await()
    }

    override suspend fun deleteMedication(medicationId: String) {
        medicationCollection.document(medicationId).delete().await()
    }

    override fun getIntakeRecords(userId: String, date: String): Flow<List<IntakeRecordDTO>> {
        return intakeCollection
            .whereEqualTo(FIELD_USER_ID, userId)
            .whereEqualTo(FIELD_RECORD_DATE, date)
            .snapshots()
            .map { it.toObjects(IntakeRecordDTO::class.java) }
    }

    override suspend fun saveIntakeRecord(record: IntakeRecordDTO) {
        val docRef = if (record.id.isEmpty()) {
            intakeCollection.document()
        } else {
            intakeCollection.document(record.id)
        }
        docRef.set(record.copy(id = docRef.id)).await()
    }

    override suspend fun deleteIntakeRecord(medicationId: String, scheduleId: String, date: String) {
        val snapshot = intakeCollection
            .whereEqualTo(FIELD_MEDICATION_ID, medicationId)
            .whereEqualTo(FIELD_SCHEDULE_ID, scheduleId)
            .whereEqualTo(FIELD_RECORD_DATE, date)
            .get()
            .await()

        for (document in snapshot.documents) {
            document.reference.delete().await()
        }
    }
}