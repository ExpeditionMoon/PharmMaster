package com.moon.pharm.data.repository

import com.moon.pharm.data.source.ConsultDummyData
import com.moon.pharm.domain.model.ConsultImage
import com.moon.pharm.domain.model.ConsultItem
import com.moon.pharm.domain.model.ConsultStatus
import com.moon.pharm.domain.model.Pharmacist
import com.moon.pharm.domain.repository.ConsultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID

class ConsultRepositoryImpl : ConsultRepository {
    private val _consultItems = MutableStateFlow(ConsultDummyData.dummyConsultItems)

    override fun getConsultItems(): Flow<List<ConsultItem>> = _consultItems.asStateFlow()

    override fun getConsultDetail(id: String): Flow<ConsultItem> =
        _consultItems.map { items -> items.first { it.id == id } }

    override suspend fun consultPost(expertId: String, title: String, content: String, images: List<String>): Result<Unit> {
        return try {
            val newItem = ConsultItem(
                id = UUID.randomUUID().toString(),
                userId = "me_user",
                expertId = expertId,
                title = title,
                content = content,
                status = ConsultStatus.WAITING,
                createdAt = "2025-06-21",
                images = images.map { ConsultImage(it) },
                answer = null
            )
            _consultItems.update { listOf(newItem) + it }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPharmacistsByPharmacy(pharmacyId: String): List<Pharmacist> {
        return ConsultDummyData.dummyPharmacists.filter { it.pharmacyId == pharmacyId }
    }

    override suspend fun getPharmacistById(id: String): Pharmacist? {
        return ConsultDummyData.dummyPharmacists.find { it.id == id }
    }
}
