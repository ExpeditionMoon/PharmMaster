package com.moon.pharm.domain.usecase.consult

import com.moon.pharm.domain.repository.ConsultRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class UploadConsultImagesUseCase @Inject constructor(
    private val repository: ConsultRepository
) {
    suspend operator fun invoke(uris: List<String>, userId: String): List<String> = coroutineScope {
        val deferredUploads = uris.map { uri ->
            async {
                repository.uploadImage(uri, userId)
            }
        }
        deferredUploads.awaitAll()
    }
}