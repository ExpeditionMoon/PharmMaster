package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.datasource.remote.dto.ConsultAnswerDTO
import com.moon.pharm.data.datasource.remote.dto.ConsultImageDTO
import com.moon.pharm.data.datasource.remote.dto.ConsultItemDTO
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

fun ConsultImageDTO.toDomain(): ConsultImage = ConsultImage(
    imageName = imageName,
    imageUrl = imageUrl
)

fun ConsultImage.toDto(): ConsultImageDTO = ConsultImageDTO(
    imageName = imageName,
    imageUrl = imageUrl
)

fun ConsultAnswerDTO.toDomain(): ConsultAnswer = ConsultAnswer(
    answerId = answerId,
    pharmacistId = pharmacistId,
    pharmacistName = pharmacistName,
    content = content,
    createdAt = createdAt?.toDate()?.time ?: 0L
)

fun ConsultAnswer.toDto(): ConsultAnswerDTO = ConsultAnswerDTO(
    answerId = answerId,
    pharmacistId = pharmacistId,
    pharmacistName = pharmacistName,
    content = content,
    createdAt = createdAt.toTimestamp()
)

fun ConsultItemDTO.toDomain(): ConsultItem = ConsultItem(
    id = id,
    userId = userId,
    nickName = nickName,
    pharmacistId = pharmacistId,
    title = title,
    content = content,
    status = ConsultStatus.from(status),
    isPublic = isPublic ?: false,
    images = images.orEmpty().map { it.toDomain() },
    createdAt = createdAt?.toDate()?.time ?: 0L,
    answer = answer?.toDomain()
)

fun ConsultItem.toDto(): ConsultItemDTO = ConsultItemDTO(
    id = id,
    userId = userId,
    nickName = nickName,
    pharmacistId = pharmacistId,
    title = title,
    content = content,
    status = status.name,
    isPublic = isPublic,
    images = images.map { it.toDto() },
    createdAt = createdAt.toTimestamp(),
    answer = answer?.toDto()
)
