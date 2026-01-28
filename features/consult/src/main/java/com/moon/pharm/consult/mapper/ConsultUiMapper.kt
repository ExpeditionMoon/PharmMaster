package com.moon.pharm.consult.mapper

import com.moon.pharm.component_ui.common.PATH_DELIMITER
import com.moon.pharm.component_ui.common.QUERY_DELIMITER
import com.moon.pharm.consult.viewmodel.ConsultWriteUiState
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

object ConsultUiMapper {

    fun toDomainModel(
        writeState: ConsultWriteUiState,
        currentUserId: String,
        currentUserNickname: String,
        selectedPharmacistId: String,
        uploadedImageUrls: List<String>
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
            images = uploadedImageUrls.map { url ->
                ConsultImage(
                    imageName = url.substringAfterLast(PATH_DELIMITER).substringBefore(QUERY_DELIMITER),
                    imageUrl = url
                )
            }
        )
    }
}