package com.moon.pharm.profile.auth.mapper

import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.profile.auth.model.SignUpPharmacyUiModel
import com.moon.pharm.profile.auth.model.UserTypeUiModel
import com.moon.pharm.profile.auth.screen.SignUpUiState

object SignUpUiMapper {

    fun toUser(state: SignUpUiState): User {
        return User(
            id = "",
            email = state.email,
            nickName = state.nickName,
            userType = state.userType.toDomainModel(),
            profileImageUrl = state.profileImageUri,
            createdAt = System.currentTimeMillis()
        )
    }

    fun toPharmacist(state: SignUpUiState): Pharmacist? {
        val selectedPharmacy = state.selectedPharmacy
        if (state.userType != UserTypeUiModel.Pharmacist || selectedPharmacy == null) {
            return null
        }

        return Pharmacist(
            userId = "",
            name = state.nickName,
            bio = state.pharmacistBio,
            placeId = selectedPharmacy.placeId,
            pharmacyName = selectedPharmacy.name,
            isApproved = false
        )
    }

    fun toSelectedPharmacy(state: SignUpUiState): Pharmacy? {
        return state.selectedPharmacy?.toDomainModel()
    }
}

fun Pharmacy.toSignUpUiModel(): SignUpPharmacyUiModel {
    return SignUpPharmacyUiModel(
        id = id,
        placeId = placeId,
        name = name,
        address = address,
        tel = tel,
        latitude = latitude,
        longitude = longitude
    )
}

fun SignUpPharmacyUiModel.toDomainModel(): Pharmacy {
    return Pharmacy(
        id = id,
        placeId = placeId,
        name = name,
        address = address,
        tel = tel,
        latitude = latitude,
        longitude = longitude
    )
}

fun UserTypeUiModel?.toDomainModel(): UserType {
    return when (this) {
        UserTypeUiModel.Pharmacist -> UserType.PHARMACIST
        UserTypeUiModel.General,
        null -> UserType.GENERAL
    }
}
