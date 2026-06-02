package com.moon.pharm.consult.mapper

import com.moon.pharm.consult.viewmodel.ConsultWriteUiState
import com.moon.pharm.domain.usecase.consult.CreateConsultCommand

object ConsultUiMapper {

    fun toCreateCommand(
        writeState: ConsultWriteUiState,
        currentUserId: String,
        currentUserNickname: String,
        selectedPharmacistId: String,
        uploadedImageUrls: List<String>
    ): CreateConsultCommand {
        return CreateConsultCommand(
            userId = currentUserId,
            nickName = currentUserNickname,
            pharmacistId = selectedPharmacistId,
            title = writeState.title,
            content = writeState.content,
            isPublic = writeState.isPublic,
            imageUrls = uploadedImageUrls
        )
    }
}
