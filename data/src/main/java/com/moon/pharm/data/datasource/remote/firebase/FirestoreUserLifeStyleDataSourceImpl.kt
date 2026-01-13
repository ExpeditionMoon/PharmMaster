package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.moon.pharm.data.common.DOCUMENT_LIFESTYLE
import com.moon.pharm.data.common.SETTING_COLLECTION
import com.moon.pharm.data.common.USER_COLLECTION
import com.moon.pharm.data.datasource.UserLifeStyleDataSource
import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserLifeStyleDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserLifeStyleDataSource {

    private val userCollection = firestore.collection(USER_COLLECTION)

    private fun getLifeStyleDocument(userId: String) =
        userCollection
            .document(userId)
            .collection(SETTING_COLLECTION)
            .document(DOCUMENT_LIFESTYLE)

    override fun getUserLifeStyle(userId: String): Flow<UserLifeStyleDTO?> {
        return getLifeStyleDocument(userId)
            .snapshots()
            .map { snapshot ->
                if (snapshot.exists()) {
                    snapshot.toObject(UserLifeStyleDTO::class.java)
                } else {
                    null
                }
            }
    }

    override suspend fun saveUserLifeStyle(userId: String, dto: UserLifeStyleDTO) {
        getLifeStyleDocument(userId)
            .set(dto)
            .await()
    }
}