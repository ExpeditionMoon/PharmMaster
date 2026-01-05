package com.moon.pharm.data.di

import com.moon.pharm.data.datasource.AuthDataSource
import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.datasource.MedicationDataSource
import com.moon.pharm.data.datasource.remote.firebase.FirestoreAuthDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestoreConsultDataSourceImpl
import com.moon.pharm.data.datasource.remote.firebase.FirestoreMedicationDataSourceImpl
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
        impl: FirestoreAuthDataSourceImpl
    ): AuthDataSource

    @Binds
    @Singleton
    abstract fun bindConsultDataSource(
        impl: FirestoreConsultDataSourceImpl
    ): ConsultDataSource

    @Binds
    @Singleton
    abstract fun bindMedicationDataSource(
        impl: FirestoreMedicationDataSourceImpl
    ): MedicationDataSource
}