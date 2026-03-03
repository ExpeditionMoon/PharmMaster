package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.domain.model.auth.Pharmacist

@IgnoreExtraProperties
data class PharmacistDTO(
    @DocumentId
    var userId: String = EMPTY_STRING,

    var name: String = EMPTY_STRING,
    var bio: String? = null,
    var placeId: String = EMPTY_STRING,
    var pharmacyName: String = EMPTY_STRING,
    var isApproved: Boolean = false
)

// Pharmacist 매퍼
fun PharmacistDTO.toDomain(): Pharmacist = Pharmacist(
    userId = this.userId,
    name = this.name,
    bio = this.bio ?: "",
    placeId = this.placeId,
    pharmacyName = this.pharmacyName,
    isApproved = this.isApproved
)

fun Pharmacist.toDto(): PharmacistDTO = PharmacistDTO(
    userId = this.userId,
    name = this.name,
    bio = this.bio,
    placeId = this.placeId,
    pharmacyName = this.pharmacyName,
    isApproved = this.isApproved
)