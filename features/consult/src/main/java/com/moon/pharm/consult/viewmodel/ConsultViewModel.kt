package com.moon.pharm.consult.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moon.pharm.consult.model.ConsultUiModel
import com.moon.pharm.consult.model.ConsultPrimaryTab
import com.moon.pharm.domain.model.Pharmacist
import com.moon.pharm.domain.repository.ConsultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConsultViewModel(
    private val repository: ConsultRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConsultUiModel())
    private val _pharmacists = MutableStateFlow<List<Pharmacist>>(emptyList())
    val uiState = _uiState.asStateFlow()
    val pharmacists: StateFlow<List<Pharmacist>> = _pharmacists.asStateFlow()

    init {
        fetchConsults()
    }

    private fun fetchConsults() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getConsultItems().collect { items ->
                _uiState.update { it.copy(items = items, isLoading = false) }
            }
        }
    }

    fun onTabSelected(tab: ConsultPrimaryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
    }

    suspend fun consultPost(
        expertId: String, title: String, content: String, images: List<String>
    ): Result<Unit> {
        return try {
            val result = repository.consultPost(
                expertId = expertId, title = title, content = content, images = images
            )
            if (result.isSuccess) {
                fetchConsults()
            }
            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getConsultDetail(id: String) {
        viewModelScope.launch {
            repository.getConsultDetail(id).collect { item ->
                _uiState.update { it.copy(selectedItem = item) }

                item.answer?.let { answer ->
                    val pharmacist = repository.getPharmacistById(answer.expertId)
                    _uiState.update { it.copy(pharmacist = pharmacist) }
                }
            }
        }
    }

    fun selectPharmacy(pharmacyId: String) {
        viewModelScope.launch {
            val result = repository.getPharmacistsByPharmacy(pharmacyId)
            _pharmacists.value = result
        }
    }
}