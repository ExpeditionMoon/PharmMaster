package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.result.mapResult
import com.moon.pharm.domain.usecase.auth.GetCurrentUserIdUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetConsultItemsUseCase @Inject constructor(
    private val repository: ConsultRepository,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) {
    operator fun invoke(): Flow<DataResourceResult<List<ConsultItem>>> {
        val currentUserId = getCurrentUserIdUseCase()
        return repository.getConsultItems().map { result ->
            result.mapResult { items ->
                items.filter { item ->
                    item.isPublic || (currentUserId != null && item.userId == currentUserId)
                }
            }
        }
    }
}