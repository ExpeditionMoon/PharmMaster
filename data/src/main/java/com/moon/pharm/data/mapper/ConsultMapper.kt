package com.moon.pharm.data.mapper

import com.google.firebase.Timestamp
import com.moon.pharm.data.remote.dto.ConsultAnswerDTO
import com.moon.pharm.data.remote.dto.ConsultItemDTO
import com.moon.pharm.data.remote.dto.PharmacistDTO
import com.moon.pharm.domain.model.ConsultAnswer
import com.moon.pharm.domain.model.ConsultImage
import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.ConsultStatus
import com.moon.pharm.domain.model.Pharmacist
import kotlin.collections.map

fun Timestamp?.toKstString(): String {
    if (this == null) return ""
    val date = this.toDate()
    val formatter = java.text.SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss",
        java.util.Locale.KOREA
    )
    formatter.timeZone = java.util.TimeZone.getTimeZone("Asia/Seoul")
    return formatter.format(date)
}

fun ConsultItemDTO.toDomainConsult(): ConsultItem {
    return ConsultItem(
        id = this.id,
        userId = this.userId,
        expertId = this.expertId,
        title = this.title,
        content = this.content,
        status = try {
            ConsultStatus.valueOf(this.status)
        } catch (e: Exception) {
            ConsultStatus.WAITING
        },
        images = this.images?.map { ConsultImage(it) } ?: emptyList(),
        createdAt = this.createdAt.toKstString(),
        answer = this.answer?.toDomainAnswer()
    )
}

fun ConsultItem.toFirestoreConsultDTO(): ConsultItemDTO {
    return ConsultItemDTO(
        userId = this.userId,
        expertId = this.expertId,
        title = this.title,
        content = this.content,
        status = this.status.name,
        images = this.images.map { it.imageName },
        createdAt = null,
        answer = this.answer?.let {
            ConsultAnswerDTO(
                answerId = it.answerId,
                expertId = it.expertId,
                content = it.content,
                createdAt = null
            )
        }
    )
}

fun List<ConsultItemDTO>.toDomainConsultList() =
    this.map { it.toDomainConsult() }.toList()

fun ConsultAnswerDTO.toDomainAnswer(): ConsultAnswer {
    return ConsultAnswer(
        answerId = this.answerId,
        expertId = this.expertId,
        content = this.content,
        createdAt = this.createdAt.toKstString()
    )
}

fun PharmacistDTO.toDomainPharmacist(): Pharmacist {
    return Pharmacist(
        id = this.id,
        name = this.name,
        bio = this.bio ?: "",
        pharmacyId = this.pharmacyId,
        pharmacyName = this.pharmacyName,
        isApproved = this.isApproved,
        imageUrl = this.imageUrl ?: ""
    )
}
