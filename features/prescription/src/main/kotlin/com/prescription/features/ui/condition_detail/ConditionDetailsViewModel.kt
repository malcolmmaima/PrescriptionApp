package com.prescription.features.ui.condition_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prescription.database.entities.DrugEntity
import com.prescription.database.entities.ProblemEntity
import com.prescription.database.repository.PrescriptionRepository
import com.prescription.features.ui.home.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConditionDetailsViewModel @Inject constructor(
    private val repository: PrescriptionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Pair<ProblemEntity, List<DrugEntity>>>>(UiState.Loading)
    val uiState: StateFlow<UiState<Pair<ProblemEntity, List<DrugEntity>>>> = _uiState

    fun fetchProblemDetails(problemId: String) {
        viewModelScope.launch {
            repository.getProblemById(problemId).collect { problem ->
                if (problem != null) {
                    repository.getDrugsByProblemId(problemId.toInt()).collect { drugs ->
                        _uiState.value = UiState.Success(problem to drugs)
                    }
                } else {
                    _uiState.value = UiState.Error("Problem not found")
                }
            }
        }
    }
}

