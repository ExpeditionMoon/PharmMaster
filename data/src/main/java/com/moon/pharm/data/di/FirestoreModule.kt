package com.moon.pharm.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.datasource.remote.firebase.FirestoreAuthDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestoreConsultDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestoreMedicationDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthDataSource =
        FirestoreAuthDataSourceImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideFirestoreConsultDataSource(
        firestore: FirebaseFirestore
    ): ConsultDataSource =
        FirestoreConsultDataSourceImpl(firestore)

    @Provides
    @Singleton
    fun provideFirestoreMedicationDataSource(
        firestore: FirebaseFirestore
    ): MedicationDataSource =
        FirestoreMedicationDataSourceImpl(firestore)

}