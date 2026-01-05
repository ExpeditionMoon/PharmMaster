package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserDTO(
    @DocumentId
    var id: String = "",

    val email: String = "",
    val nickname: String = "",
    val userType: String = "",
    val profileImageUrl: String? = null
)