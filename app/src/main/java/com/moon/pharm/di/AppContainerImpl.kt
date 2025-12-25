package com.moon.pharm.di

import com.moon.pharm.data.datasource.ConsultDataSource
import com.moon.pharm.data.remote.firebase.FirestoreConsultDataSourceImpl
import com.moon.pharm.data.repository.FirestoreConsultRepositoryImpl
import com.moon.pharm.domain.repository.ConsultRepository
import com.moon.pharm.domain.usecase.consult.ConsultUseCases
import com.moon.pharm.domain.usecase.consult.CreateConsultUseCase
import com.moon.pharm.domain.usecase.consult.GetConsultDetailUseCase
import com.moon.pharm.domain.usecase.consult.GetConsultItemsUseCase
import com.moon.pharm.domain.usecase.consult.GetPharmacistUseCase

class AppContainerImpl : AppContainer {
    private val remoteDataSource: ConsultDataSource by lazy {
        FirestoreConsultDataSourceImpl()
    }

    private val consultRepository: ConsultRepository by lazy {
        FirestoreConsultRepositoryImpl(remoteDataSource)
    }

    override val consultUseCases: ConsultUseCases by lazy {
        ConsultUseCases(
            createConsultUseCase = CreateConsultUseCase(consultRepository),
            getConsultItemsUseCase = GetConsultItemsUseCase(consultRepository),
            getConsultDetailUseCase = GetConsultDetailUseCase(consultRepository),
            getPharmacistUseCase = GetPharmacistUseCase(consultRepository)
        )
    }
}