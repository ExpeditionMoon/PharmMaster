package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.domain.model.auth.User

fun UserDTO.toDomainUser(): User {
    return User(
        id = this.id,
        email = this.email,
        nickName = this.nickName,
        userType = this.userType,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt?.toDate()?.time ?: 0L
    )
}

fun User.toFirestoreUserDTO(): UserDTO {
    return UserDTO(
        id = this.id,
        email = this.email,
        nickName = this.nickName,
        userType = this.userType,
        profileImageUrl = this.profileImageUrl,
        createdAt = this.createdAt.toTimestamp(),
    )
}