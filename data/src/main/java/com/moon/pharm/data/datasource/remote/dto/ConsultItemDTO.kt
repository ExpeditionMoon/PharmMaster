package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.domain.model.consult.ConsultStatus

@IgnoreExtraProperties
data class ConsultItemDTO(
    @DocumentId
    var id: String = EMPTY_STRING,

    var userId: String = EMPTY_STRING,
    var pharmacistId: String? = null,
    var title: String = EMPTY_STRING,
    var content: String = EMPTY_STRING,
    var status: String = ConsultStatus.WAITING.name,
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