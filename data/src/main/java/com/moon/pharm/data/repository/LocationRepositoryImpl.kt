package com.moon.pharm.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.moon.pharm.domain.model.map.LocationException
import com.moon.pharm.domain.repository.LocationRepository
import com.moon.pharm.domain.result.DataResourceResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private fun Throwable.toLocationException(): LocationException = when {
        this is SecurityException -> LocationException.PermissionDenied()
        this.message == "Location is null" -> LocationException.NotFound()
        else -> LocationException.Unknown(this.message)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): DataResourceResult<Pair<Double, Double>> {
        return runCatching {
            suspendCancellableCoroutine { continuation ->
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(Pair(location.latitude, location.longitude))
                    } else {
                        continuation.resumeWithException(Exception("Location is null"))
                    }
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
            }
        }.fold(
            onSuccess = { DataResourceResult.Success(it) },
            onFailure = { e -> DataResourceResult.Failure(e.toLocationException()) }
        )
    }
}