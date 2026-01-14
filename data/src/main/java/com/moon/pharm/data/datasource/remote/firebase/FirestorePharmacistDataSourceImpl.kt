package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.common.FIELD_PLACE_ID
import com.moon.pharm.data.common.FIELD_USER_TYPE
import com.moon.pharm.data.common.PHARMACIST_COLLECTION
import com.moon.pharm.data.common.USER_COLLECTION
import com.moon.pharm.data.common.USER_TYPE_PHARMACIST
import com.moon.pharm.data.datasource.PharmacistDataSource
import com.moon.pharm.data.datasource.remote.dto.PharmacistDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestorePharmacistDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PharmacistDataSource {
    private val pharmacistCollection = firestore.collection(PHARMACIST_COLLECTION)
    private val userCollection = firestore.collection(USER_COLLECTION)

    override suspend fun savePharmacist(pharmacistDto: PharmacistDTO): DataResourceResult<Unit> {
        return try {
            pharmacistCollection.document(pharmacistDto.userId).set(pharmacistDto).await()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }

    override fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<PharmacistDTO>> = callbackFlow {
        val docRef = userCollection.document(pharmacistId)

        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(DataResourceResult.Failure(error))
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val dto = snapshot.toObject(PharmacistDTO::class.java)
                if (dto != null) trySend(DataResourceResult.Success(dto))
                else trySend(DataResourceResult.Failure(Exception("Mapping failed")))
            } else {
                trySend(DataResourceResult.Failure(Exception("Pharmacist not found")))
            }
        }
        awaitClose { registration.remove() }
    }

    override fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<PharmacistDTO>>> = callbackFlow {
        trySend(DataResourceResult.Loading)
        val query = userCollection
            .whereEqualTo(FIELD_USER_TYPE, USER_TYPE_PHARMACIST)
            .whereEqualTo(FIELD_PLACE_ID, placeId)

        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(DataResourceResult.Failure(error))
                return@addSnapshotListener
            }
            val dtos = snapshot?.toObjects(PharmacistDTO::class.java) ?: emptyList()
            trySend(DataResourceResult.Success(dtos))
        }
        awaitClose { registration.remove() }
    }
}