package com.moon.pharm.data.datasource.remote.firebase

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.common.ERROR_MSG_PHARMACY_NOT_FOUND
import com.moon.pharm.data.common.PHARMACY_COLLECTION
import com.moon.pharm.data.datasource.PharmacyStorageDataSource
import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import com.moon.pharm.domain.result.DataResourceResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestorePharmacyDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : PharmacyStorageDataSource {

    private val pharmacyCollection = firestore.collection(PHARMACY_COLLECTION)

    override fun getPharmacyFromFirestore(placeId: String): Flow<DataResourceResult<PharmacyDTO>> = callbackFlow {
        trySend(DataResourceResult.Loading)

        val docRef = pharmacyCollection.document(placeId)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(DataResourceResult.Failure(error))
                return@addSnapshotListener
            }

            val dto = snapshot?.toObject(PharmacyDTO::class.java)
            if (dto != null) {
                trySend(DataResourceResult.Success(dto))
            } else {
                trySend(DataResourceResult.Failure(Exception(ERROR_MSG_PHARMACY_NOT_FOUND)))
            }
        }
        awaitClose { registration.remove() }
    }

    override suspend fun savePharmacyToFirestore(pharmacyDTO: PharmacyDTO): DataResourceResult<Unit> {
        return try {
            pharmacyCollection
                .document(pharmacyDTO.placeId)
                .set(pharmacyDTO)
                .await()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }
}