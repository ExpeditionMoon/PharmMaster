package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.moon.pharm.data.common.EMPTY_STRING

@IgnoreExtraProperties
data class PharmacistDTO(
    @DocumentId
    var userId: String = EMPTY_STRING,

    var name: String = EMPTY_STRING,
    var bio: String? = null,
    var pharmacyId: String = EMPTY_STRING,
    var pharmacyName: String = EMPTY_STRING,
    var isApproved: Boolean = false
)