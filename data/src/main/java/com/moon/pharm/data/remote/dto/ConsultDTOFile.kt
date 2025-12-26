package com.moon.pharm.data.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import com.moon.pharm.domain.model.ConsultStatus

@IgnoreExtraProperties
data class ConsultItemDTO(
    @DocumentId
    var _id: String = "",

    var userId: String = "",
    var expertId: String? = null,
    var title: String = "",
    var content: String = "",
    var status: String = ConsultStatus.WAITING.name,
    var images: List<String>? = null,

    @ServerTimestamp
    var createdAt: Timestamp? = null,

    var answer: ConsultAnswerDTO? = null,
)

@IgnoreExtraProperties
data class ConsultAnswerDTO(
    var answerId: String = "",
    var expertId: String = "",
    var content: String = "",
    @ServerTimestamp
    var createdAt: Timestamp? = null
)

@IgnoreExtraProperties
data class PharmacistDTO(
    var _id: String = "",
    var name: String = "",
    var bio: String? = null,
    var pharmacyId: String = "",
    var pharmacyName: String = "",
    var isApproved: Boolean = false,
    var imageUrl: String? = null
)