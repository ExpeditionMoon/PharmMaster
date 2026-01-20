package com.moon.pharm.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.moon.pharm.domain.repository.LocationRepository
import com.moon.pharm.domain.result.DataResourceResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): DataResourceResult<Pair<Double, Double>> {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    continuation.resume(
                        DataResourceResult.Success(Pair(location.latitude, location.longitude))
                    )
                } else {
                    continuation.resume(
                        DataResourceResult.Failure(Exception("Location is null"))
                    )
                }
            }.addOnFailureListener { e ->
                continuation.resume(DataResourceResult.Failure(e))
            }
        }
    }
}