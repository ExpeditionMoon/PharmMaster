package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.common.FIELD_USER_EMAIL
import com.moon.pharm.data.common.USER_COLLECTION
import com.moon.pharm.data.common.generateInternalPassword
import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.mapper.toFirestoreUserDTO
import com.moon.pharm.domain.model.AuthError
import com.moon.pharm.domain.model.auth.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreAuthDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthDataSource {

    private val collection = firestore.collection(USER_COLLECTION)

    override suspend fun createUser(user: User) {
        val docRef = if (user.id.isNotEmpty()) collection.document(user.id)
        else collection.document()

        val dataToSave = user.toFirestoreUserDTO()
        docRef.set(dataToSave).await()
    }

    override suspend fun isEmailDuplicated(email: String): Boolean {
        val query = collection.whereEqualTo(FIELD_USER_EMAIL, email).get().await()
        return !query.isEmpty
    }

    override suspend fun signUp(email: String): String {
        return try {
            val internalPassword = generateInternalPassword(email)
            val result = auth.createUserWithEmailAndPassword(email, internalPassword).await()
            result.user?.uid ?: throw AuthError.SignUpFailed()
        } catch (e: Exception) {
            throw AuthError.SignUpFailed()
        }
    }
}