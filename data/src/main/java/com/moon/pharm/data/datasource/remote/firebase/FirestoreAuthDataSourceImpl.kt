package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.domain.result.DataResourceResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthDataSource {

    override fun getCurrentUserId(): String? = auth.currentUser?.uid
    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override suspend fun createAccount(email: String, password: String): DataResourceResult<String> {
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
}