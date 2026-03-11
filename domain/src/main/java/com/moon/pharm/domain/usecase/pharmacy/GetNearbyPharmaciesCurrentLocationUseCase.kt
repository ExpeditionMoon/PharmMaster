package com.moon.pharm.domain.usecase.pharmacy

import com.moon.pharm.domain.model.map.GeoLocation
import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.repository.LocationRepository
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetNearbyPharmaciesCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val pharmacyRepository: PharmacyRepository
) {
    data class Result(val location: GeoLocation, val pharmacies: List<Pharmacy>)

    operator fun invoke(): Flow<DataResourceResult<Result>> = flow {
        emit(DataResourceResult.Loading)

        val locationResult = locationRepository.getCurrentLocation()

        val myLocation = if (locationResult is DataResourceResult.Success) {
            val (lat, lng) = locationResult.resultData
            GeoLocation(lat, lng)
        } else {
            GeoLocation.DEFAULT
        }

        pharmacyRepository.searchNearbyPharmacies(
            myLocation.lat,
            myLocation.lng
        ).collectLatest { searchResult ->
            if (searchResult is DataResourceResult.Success) {
                emit(
                    DataResourceResult.Success(
                        Result(location = myLocation, pharmacies = searchResult.resultData)
                    )
                )
            } else if (searchResult is DataResourceResult.Failure) {
                emit(DataResourceResult.Failure(searchResult.exception))
            }
        }
    }.catch { e ->
        e.printStackTrace()
        emit(DataResourceResult.Failure(e))
    }
}