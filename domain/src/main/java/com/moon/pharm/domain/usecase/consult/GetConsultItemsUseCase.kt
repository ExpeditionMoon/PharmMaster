package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.user.GetNicknameUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetConsultItemsUseCase @Inject constructor(
    private val repository: ConsultRepository,
    private val getNicknameUseCase: GetNicknameUseCase
) {
    operator fun invoke(): Flow<DataResourceResult<List<ConsultItem>>> {
        return repository.getConsultItems()
            .map { result ->
                if (result is DataResourceResult.Success) {
                    val rawList = result.resultData

                    val distinctUserIds = rawList.map { it.userId }.distinct()

                    val nicknameMap = mutableMapOf<String, String>()
                    distinctUserIds.forEach { userId ->
                        nicknameMap[userId] = getNicknameUseCase.getNickname(userId)
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