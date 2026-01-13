package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.common.PHARMACY_COLLECTION
import com.moon.pharm.data.datasource.PharmacyDataSource
import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestorePharmacyDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PharmacyDataSource {

    private val pharmacyCollection = firestore.collection(PHARMACY_COLLECTION)

    override fun searchExternalPharmacies(query: String): Flow<DataResourceResult<List<PharmacyDTO>>> = flow {
        // TODO: 별도의 GoogleMapDataSource에서 처리
    }

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
                trySend(DataResourceResult.Failure(Exception("Pharmacy not found")))
            }
        }
        awaitClose { registration.remove() }
    }

    override fun savePharmacyToFirestore(pharmacyDTO: PharmacyDTO): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        try {
            pharmacyCollection
                .document(pharmacyDTO.placeId)
                .set(pharmacyDTO)
                .await()
            emit(DataResourceResult.Success(Unit))
        } catch (e: Exception) {
            emit(DataResourceResult.Failure(e))
        }
    }
}