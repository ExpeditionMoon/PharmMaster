package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.common.DOCUMENT_LIFESTYLE
import com.moon.pharm.data.common.FIELD_PLACE_ID
import com.moon.pharm.data.common.FIELD_USER_EMAIL
import com.moon.pharm.data.common.FIELD_USER_TYPE
import com.moon.pharm.data.common.PHARMACIST_COLLECTION
import com.moon.pharm.data.common.SETTING_COLLECTION
import com.moon.pharm.data.common.USER_COLLECTION
import com.moon.pharm.data.common.USER_TYPE_PHARMACIST
import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.datasource.remote.dto.PharmacistDTO
import com.moon.pharm.data.datasource.remote.dto.UserDTO
import com.moon.pharm.data.datasource.remote.dto.UserLifeStyleDTO
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreAuthDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthDataSource {

    private val userCollection = firestore.collection(USER_COLLECTION)
    private val pharmacistCollection = firestore.collection(PHARMACIST_COLLECTION)


    override fun getCurrentUserId(): String? = auth.currentUser?.uid
    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override suspend fun signUp(email: String, password: String): DataResourceResult<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("User creation failed")
            DataResourceResult.Success(uid)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }

    override suspend fun login(email: String, password: String): DataResourceResult<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Login failed")
            DataResourceResult.Success(uid)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }

    override suspend fun logout(): DataResourceResult<Unit> {
        return try {
            auth.signOut()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }

    override suspend fun deleteAccount(): DataResourceResult<Unit> {
        return try {
            val user = auth.currentUser ?: throw Exception("No user logged in")
            user.delete().await()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }


    override suspend fun sendPasswordResetEmail(email: String): DataResourceResult<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
    }

    override suspend fun isEmailDuplicated(email: String): Boolean {
        val query = userCollection.whereEqualTo(FIELD_USER_EMAIL, email).get().await()
        return !query.isEmpty
    }

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

    override suspend fun savePharmacist(pharmacistDto: PharmacistDTO): DataResourceResult<Unit> {
        return try {
            pharmacistCollection.document(pharmacistDto.userId).set(pharmacistDto).await()
            DataResourceResult.Success(Unit)
        } catch (e: Exception) {
            DataResourceResult.Failure(e)
        }
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
                if (dto != null) {
                    trySend(DataResourceResult.Success(dto))
                } else {
                    trySend(DataResourceResult.Failure(Exception("User data mapping failed")))
                }
            } else {
                trySend(DataResourceResult.Failure(Exception("User not found")))
            }
        }
        awaitClose { registration.remove() }
    }

    override fun getPharmacistsByPlaceId(placeId: String): Flow<DataResourceResult<List<PharmacistDTO>>> = callbackFlow {
        trySend(DataResourceResult.Loading)

        val query = userCollection
            .whereEqualTo(FIELD_USER_TYPE, USER_TYPE_PHARMACIST)
            .whereEqualTo(FIELD_PLACE_ID, placeId)

        val registration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(DataResourceResult.Failure(error))
                return@addSnapshotListener
            }

            val dtos = snapshot?.toObjects(PharmacistDTO::class.java) ?: emptyList()
            trySend(DataResourceResult.Success(dtos))
        }

        awaitClose { registration.remove() }
    }

    override fun getPharmacistById(pharmacistId: String): Flow<DataResourceResult<PharmacistDTO>> = callbackFlow {
        trySend(DataResourceResult.Loading)

        val docRef = firestore.collection(USER_COLLECTION).document(pharmacistId)

        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(DataResourceResult.Failure(error))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val dto = snapshot.toObject(PharmacistDTO::class.java)
                if (dto != null) {
                    trySend(DataResourceResult.Success(dto))
                } else {
                    trySend(DataResourceResult.Failure(Exception("Data mapping failed")))
                }
            } else {
                trySend(DataResourceResult.Failure(Exception("Pharmacist not found")))
            }
        }
        awaitClose { registration.remove() }
    }
}