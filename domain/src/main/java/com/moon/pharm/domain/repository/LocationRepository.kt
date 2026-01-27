package com.moon.pharm.domain.repository

import com.moon.pharm.domain.result.DataResourceResult

interface LocationRepository {
    suspend fun getCurrentLocation(): DataResourceResult<Pair<Double, Double>>
}