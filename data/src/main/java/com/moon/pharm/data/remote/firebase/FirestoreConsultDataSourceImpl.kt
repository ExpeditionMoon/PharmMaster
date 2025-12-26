package com.moon.pharm.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.mapper.toDomainConsult
import com.moon.pharm.data.mapper.toDomainConsultList
import com.moon.pharm.data.mapper.toFirestoreConsultDTO
import com.moon.pharm.data.remote.dto.ConsultItemDTO
import com.moon.pharm.domain.model.ConsultItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.jvm.java

class FirestoreConsultDataSourceImpl @Inject constructor(
    val firestore: FirebaseFirestore
) : ConsultDataSource {

    private val collection = firestore.collection("consult_collec")

    override suspend fun create(consult: ConsultItem) {
        val docRef = if (consult.id.isNotEmpty()) collection.document(consult.id)
        else collection.document()

        val dataToSave = consult.toFirestoreConsultDTO()
        docRef.set(dataToSave).await()
    }

    override fun getConsultItems(): Flow<List<ConsultItem>> = callbackFlow {
        val listener = collection.orderBy("createdAt").addSnapshotListener { snapshot, e ->

                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                try {
                    val consultList = snapshot?.toObjects(ConsultItemDTO::class.java) ?: emptyList()
                    trySend(consultList.toDomainConsultList())
                } catch (mappingError: Exception) {
                    close(mappingError)
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getConsultDetail(id: String): Flow<ConsultItem> = callbackFlow {
        val docRef = collection.document(id)

        val subscription = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val item = snapshot.toObject(ConsultItemDTO::class.java)
                if (item != null) {
                    trySend(item.toDomainConsult())
                }
            }
        }
        awaitClose { subscription.remove() }
    }
}