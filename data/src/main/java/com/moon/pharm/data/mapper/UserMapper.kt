package com.moon.pharm.data.mapper

import com.moon.pharm.data.common.toTimestamp
import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType

fun UserDTO.toDomain(): User = User(
    id = id,
    email = email,
    nickName = nickName,
    userType = UserType.from(userType),
    profileImageUrl = profileImageUrl,
    createdAt = createdAt?.toDate()?.time ?: 0L,
    fcmToken = fcmToken
)

fun User.toDto(): UserDTO = UserDTO(
    id = id,
    email = email,
    nickName = nickName,
    userType = userType.name,
    profileImageUrl = profileImageUrl,
    createdAt = createdAt.toTimestamp(),
    fcmToken = fcmToken
)
