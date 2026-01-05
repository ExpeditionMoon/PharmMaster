package com.moon.pharm.data.mapper

import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.domain.model.User

fun UserDTO.toDomainUser(): User {
    return User(
        id = this.id,
        email = this.email,
        nickname = this.nickname,
        userType = this.userType,
        profileImageUrl = this.profileImageUrl
    )
}

fun User.toFirestoreUserDTO(): UserDTO {
    return UserDTO(
        id = this.id,
        email = this.email,
        nickname = this.nickname,
        userType = this.userType,
        profileImageUrl = this.profileImageUrl
    )
}