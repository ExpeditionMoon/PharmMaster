package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.moon.pharm.data.common.CONSULT_COLLECTION
import com.moon.pharm.data.common.ERROR_MSG_CONSULT_NOT_FOUND
import com.moon.pharm.data.common.FIELD_ANSWER
import com.moon.pharm.data.common.FIELD_CREATED_AT
import com.moon.pharm.data.common.FIELD_STATUS
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.datasource.remote.dto.ConsultAnswerDTO
import com.moon.pharm.data.datasource.remote.dto.ConsultItemDTO
import com.moon.pharm.domain.model.consult.ConsultStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreConsultDataSourceImpl @Inject constructor(
    val firestore: FirebaseFirestore
) : ConsultDataSource {

    private val collection = firestore.collection(CONSULT_COLLECTION)

    override suspend fun create(consult: ConsultItemDTO) {
        val docRef = if (consult.id.isNotEmpty()) collection.document(consult.id)
        else collection.document()

        val dataToSave = consult.copy(id = docRef.id)
        docRef.set(dataToSave).await()
    }

    override fun getConsultItems(): Flow<List<ConsultItemDTO>> =
        collection.orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(ConsultItemDTO::class.java)
            }

    override fun getConsultDetail(id: String): Flow<ConsultItemDTO?> = flow {
        val snapshot = collection.document(id).get().await()

        if (snapshot.exists()) {
            val dto = snapshot.toObject(ConsultItemDTO::class.java)
            emit(dto)
        }
    }

    override suspend fun updateConsultAnswer(consultId: String, answerDto: ConsultAnswerDTO): ConsultItemDTO {
        val consultRef = collection.document(consultId)
        return firestore.runTransaction { transaction ->
            val snapshot = transaction.get(consultRef)
            val currentDto = snapshot.toObject(ConsultItemDTO::class.java)
                ?: throw FirebaseFirestoreException(
                    ERROR_MSG_CONSULT_NOT_FOUND,
                    FirebaseFirestoreException.Code.NOT_FOUND
                )

            transaction.update(consultRef, FIELD_ANSWER, answerDto)
            transaction.update(consultRef, FIELD_STATUS, ConsultStatus.COMPLETED.name)
            currentDto.copy(
                answer = answerDto,
                status = ConsultStatus.COMPLETED.name
            )
        }.await()
    }
}