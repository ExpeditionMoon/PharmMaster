package com.moon.pharm.di

import com.moon.pharm.domain.usecase.consult.ConsultUseCases

interface AppContainer {
    val consultUseCases: ConsultUseCases
}