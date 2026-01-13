package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import com.moon.pharm.data.common.EMPTY_STRING

@IgnoreExtraProperties
data class UserDTO(
    @DocumentId
    var id: String = EMPTY_STRING,

    val email: String = EMPTY_STRING,
    val nickName: String = EMPTY_STRING,
    val userType: String = EMPTY_STRING,
    val profileImageUrl: String? = null,

    @ServerTimestamp
    var createdAt: Timestamp? = null
)