package com.moon.pharm.data.datasource.remote.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType

@IgnoreExtraProperties
data class UserDTO(
    @DocumentId
    var id: String = EMPTY_STRING,

    val email: String = EMPTY_STRING,
    val nickName: String = EMPTY_STRING,
    val userType: String = EMPTY_STRING,
    val profileImageUrl: String? = null,

    @ServerTimestamp
    var createdAt: Timestamp? = null,

    @get:PropertyName("fcmToken")
    @set:PropertyName("fcmToken")
    var fcmToken: String? = null
)

// User 매퍼
fun UserDTO.toDomain(): User = User(
    id = this.id,
    email = this.email,
    nickName = this.nickName,
    userType = UserType.from(this.userType),
    profileImageUrl = this.profileImageUrl,
    createdAt = this.createdAt?.toDate()?.time ?: 0L,
    fcmToken = this.fcmToken
)

fun User.toDto(): UserDTO = UserDTO(
    id = this.id,
    email = this.email,
    nickName = this.nickName,
    userType = this.userType.name,
    profileImageUrl = this.profileImageUrl,
    createdAt = this.createdAt.toTimestamp(),
    fcmToken = this.fcmToken
)