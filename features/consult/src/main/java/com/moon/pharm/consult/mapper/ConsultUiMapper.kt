package com.moon.pharm.consult.mapper

import com.moon.pharm.consult.screen.ConsultWriteState
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

object ConsultUiMapper {

    fun toDomainModel(
        writeState: ConsultWriteState,
        currentUserId: String,
        currentUserNickname: String,
        selectedPharmacistId: String
    ): ConsultItem {
        return ConsultItem(
            id = "",
            userId = currentUserId,
            nickName = currentUserNickname,
            pharmacistId = selectedPharmacistId,
            title = writeState.title,
            content = writeState.content,
            status = ConsultStatus.WAITING,
            isPublic = writeState.isPublic,
            createdAt = System.currentTimeMillis(),
            images = writeState.images.map { path ->
                ConsultImage(path.substringAfterLast("/"), path)
            }
        )
    }
}