package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus

@IgnoreExtraProperties
data class ConsultItemDTO(
    @DocumentId
    var id: String = EMPTY_STRING,

    var userId: String = EMPTY_STRING,
    var nickName: String = EMPTY_STRING,
    var pharmacistId: String? = null,
    var title: String = EMPTY_STRING,
    var content: String = EMPTY_STRING,
    var status: String = ConsultStatus.WAITING.name,

    @get:PropertyName("public")
    @set:PropertyName("public")
    var isPublic: Boolean? = null,

    var images: List<ConsultImageDTO>? = null,

    @ServerTimestamp
    var createdAt: Timestamp? = null,

    var answer: ConsultAnswerDTO? = null,
)

@IgnoreExtraProperties
data class ConsultImageDTO(
    var imageName: String = EMPTY_STRING,
    var imageUrl: String = EMPTY_STRING
)

@IgnoreExtraProperties
data class ConsultAnswerDTO(
    var answerId: String = EMPTY_STRING,
    var pharmacistId: String = EMPTY_STRING,
    var pharmacistName: String = EMPTY_STRING,
    var content: String = EMPTY_STRING,

    @ServerTimestamp
    var createdAt: Timestamp? = null
)

// ConsultImage 매퍼
fun ConsultImageDTO.toDomain(): ConsultImage = ConsultImage(
    imageName = this.imageName,
    imageUrl = this.imageUrl
)
fun ConsultImage.toDto(): ConsultImageDTO = ConsultImageDTO(
    imageName = this.imageName,
    imageUrl = this.imageUrl
)

// ConsultAnswer 매퍼
fun ConsultAnswerDTO.toDomain(): ConsultAnswer = ConsultAnswer(
    answerId = this.answerId,
    pharmacistId = this.pharmacistId,
    pharmacistName = this.pharmacistName,
    content = this.content,
    createdAt = this.createdAt?.toDate()?.time ?: 0L
)
fun ConsultAnswer.toDto(): ConsultAnswerDTO = ConsultAnswerDTO(
    answerId = this.answerId,
    pharmacistId = this.pharmacistId,
    pharmacistName = this.pharmacistName,
    content = this.content,
    createdAt = this.createdAt.toTimestamp()
)

// ConsultItem 매퍼
fun ConsultItemDTO.toDomain(): ConsultItem = ConsultItem(
    id = this.id,
    userId = this.userId,
    nickName = this.nickName,
    pharmacistId = this.pharmacistId,
    title = this.title,
    content = this.content,
    status = ConsultStatus.from(this.status),
    isPublic = this.isPublic ?: false,
    images = this.images?.map { it.toDomain() } ?: emptyList(),
    createdAt = this.createdAt?.toDate()?.time ?: 0L,
    answer = this.answer?.toDomain()
)
fun ConsultItem.toDto(): ConsultItemDTO = ConsultItemDTO(
    id = this.id,
    userId = this.userId,
    nickName = this.nickName,
    pharmacistId = this.pharmacistId,
    title = this.title,
    content = this.content,
    status = this.status.name,
    isPublic = this.isPublic,
    images = this.images.map { it.toDto() },
    createdAt = this.createdAt.toTimestamp(),
    answer = this.answer?.toDto()
)

fun List<ConsultItemDTO>.toDomainList() = this.map { it.toDomain() }