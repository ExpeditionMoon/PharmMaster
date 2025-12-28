package com.moon.pharm.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.mapper.toFirestoreMedicationDTO
import com.moon.pharm.domain.model.MedicationItem
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreMedicationDataSourceImpl @Inject constructor(
    val firestore: FirebaseFirestore
) : MedicationDataSource {

    private val collection = firestore.collection("medication_collec")

    override suspend fun create(medication: MedicationItem) {
        val docRef = if (medication.id.isNotEmpty()) collection.document(medication.id)
        else collection.document()

        val dataToSave = medication.toFirestoreMedicationDTO()
        docRef.set(dataToSave).await()
    }

}