package com.moon.pharm.consult.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moon.pharm.consult.viewmodel.ConsultViewModel
import com.moon.pharm.domain.usecase.consult.ConsultUseCases

class ConsultViewModelFactory(
    private val consultUseCases: ConsultUseCases
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ConsultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsultViewModel(consultUseCases) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
