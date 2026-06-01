package com.moon.pharm.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.moon.pharm.data.di.IoDispatcher
import com.moon.pharm.domain.model.map.LocationException
import com.moon.pharm.domain.repository.LocationRepository
import com.moon.pharm.domain.result.DataResourceResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private fun Throwable.toLocationException(): LocationException = when {
        this is LocationException -> this
        this is SecurityException -> LocationException.PermissionDenied()
        message == "Location is null" -> LocationException.NotFound()
        else -> LocationException.Unknown(message)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): DataResourceResult<Pair<Double, Double>> =
        runDataResourceOperation(
            dispatcher = ioDispatcher,
            errorMapper = { it.toLocationException() }
        ) {
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
        }
}
