package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.datasource.remote.dto.ConsultAnswerDTO
import com.moon.pharm.data.datasource.remote.dto.ConsultImageDTO
import com.moon.pharm.data.datasource.remote.dto.ConsultItemDTO
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

fun ConsultItemDTO.toDomain(): ConsultItem {
    return ConsultItem(
        id = this.id,
        userId = this.userId,
        nickName = this.nickName,
        pharmacistId = this.pharmacistId,
        title = this.title,
        content = this.content,
        status = try {
            ConsultStatus.valueOf(this.status)
        } catch (e: Exception) {
            ConsultStatus.WAITING
        },
        isPublic = this.isPublic?: false,
        images = this.images?.map { ConsultImage(it.imageName, it.imageUrl) } ?: emptyList(),
        createdAt = this.createdAt?.toDate()?.time ?: 0L,
        answer = this.answer?.toDomain()
    )
}

fun ConsultItem.toDto(): ConsultItemDTO {
    return ConsultItemDTO(
        id = this.id,
        userId = this.userId,
        nickName = this.nickName,
        pharmacistId = this.pharmacistId,
        title = this.title,
        content = this.content,
        status = this.status.name,
        isPublic = this.isPublic,
        images = this.images.map { ConsultImageDTO(it.imageName, it.imageUrl) },
        createdAt = this.createdAt.toTimestamp(),
        answer = this.answer?.let {
            ConsultAnswerDTO(
                answerId = it.answerId,
                pharmacistId = it.pharmacistId,
                pharmacistName = it.pharmacistName,
                content = it.content,
                createdAt = it.createdAt.toTimestamp()
            )
        }
    )
}

fun ConsultAnswerDTO.toDomain(): ConsultAnswer {
    return ConsultAnswer(
        answerId = this.answerId,
        pharmacistId = this.pharmacistId,
        pharmacistName = this.pharmacistName,
        content = this.content,
        createdAt = this.createdAt?.toDate()?.time ?: 0L
    )
}

fun ConsultAnswer.toDto(): ConsultAnswerDTO {
    return ConsultAnswerDTO(
        answerId = this.answerId,
        pharmacistId = this.pharmacistId,
        pharmacistName = this.pharmacistName,
        content = this.content,
        createdAt = this.createdAt.toTimestamp()
    )
}

fun List<ConsultItemDTO>.toDomainList() = this.map { it.toDomain() }
