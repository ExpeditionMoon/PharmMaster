package com.moon.pharm.domain.usecase.pharmacy

import com.moon.pharm.domain.model.map.GeoLocation
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.result.DataResourceResult
import com.moon.pharm.domain.usecase.location.GetCurrentLocationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNearbyPharmaciesCurrentLocationUseCase @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val searchNearbyPharmaciesUseCase: SearchNearbyPharmaciesUseCase
) {
    data class Result(val location: GeoLocation, val pharmacies: List<Pharmacy>)

    operator fun invoke(): Flow<DataResourceResult<Result>> = flow {
        emit(DataResourceResult.Loading)

        val locationResult = getCurrentLocationUseCase()

        val myLocation = if (locationResult is DataResourceResult.Success) {
            val (lat, lng) = locationResult.resultData
            GeoLocation(lat, lng)
        } else {
            GeoLocation.DEFAULT
        }

        searchNearbyPharmaciesUseCase(myLocation.lat, myLocation.lng).collect { searchResult ->
            if (searchResult is DataResourceResult.Success) {
                emit(DataResourceResult.Success(
                    Result(
                        location = myLocation,
                        pharmacies = searchResult.resultData
                    )
                ))
            } else if (searchResult is DataResourceResult.Failure) {
                emit(DataResourceResult.Failure(searchResult.exception))
            }
        }
    }
}