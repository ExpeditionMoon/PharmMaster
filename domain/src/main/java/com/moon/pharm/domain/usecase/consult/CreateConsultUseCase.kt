package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

data class CreateConsultCommand(
    val id: String = "",
    val userId: String,
    val nickName: String,
    val pharmacistId: String,
    val title: String,
    val content: String,
    val isPublic: Boolean,
    val imageUrls: List<String>
)

class CreateConsultUseCase(
    private val consultRepository: ConsultRepository
) {
    operator fun invoke(command: CreateConsultCommand): Flow<DataResourceResult<Unit>> {
        return consultRepository.createConsult(command.toConsultItem())
    }

    private fun CreateConsultCommand.toConsultItem(): ConsultItem {
        return ConsultItem(
            id = id,
            userId = userId,
            nickName = nickName,
            pharmacistId = pharmacistId,
            title = title,
            content = content,
            status = ConsultStatus.WAITING,
            isPublic = isPublic,
            createdAt = System.currentTimeMillis(),
            images = imageUrls.map { url ->
                ConsultImage(
                    imageName = url.substringAfterLast("/").substringBefore("?"),
                    imageUrl = url
                )
            }
        )
    }
}
