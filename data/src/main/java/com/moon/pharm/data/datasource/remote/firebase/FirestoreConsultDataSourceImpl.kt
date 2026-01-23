package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.moon.pharm.data.common.CONSULT_COLLECTION
import com.moon.pharm.data.common.FIELD_CREATED_AT
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.datasource.remote.dto.ConsultItemDTO
import com.moon.pharm.data.mapper.toDomain
import com.moon.pharm.data.mapper.toDomainList
import com.moon.pharm.data.mapper.toDto
import com.moon.pharm.domain.model.consult.ConsultItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreConsultDataSourceImpl @Inject constructor(
    val firestore: FirebaseFirestore
) : ConsultDataSource {

    private val collection = firestore.collection(CONSULT_COLLECTION)

    override suspend fun create(consult: ConsultItem) {
        val docRef = if (consult.id.isNotEmpty()) collection.document(consult.id)
        else collection.document()

        val dataToSave = consult.copy(id = docRef.id).toDto()
        docRef.set(dataToSave).await()
    }

    override fun getConsultItems(): Flow<List<ConsultItem>> =
        collection.orderBy(FIELD_CREATED_AT)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(ConsultItemDTO::class.java).toDomainList()
            }

    override fun getConsultDetail(id: String): Flow<ConsultItem?> = flow {
        val snapshot = collection.document(id).get().await()

        if (snapshot.exists()) {
            val dto = snapshot.toObject(ConsultItemDTO::class.java)
            val domain = dto?.toDomain()
            emit(domain)
        }
    }
}