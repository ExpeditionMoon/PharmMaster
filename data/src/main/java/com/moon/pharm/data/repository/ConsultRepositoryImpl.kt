package com.moon.pharm.data.repository

import com.google.firebase.firestore.FirebaseFirestoreException
import com.moon.pharm.data.common.NotificationConstants.ERR_FCM_FAILED
import com.moon.pharm.data.common.NotificationConstants.ERR_UNKNOWN_SERVER
import com.moon.pharm.data.common.NotificationConstants.MSG_ANSWER_BODY
import com.moon.pharm.data.common.NotificationConstants.MSG_ANSWER_TITLE
import com.moon.pharm.data.common.NotificationConstants.MSG_NEW_CONSULT_BODY
import com.moon.pharm.data.common.NotificationConstants.MSG_NEW_CONSULT_TITLE
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.datasource.ImageDataSource
import com.moon.pharm.data.datasource.remote.dto.toDomain
import com.moon.pharm.data.datasource.remote.dto.toDto
import com.moon.pharm.data.datasource.remote.fcm.FcmApi
import com.moon.pharm.data.datasource.remote.fcm.FcmSendRequest
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultException
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ConsultRepositoryImpl @Inject constructor(
    private val fcmApi: FcmApi,
    private val dataSource: ConsultDataSource,
    private val imageDataSource: ImageDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ConsultRepository {

    private fun Throwable.toConsultException(): ConsultException = when {
        this is ConsultException -> this
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> ConsultException.NotFound()
        this is FirebaseFirestoreException -> ConsultException.Network()
        else -> ConsultException.Unknown(message)
    }

    private fun <T> wrapOperation(
        operation: suspend () -> T
    ): Flow<DataResourceResult<T>> = dataResourceFlow(
        dispatcher = ioDispatcher,
        errorMapper = { it.toConsultException() },
        operation = operation
    )

    private fun <T> Flow<T>.asDataResourceResult(): Flow<DataResourceResult<T>> {
        return asDataResourceResult(
            dispatcher = ioDispatcher,
            errorMapper = { it.toConsultException() }
        )
    }

    override fun getConsultItems(): Flow<DataResourceResult<List<ConsultItem>>> {
        return dataSource.getConsultItems()
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
    }

    override fun createConsult(consultInfo: ConsultItem) =
        wrapOperation { dataSource.create(consultInfo.toDto()) }

    override fun getConsultDetail(id: String): Flow<DataResourceResult<ConsultItem>> {
        return dataSource.getConsultDetail(id)
            .map { dto -> dto?.toDomain() ?: throw ConsultException.NotFound() }
            .asDataResourceResult()
    }

    override fun getMyConsult(userId: String): Flow<DataResourceResult<List<ConsultItem>>> {
        return dataSource.getMyConsults(userId)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
    }

    override fun updateConsult(
        consultId: String, title: String, content: String, isPublic: Boolean
    ): Flow<DataResourceResult<Unit>> = wrapOperation {
        dataSource.updateConsult(consultId, title, content, isPublic)
    }

    override fun deleteConsult(consultId: String): Flow<DataResourceResult<Unit>> = wrapOperation {
        dataSource.deleteConsult(consultId)
    }

    override fun getMyAnsweredConsultList(userId: String): Flow<DataResourceResult<List<ConsultItem>>> {
        return dataSource.getMyAnsweredConsults(userId)
            .map { dtoList -> dtoList.map { it.toDomain() } }
            .asDataResourceResult()
    }

    override fun registerAnswer(
        consultId: String,
        answer: ConsultAnswer
    ): Flow<DataResourceResult<ConsultItem>> = wrapOperation {
        val answerDto = answer.toDto()
        val updatedDto = dataSource.updateConsultAnswer(consultId, answerDto)
        updatedDto.toDomain()
    }

    override fun deleteConsultAnswer(consultId: String): Flow<DataResourceResult<Unit>> = wrapOperation {
        dataSource.deleteConsultAnswer(consultId)
    }

    override suspend fun uploadImage(uri: String, userId: String): String {
        return withContext(ioDispatcher) {
            imageDataSource.uploadImage(uri, userId)
        }
    }

    override suspend fun sendAnswerNotification(
        targetUserToken: String,
        consultId: String
    ): DataResourceResult<Unit> {
        return sendFcmNotification(
            targetToken = targetUserToken,
            title = MSG_ANSWER_TITLE,
            body = MSG_ANSWER_BODY,
            consultId = consultId
        )
    }

    override suspend fun sendNewConsultNotification(
        targetToken: String,
        consultId: String
    ): DataResourceResult<Unit> {
        return sendFcmNotification(
            targetToken = targetToken,
            title = MSG_NEW_CONSULT_TITLE,
            body = MSG_NEW_CONSULT_BODY,
            consultId = consultId
        )
    }

    private suspend fun sendFcmNotification(
        targetToken: String, title: String, body: String, consultId: String
    ): DataResourceResult<Unit> =
        runDataResourceOperation(
            dispatcher = ioDispatcher,
            timeoutMillis = 5_000L,
            errorMapper = { it.toConsultException() }
        ) {
            val request = FcmSendRequest(
                targetToken = targetToken, title = title, body = body, consultId = consultId
            )
            val response = fcmApi.sendNotification(request)

            if (!response.success) {
                val errorMsg = response.error ?: ERR_UNKNOWN_SERVER
                throw ConsultException.Unknown("$ERR_FCM_FAILED$errorMsg")
            }
        }

    override suspend fun updatePharmacistNicknameInAnswers(
        userId: String, newNickname: String
    ): DataResourceResult<Unit> =
        runDataResourceOperation(
            dispatcher = ioDispatcher,
            errorMapper = { it.toConsultException() }
        ) {
            dataSource.updatePharmacistNicknameInAnswers(userId, newNickname)
        }
}
