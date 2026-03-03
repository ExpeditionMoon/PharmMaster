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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ConsultRepositoryImpl @Inject constructor(
    private val fcmApi: FcmApi,
    private val dataSource: ConsultDataSource,
    private val imageDataSource: ImageDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ConsultRepository {

    private fun Throwable.toConsultException(): ConsultException = when {
        this is FirebaseFirestoreException && this.code == FirebaseFirestoreException.Code.NOT_FOUND -> ConsultException.NotFound()
        this is FirebaseFirestoreException -> ConsultException.Network()
        else -> ConsultException.Unknown(this.message)
    }

    private fun <T> wrapOperation(
        operation: suspend () -> T
    ): Flow<DataResourceResult<T>> = flow {
        emit(DataResourceResult.Loading)

        val result = runCatching {
            operation()
        }.fold(
            onSuccess = { DataResourceResult.Success(it) },
            onFailure = { e ->
                DataResourceResult.Failure(e.toConsultException())
            }
        )
        emit(result)
    }.flowOn(ioDispatcher)

    private fun <T> Flow<T>.asDataResourceResult(): Flow<DataResourceResult<T>> {
        return this
            .map { DataResourceResult.Success(it) as DataResourceResult<T> }
            .onStart { emit(DataResourceResult.Loading) }
            .catch { e ->
                emit(DataResourceResult.Failure(e.toConsultException()))
            }
            .flowOn(ioDispatcher)
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

    override suspend fun uploadImage(uri: String, userId: String): String {
        return imageDataSource.uploadImage(uri, userId)
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
    ): DataResourceResult<Unit> = runCatching {
        val request = FcmSendRequest(
            targetToken = targetToken, title = title, body = body, consultId = consultId
        )
        val response = fcmApi.sendNotification(request)

        if (!response.success) {
            val errorMsg = response.error ?: ERR_UNKNOWN_SERVER
            throw ConsultException.Unknown("$ERR_FCM_FAILED$errorMsg")
        }
    }.fold(onSuccess = { DataResourceResult.Success(Unit) }, onFailure = { e ->
        val error = e as? ConsultException ?: ConsultException.Unknown(e.message)
        DataResourceResult.Failure(error)
    })

    override suspend fun updatePharmacistNicknameInAnswers(
        userId: String, newNickname: String
    ): DataResourceResult<Unit> = runCatching {
        dataSource.updatePharmacistNicknameInAnswers(userId, newNickname)
    }.fold(onSuccess = { DataResourceResult.Success(Unit) }, onFailure = { e ->
        val error = e as? ConsultException ?: ConsultException.Unknown(e.message)
        DataResourceResult.Failure(error)
    })
}