package com.moon.pharm.profile.mypage.mapper

import com.moon.pharm.domain.model.auth.User
import com.moon.pharm.domain.model.auth.UserType
import com.moon.pharm.domain.model.consult.ConsultItem
import com.moon.pharm.domain.model.consult.ConsultStatus
import com.moon.pharm.profile.mypage.model.MyPageConsultStatusUiModel
import com.moon.pharm.profile.mypage.model.MyPageConsultUiModel
import com.moon.pharm.profile.mypage.model.MyPageUserUiModel

fun User.toMyPageUiModel(): MyPageUserUiModel {
    return MyPageUserUiModel(
        id = id,
        nickName = nickName,
        profileImageUrl = profileImageUrl,
        isPharmacist = userType == UserType.PHARMACIST
    )
}

fun ConsultItem.toMyPageUiModel(): MyPageConsultUiModel {
    return MyPageConsultUiModel(
        id = id,
        status = when (status) {
            ConsultStatus.WAITING -> MyPageConsultStatusUiModel.Waiting
            ConsultStatus.COMPLETED -> MyPageConsultStatusUiModel.Completed
        }
    )
}
