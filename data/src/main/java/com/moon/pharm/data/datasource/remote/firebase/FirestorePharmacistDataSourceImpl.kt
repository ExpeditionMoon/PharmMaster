package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.common.FIELD_NICKNAME
import com.moon.pharm.data.common.FIELD_PLACE_ID
import com.moon.pharm.data.common.PHARMACIST_COLLECTION
import com.moon.pharm.data.datasource.PharmacistDataSource
import com.moon.pharm.data.datasource.remote.dto.PharmacistDTO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestorePharmacistDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PharmacistDataSource {
    private val pharmacistCollection = firestore.collection(PHARMACIST_COLLECTION)

    override suspend fun savePharmacist(pharmacistDto: PharmacistDTO) {
        pharmacistCollection.document(pharmacistDto.userId).set(pharmacistDto).await()
    }

    override fun getPharmacistById(pharmacistId: String): Flow<PharmacistDTO> = callbackFlow {
        val docRef = pharmacistCollection.document(pharmacistId)

        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val dto = snapshot.toObject(PharmacistDTO::class.java)
                if (dto != null) trySend(dto)
                else close(Exception("Mapping failed"))
            } else {
                close(Exception("Pharmacist not found"))
            }
        }
        awaitClose { registration.remove() }
    }

    override fun getPharmacistsByPlaceId(placeId: String): Flow<List<PharmacistDTO>> = callbackFlow {
        val query = pharmacistCollection.whereEqualTo(FIELD_PLACE_ID, placeId)

        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val dtos = snapshot?.toObjects(PharmacistDTO::class.java) ?: emptyList()
            trySend(dtos)
        }
        awaitClose { registration.remove() }
    }

    override suspend fun updatePharmacistNickname(userId: String, newNickname: String) {
        pharmacistCollection.document(userId).update(FIELD_NICKNAME, newNickname).await()
    }
}