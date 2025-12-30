package com.moon.pharm.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.mapper.toDomainMedicationList
import com.moon.pharm.data.mapper.toFirestoreMedicationDTO
import com.moon.pharm.data.remote.dto.MedicationItemDto
import com.moon.pharm.domain.model.MedicationItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override fun getMedicationItems(): Flow<List<MedicationItem>> = callbackFlow {
        val listener = collection.orderBy("alarmTime").addSnapshotListener { snapshot, e ->

            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            try {
                val medicationList = snapshot?.toObjects(MedicationItemDto::class.java) ?: emptyList()
                trySend(medicationList.toDomainMedicationList())
            } catch (mappingError: Exception) {
                close(mappingError)
            }
        }
        awaitClose { listener.remove() }
    }
}