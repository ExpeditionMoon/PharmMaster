package com.moon.pharm.data.di

import com.moon.pharm.data.remote.firebase.FirestoreConsultRepositoryImpl
import com.moon.pharm.data.remote.firebase.FirestoreMedicationRepositoryImpl
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.MedicationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindConsultRepository(
        impl: FirestoreConsultRepositoryImpl
    ): ConsultRepository

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(
        impl: FirestoreMedicationRepositoryImpl
    ): MedicationRepository
}