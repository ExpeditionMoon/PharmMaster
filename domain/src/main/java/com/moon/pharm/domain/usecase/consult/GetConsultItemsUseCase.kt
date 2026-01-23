package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import com.moon.pharm.domain.usecase.user.GetNicknameUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetConsultItemsUseCase @Inject constructor(
    private val repository: ConsultRepository,
    private val getNicknameUseCase: GetNicknameUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) {
    operator fun invoke(): Flow<DataResourceResult<List<ConsultItem>>> = flow {
        emit(DataResourceResult.Loading)

        val currentUserId = getCurrentUserIdUseCase()

        val listResult = repository.getConsultItems()
            .filter { it !is DataResourceResult.Loading }
            .first()

        if (listResult is DataResourceResult.Success) {
            val rawList = listResult.resultData
            val filteredList = rawList.filter { item ->
                item.isPublic || (currentUserId != null && item.userId == currentUserId)
            }

            val distinctUserIds = filteredList.map { it.userId }.distinct()
            val nicknameMap = mutableMapOf<String, String>()

            distinctUserIds.forEach { userId ->
                nicknameMap[userId] = getNicknameUseCase.getNickname(userId)
            }

            val enrichedList = filteredList.map { item ->
                item.copy(nickName = nicknameMap[item.userId] ?: "")
            }

            emit(DataResourceResult.Success(enrichedList))

        } else if (listResult is DataResourceResult.Failure) {
            emit(DataResourceResult.Failure(listResult.exception))
        }
    }
}