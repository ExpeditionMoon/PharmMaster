package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.moon.pharm.data.common.DOCUMENT_LIFESTYLE
import com.moon.pharm.data.common.FIELD_FCM_TOKEN
import com.moon.pharm.data.common.FIELD_USER_EMAIL
import com.moon.pharm.data.common.SETTING_COLLECTION
import com.moon.pharm.data.common.USER_COLLECTION
import com.moon.pharm.data.datasource.UserDataSource
import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserDataSource {

    private val userCollection = firestore.collection(USER_COLLECTION)

    override suspend fun saveUser(userDto: UserDTO): DataResourceResult<Unit> {
        return try {
            userCollection.document(userDto.id).set(userDto).await()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }

    override suspend fun saveUserLifeStyle(userId: String, lifeStyleDto: UserLifeStyleDTO): DataResourceResult<Unit> {
        return try {
            userCollection.document(userId)
                .collection(SETTING_COLLECTION)
                .document(DOCUMENT_LIFESTYLE)
                .set(lifeStyleDto)
                .await()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }

    override suspend fun isEmailDuplicated(email: String): Boolean {
        val query = userCollection.whereEqualTo(FIELD_USER_EMAIL, email).get().await()
        return !query.isEmpty
    }

    override fun getUserById(userId: String): Flow<DataResourceResult<UserDTO>> = callbackFlow {
        val docRef = userCollection.document(userId)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(DataResourceResult.Failure(error))
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val dto = snapshot.toObject(UserDTO::class.java)
                if (dto != null) trySend(DataResourceResult.Success(dto))
                else trySend(DataResourceResult.Failure(Exception("Mapping failed")))
            } else {
                trySend(DataResourceResult.Failure(Exception("User not found")))
            }
        }
        awaitClose { registration.remove() }
    }

    override suspend fun getFcmToken(): String {
        return FirebaseMessaging.getInstance().token.await()
    }

    override suspend fun updateFcmToken(userId: String, token: String): DataResourceResult<Unit> {
        return try {
            userCollection.document(userId)
                .update(FIELD_FCM_TOKEN, token)
                .await()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }
}