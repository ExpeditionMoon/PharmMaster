package com.moon.pharm.data.di

import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.datasource.PharmacistDataSource
import com.moon.pharm.data.datasource.PharmacyDataSource
import com.moon.pharm.data.datasource.UserDataSource
import com.moon.pharm.data.datasource.remote.firebase.FirebaseAuthDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestoreConsultDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestoreMedicationDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestorePharmacistDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestorePharmacyDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestoreUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthDataSource(
        impl: FirebaseAuthDataSourceImpl
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindUserDataSource(
        impl: FirestoreUserDataSourceImpl
    ): UserDataSource

    @Binds
    @Singleton
    abstract fun bindPharmacistDataSource(
        impl: FirestorePharmacistDataSourceImpl
    ): PharmacistDataSource

    @Binds
    @Singleton
    abstract fun bindConsultDataSource(
        impl: FirestoreConsultDataSourceImpl
    ): ConsultDataSource

    @Binds
    @Singleton
    abstract fun bindPharmacyDataSource(
        impl: FirestorePharmacyDataSourceImpl
    ): PharmacyDataSource

    @Binds
    @Singleton
    abstract fun bindMedicationDataSource(
        impl: FirestoreMedicationDataSourceImpl
    ): MedicationDataSource
}