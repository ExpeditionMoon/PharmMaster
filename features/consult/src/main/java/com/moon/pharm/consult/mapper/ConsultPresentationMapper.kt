package com.moon.pharm.consult.mapper

import com.moon.pharm.consult.model.ConsultAnswerUiModel
import com.moon.pharm.consult.model.ConsultImageUiModel
import com.moon.pharm.consult.model.ConsultStatusUiModel
import com.moon.pharm.consult.model.ConsultUiModel
import com.moon.pharm.consult.model.PharmacistUiModel
import com.moon.pharm.consult.model.PharmacyUiModel
import com.moon.pharm.domain.model.auth.Pharmacist
import com.moon.pharm.domain.model.consult.ConsultAnswer
import com.moon.pharm.domain.model.consult.ConsultImage
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.domain.model.pharmacy.Pharmacy

fun ConsultItem.toUiModel(): ConsultUiModel {
    return ConsultUiModel(
        id = id,
        userId = userId,
        nickName = nickName,
        pharmacistId = pharmacistId,
        title = title,
        content = content,
        status = status.toUiModel(),
        isPublic = isPublic,
        createdAt = createdAt,
        images = images.map { it.toUiModel() },
        answer = answer?.toUiModel()
    )
}

fun ConsultUiModel.toDomainModel(): ConsultItem {
    return ConsultItem(
        id = id,
        userId = userId,
        nickName = nickName,
        pharmacistId = pharmacistId,
        title = title,
        content = content,
        status = status.toDomainModel(),
        isPublic = isPublic,
        createdAt = createdAt,
        images = images.map { it.toDomainModel() },
        answer = answer?.toDomainModel()
    )
}

fun Pharmacy.toUiModel(): PharmacyUiModel {
    return PharmacyUiModel(
        id = id,
        placeId = placeId,
        name = name,
        address = address,
        tel = tel,
        latitude = latitude,
        longitude = longitude
    )
}

fun PharmacyUiModel.toDomainModel(): Pharmacy {
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

fun Pharmacist.toUiModel(): PharmacistUiModel {
    return PharmacistUiModel(
        userId = userId,
        name = name,
        bio = bio,
        placeId = placeId,
        pharmacyName = pharmacyName
    )
}

fun PharmacistUiModel.toDomainModel(): Pharmacist {
    return Pharmacist(
        userId = userId,
        name = name,
        bio = bio,
        placeId = placeId,
        pharmacyName = pharmacyName
    )
}

fun ConsultStatus.toUiModel(): ConsultStatusUiModel {
    return when (this) {
        ConsultStatus.WAITING -> ConsultStatusUiModel.Waiting
        ConsultStatus.COMPLETED -> ConsultStatusUiModel.Completed
    }
}

fun ConsultStatusUiModel.toDomainModel(): ConsultStatus {
    return when (this) {
        ConsultStatusUiModel.Waiting -> ConsultStatus.WAITING
        ConsultStatusUiModel.Completed -> ConsultStatus.COMPLETED
    }
}

private fun ConsultImage.toUiModel(): ConsultImageUiModel {
    return ConsultImageUiModel(
        imageName = imageName,
        imageUrl = imageUrl
    )
}

private fun ConsultImageUiModel.toDomainModel(): ConsultImage {
    return ConsultImage(
        imageName = imageName,
        imageUrl = imageUrl
    )
}

private fun ConsultAnswer.toUiModel(): ConsultAnswerUiModel {
    return ConsultAnswerUiModel(
        answerId = answerId,
        pharmacistId = pharmacistId,
        pharmacistName = pharmacistName,
        content = content,
        createdAt = createdAt
    )
}

private fun ConsultAnswerUiModel.toDomainModel(): ConsultAnswer {
    return ConsultAnswer(
        answerId = answerId,
        pharmacistId = pharmacistId,
        pharmacistName = pharmacistName,
        content = content,
        createdAt = createdAt
    )
}
