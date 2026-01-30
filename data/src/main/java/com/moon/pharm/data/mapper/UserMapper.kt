package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType

fun UserDTO.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        nickName = this.nickName,
        userType = try {
            UserType.valueOf(this.userType)
        } catch (e: Exception) {
            UserType.GENERAL
        },
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt?.toDate()?.time ?: 0L,
        fcmToken = this.fcmToken
    )
}

fun User.toDto(): UserDTO {
    return UserDTO(
        id = this.id,
        email = this.email,
        nickName = this.nickName,
        userType = this.userType.name,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt.toTimestamp(),
        fcmToken = this.fcmToken
    )
}