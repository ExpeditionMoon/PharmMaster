package com.moon.pharm.domain.usecase.pharmacy

import com.moon.pharm.domain.model.pharmacy.Pharmacy
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.flow.Flow

class SearchNearbyPharmaciesUseCase(
    private val pharmacyRepository: PharmacyRepository
) {
    operator fun invoke(lat: Double, lng: Double): Flow<DataResourceResult<List<Pharmacy>>> {
        return pharmacyRepository.searchNearbyPharmacies(lat, lng)
    }
}
