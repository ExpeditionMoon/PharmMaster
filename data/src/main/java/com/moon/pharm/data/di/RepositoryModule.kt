package com.moon.pharm.data.di

import com.moon.pharm.data.repository.AuthRepositoryImpl
import com.moon.pharm.data.repository.ConsultRepositoryImpl
import com.moon.pharm.data.repository.MedicationRepositoryImpl
import com.moon.pharm.data.repository.PharmacistRepositoryImpl
import com.moon.pharm.data.repository.PharmacyRepositoryImpl
import com.moon.pharm.data.repository.UserRepositoryImpl
import com.moon.pharm.domain.repository.AuthRepository
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.repository.MedicationRepository
import com.moon.pharm.domain.repository.PharmacistRepository
import com.moon.pharm.domain.repository.PharmacyRepository
import com.moon.pharm.domain.repository.UserRepository
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
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindPharmacistRepository(
        impl: PharmacistRepositoryImpl
    ): PharmacistRepository

    @Binds
    @Singleton
    abstract fun bindConsultRepository(
        impl: ConsultRepositoryImpl
    ): ConsultRepository

    @Binds
    @Singleton
    abstract fun bindPharmacyRepository(
        pharmacyRepositoryImpl: PharmacyRepositoryImpl
    ): PharmacyRepository

    @Binds
    @Singleton
    abstract fun bindMedicationRepository(
        impl: MedicationRepositoryImpl
    ): MedicationRepository
}