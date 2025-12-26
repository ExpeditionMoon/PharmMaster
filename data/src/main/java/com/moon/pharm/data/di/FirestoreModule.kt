package com.moon.pharm.data.di

import com.google.firebase.firestore.FirebaseFirestore
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.remote.firebase.FirestoreConsultDataSourceImpl
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
    fun provideFirestoreConsultDataSource(
        firestore: FirebaseFirestore
    ): ConsultDataSource =
        FirestoreConsultDataSourceImpl(firestore)
}