package com.moon.pharm.domain.usecase.pharmacy

import com.moon.pharm.domain.repository.PharmacyRepository
import javax.inject.Inject

class SearchNearbyPharmaciesUseCase @Inject constructor(
    private val repository: PharmacyRepository
) {
    operator fun invoke(lat: Double, lng: Double) = repository.searchNearbyPharmacies(lat, lng)
}