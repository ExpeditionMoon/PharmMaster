package com.moon.pharm.data.datasource.remote.kakao

object KakaoApiConst {
    // Base URL
    const val BASE_URL = "https://dapi.kakao.com/"

    // Auth
    const val AUTH_HEADER = "Authorization"
    const val AUTH_PREFIX = "KakaoAK"

    // Endpoints
    const val PATH_SEARCH_CATEGORY = "v2/local/search/category.json"
    const val PATH_SEARCH_KEYWORD = "v2/local/search/keyword.json"

    // Parameters
    const val PARAM_CATEGORY_GROUP_CODE = "category_group_code"
    const val PARAM_X = "x"
    const val PARAM_Y = "y"
    const val PARAM_RADIUS = "radius"
    const val PARAM_SORT = "sort"
    const val PARAM_QUERY = "query"

    // Default Values
    const val VALUE_CATEGORY_CODE_PHARMACY = "PM9"
    const val VALUE_SORT_DISTANCE = "distance"
    const val DEFAULT_RADIUS = 2000
}