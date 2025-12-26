package com.moon.pharm.data.remote.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.mapper.toFirestoreConsultDTO
import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreConsultDataSourceImpl @Inject constructor(
    val firestore: FirebaseFirestore
) : ConsultDataSource {

    private val collection = firestore.collection("consult_collec")

    override suspend fun create(consult: ConsultItem) {
        Log.d("FirestoreTest", "create() called")

        val dataToSave = consult.toFirestoreConsultDTO()
        val docRef = if (consult.id.isNotEmpty())
            collection.document(consult.id)
        else
            collection.document()
        docRef.set(dataToSave).await()

        Log.d("FirestoreTest", "Firestore add success")
    }

    override fun getConsultItems(): Flow<DataResourceResult<List<ConsultItem>>> = flow {
        emit(DataResourceResult.Loading)
        try {
            emit(DataResourceResult.Success(emptyList()))
        } catch (e: Exception) {
            emit(DataResourceResult.Failure(e))
        }
    }

    override fun getConsultDetail(id: String): Flow<ConsultItem> = callbackFlow {
        val docRef = collection.document(id)

        val subscription = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val item = snapshot.toObject(ConsultItem::class.java)
                if (item != null) {
                    trySend(item)
                }
            }
        }
        awaitClose { subscription.remove() }
    }
}