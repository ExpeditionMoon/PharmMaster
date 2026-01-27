package com.moon.pharm.data.datasource.remote.firebase

import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.moon.pharm.data.common.ERROR_MSG_UPLOAD_FAILED
import com.moon.pharm.data.common.STORAGE_CONSULT_IMAGES
import com.moon.pharm.data.datasource.ImageDataSource
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseImageDataSourceImpl @Inject constructor(
    private val storage: FirebaseStorage
) : ImageDataSource {

    override suspend fun uploadImage(uriString: String, userId: String): String = suspendCancellableCoroutine { continuation ->
        val fileName = "${System.currentTimeMillis()}_${uriString.substringAfterLast("/")}"
        val ref = storage.reference.child("${STORAGE_CONSULT_IMAGES}/$userId/$fileName")
        val uri = uriString.toUri()

        ref.putFile(uri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                ref.downloadUrl
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result.toString())
                } else {
                    continuation.resumeWithException(task.exception ?: Exception(ERROR_MSG_UPLOAD_FAILED))
                }
            }
    }
}