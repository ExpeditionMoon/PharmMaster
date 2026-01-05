package com.moon.pharm.data.di

import com.moon.pharm.data.repository.AuthRepositoryImpl
import com.moon.pharm.data.repository.ConsultRepositoryImpl
import com.moon.pharm.data.repository.MedicationRepositoryImpl
import com.moon.pharm.domain.repository.AuthRepository
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
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindConsultRepository(
        impl: ConsultRepositoryImpl
    ): ConsultRepository

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(
        impl: MedicationRepositoryImpl
    ): MedicationRepository
}