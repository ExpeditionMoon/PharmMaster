package com.moon.pharm.data.remote.firebase

import android.system.Os.close
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

class FirestoreConsultDataSourceImpl : ConsultDataSource {
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override suspend fun create(consult: ConsultItem) {
        firestore.collection("consult_collec")
//            .document(consult.id)
            .add(consult.toFirestoreConsultDTO())
            .await()
    }

    override fun getConsultItems(): Flow<DataResourceResult<List<ConsultItem>>> = flow {
        emit(DataResourceResult.Loading)
        try {
            // Firestore에서 데이터 가져오는 로직 (임시로 빈 리스트)
            emit(DataResourceResult.Success(emptyList()))
        } catch (e: Exception) {
            emit(DataResourceResult.Failure(e))
        }
    }

    override fun getConsultDetail(id: String): Flow<ConsultItem> = callbackFlow {
        val docRef = firestore.collection("consult_collec").document(id)

        val subscription = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val item = snapshot.toObject(ConsultItem::class.java)
                if (item != null) {
                    trySend(item) // 데이터를 Flow로 전달
                }
            }
        }

        // Flow가 닫힐 때 리스너 해제
        awaitClose { subscription.remove() }
    }
}