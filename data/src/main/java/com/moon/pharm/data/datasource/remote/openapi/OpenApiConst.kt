package com.moon.pharm.data.datasource.remote.openapi

import com.moon.pharm.data.BuildConfig

object OpenApiConst {
    const val BASE_URL = "http://apis.data.go.kr/"
    val PUBLIC_DATA_SERVICE_KEY = BuildConfig.PUBLIC_DATA_SERVICE_KEY

    const val RESULT_CODE_SUCCESS = "00"
    const val DEFAULT_ERROR_MSG = "알 수 없는 API 에러 발생"

    const val DEFAULT_ITEM_NAME = "제품명 정보 없음"
    const val DEFAULT_COMPANY_NAME = "제조사 정보 없음"
    const val DEFAULT_EFFICACY = "효능 정보가 제공되지 않습니다."
    const val DEFAULT_INTERACTION = "상호작용 정보가 없습니다."
}

internal fun String.removeHtmlTags(): String {
    return this.replace(Regex("<.*?>"), "").trim()
}