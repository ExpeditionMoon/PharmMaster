package com.moon.pharm.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.moon.pharm.domain.model.map.LocationException
import com.moon.pharm.domain.repository.LocationRepository
import com.moon.pharm.domain.result.DataResourceResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private fun Throwable.toLocationException(): LocationException = when (this) {
        is SecurityException -> LocationException.PermissionDenied()
        else -> when (message) {
            "Location is null" -> LocationException.NotFound()
            else -> LocationException.Unknown(message)
        }
    }

    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    override suspend fun getCurrentLocation(): DataResourceResult<Pair<Double, Double>> {
        if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            return getLastKnownLocation(fusedLocationClient.lastLocation)
        }

        if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return DataResourceResult.Failure(LocationException.PermissionDenied())
        }

        return getLastKnownLocation(fusedLocationClient.lastLocation)
    }

    private suspend fun getLastKnownLocation(locationTask: Task<Location>): DataResourceResult<Pair<Double, Double>> {
        return runCatching {
            withTimeout(10000L) {
                locationTask.await().toCoordinates()
            }
        }.fold(
            onSuccess = { DataResourceResult.Success(it) },
            onFailure = { e -> DataResourceResult.Failure(e.toLocationException()) }
        )
    }

    private fun Location?.toCoordinates(): Pair<Double, Double> =
        this?.let { Pair(latitude, longitude) } ?: throw IllegalStateException("Location is null")
}
