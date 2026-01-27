package com.moon.pharm.profile.auth.mapper

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.profile.auth.screen.SignUpUiState

object SignUpUiMapper {

    fun toUser(state: SignUpUiState): User {
        val type = state.userType ?: UserType.GENERAL

        return User(
            id = "",
            email = state.email,
            nickName = state.nickName,
            userType = type,
            profileImageUrl = state.profileImageUri,
            createdAt = System.currentTimeMillis()
        )
    }

    fun toPharmacist(state: SignUpUiState): Pharmacist? {
        if (state.userType != UserType.PHARMACIST || state.selectedPharmacy == null) {
            return null
        }

        return Pharmacist(
            userId = "",
            name = state.nickName,
            bio = state.pharmacistBio,
            placeId = state.selectedPharmacy.placeId,
            pharmacyName = state.selectedPharmacy.name,
            isApproved = false
        )
    }
}