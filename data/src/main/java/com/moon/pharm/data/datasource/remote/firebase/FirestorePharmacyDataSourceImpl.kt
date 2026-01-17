package com.moon.pharm.data.datasource.remote.firebase

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.common.DEFAULT_LOCATION_COORDINATE
import com.moon.pharm.data.common.EMPTY_STRING
import com.moon.pharm.data.common.ERROR_MSG_PHARMACY_NOT_FOUND
import com.moon.pharm.data.common.PHARMACY_COLLECTION
import com.moon.pharm.data.common.PLACE_TYPE_PHARMACY
import com.moon.pharm.data.common.QUERY_PHARMACY_KOREAN
import com.moon.pharm.data.common.SEARCH_MAX_RESULT_COUNT
import com.moon.pharm.data.common.SEARCH_RADIUS_METERS
import com.moon.pharm.data.datasource.PharmacyDataSource
import com.moon.pharm.data.datasource.remote.dto.PharmacyDTO
import com.moon.pharm.domain.result.DataResourceResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestorePharmacyDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) : PharmacyDataSource {

    private val pharmacyCollection = firestore.collection(PHARMACY_COLLECTION)

    private val placesClient by lazy { Places.createClient(context) }
    
    override fun searchExternalPharmacies(query: String) = flow {
        emit(DataResourceResult.Loading)

        try {
            val token = AutocompleteSessionToken.newInstance()

            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(query)
                .setTypesFilter(listOf(PLACE_TYPE_PHARMACY))
                .build()

            val response = placesClient.findAutocompletePredictions(request).await()

            val dto = response.autocompletePredictions.map { prediction ->
                PharmacyDTO(
                    placeId = prediction.placeId,
                    name = prediction.getPrimaryText(null).toString(),
                    address = prediction.getSecondaryText(null).toString(),
                    lat = DEFAULT_LOCATION_COORDINATE,
                    lng = DEFAULT_LOCATION_COORDINATE
                )
            }

            emit(DataResourceResult.Success(dto))
        } catch (e: Exception) {
            emit(DataResourceResult.Failure(e))
        }
    }

    override fun fetchPharmacyDetail(placeId: String): Flow<DataResourceResult<PharmacyDTO>> = flow {
        emit(DataResourceResult.Loading)
        try {
            val placesClient = Places.createClient(context)

            val placeFields = listOf(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.FORMATTED_ADDRESS,
                Place.Field.LOCATION
            )
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)

            val response = placesClient.fetchPlace(request).await()
            val place = response.place

            val pharmacyDto = PharmacyDTO(
                placeId = place.id ?: EMPTY_STRING,
                name = place.displayName ?: EMPTY_STRING,
                address = place.formattedAddress ?: EMPTY_STRING,
                lat = place.location?.latitude ?: DEFAULT_LOCATION_COORDINATE,
                lng = place.location?.longitude ?: DEFAULT_LOCATION_COORDINATE
            )

            emit(DataResourceResult.Success(pharmacyDto))
        } catch (e: Exception) {
            emit(DataResourceResult.Failure(e))
        }
    }

    override fun searchNearbyPharmacies(lat: Double, lng: Double): Flow<DataResourceResult<List<PharmacyDTO>>> = flow {
        emit(DataResourceResult.Loading)
        try {
            val placesClient = Places.createClient(context)

            val placeFields = listOf(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.FORMATTED_ADDRESS,
                Place.Field.LOCATION
            )

            val circle = CircularBounds.newInstance(
                com.google.android.gms.maps.model.LatLng(lat, lng),
                SEARCH_RADIUS_METERS
            )

            val request = SearchByTextRequest.builder(QUERY_PHARMACY_KOREAN, placeFields)
                .setLocationBias(circle)
                .setMaxResultCount(SEARCH_MAX_RESULT_COUNT)
                .build()

            val response = placesClient.searchByText(request).await()

            val dto = response.places.map { place ->
                PharmacyDTO(
                    placeId = place.id ?: EMPTY_STRING,
                    name = place.displayName ?: EMPTY_STRING,
                    address = place.formattedAddress ?: EMPTY_STRING,
                    lat = place.location?.latitude ?: DEFAULT_LOCATION_COORDINATE,
                    lng = place.location?.longitude ?: DEFAULT_LOCATION_COORDINATE
                )
            }

            emit(DataResourceResult.Success(dto))

        } catch (e: Exception) {
            emit(DataResourceResult.Failure(e))
        }
    }

    override fun getPharmacyFromFirestore(placeId: String): Flow<DataResourceResult<PharmacyDTO>> = callbackFlow {
        trySend(DataResourceResult.Loading)

        val docRef = pharmacyCollection.document(placeId)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(DataResourceResult.Failure(error))
                return@addSnapshotListener
            }

            val dto = snapshot?.toObject(PharmacyDTO::class.java)
            if (dto != null) {
                trySend(DataResourceResult.Success(dto))
            } else {
                trySend(DataResourceResult.Failure(Exception(ERROR_MSG_PHARMACY_NOT_FOUND)))
            }
        }
        awaitClose { registration.remove() }
    }

    override fun savePharmacyToFirestore(pharmacyDTO: PharmacyDTO): Flow<DataResourceResult<Unit>> = flow {
        emit(DataResourceResult.Loading)
        try {
            pharmacyCollection
                .document(pharmacyDTO.placeId)
                .set(pharmacyDTO)
                .await()
            emit(DataResourceResult.Success(Unit))
        } catch (e: Exception) {
            emit(DataResourceResult.Failure(e))
        }
    }
}