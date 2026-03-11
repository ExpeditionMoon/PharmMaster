package com.moon.pharm.data.datasource.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.moon.pharm.data.datasource.AuthDataSource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDataSourceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthDataSource {

    override fun getCurrentUserId(): String? = auth.currentUser?.uid
    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override suspend fun createAccount(email: String, password: String): String {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("User creation failed")
    }

    override suspend fun login(email: String, password: String): String {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        return result.user?.uid ?: throw Exception("Login failed")
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override suspend fun deleteAccount() {
        val user = auth.currentUser ?: throw Exception("No user logged in")
        user.delete().await()
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}