package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.UserRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetConsultItemsUseCase @Inject constructor(
    private val repository: ConsultRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<DataResourceResult<List<ConsultItem>>> {
        return repository.getConsultItems()
            .map { result ->
                if (result is DataResourceResult.Success) {
                    val rawList = result.resultData

                    val distinctUserIds = rawList.map { it.userId }.distinct()

                    val nicknameMap = mutableMapOf<String, String>()
                    distinctUserIds.forEach { userId ->
                        val userResult = userRepository.getUserOnce(userId)
                        nicknameMap[userId] = if (userResult is DataResourceResult.Success) {
                            userResult.resultData.nickName
                        } else ""
                    }

                    val enrichedList = rawList.map { item ->
                        item.copy(nickName = nicknameMap[item.userId] ?: "")
                    }

                    DataResourceResult.Success(enrichedList)
                } else {
                    result
                }
            }
    }
}