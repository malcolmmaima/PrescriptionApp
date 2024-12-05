package com.prescription.features.ui.home

import ApiResponse
import Drug
import Problem
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.prescription.database.entities.*
import com.prescription.database.repository.PrescriptionRepository
import com.prescription.database.repository.UserCredentialRepository
import com.prescription.features.networking.repository.MedicineRepository
import com.prescription.features.networking.util.APIResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserCredentialRepository,
    private val medicineRepository: MedicineRepository,
    private val prescriptionRepository: PrescriptionRepository
) : ViewModel() {

    private val _user = MutableStateFlow<UserCredentialEntity?>(null)
    val user: StateFlow<UserCredentialEntity?> = _user

    private val _uiState = MutableStateFlow<UiState<Map<ProblemEntity, List<DrugEntity>>>>(UiState.Loading)
    val uiState: StateFlow<UiState<Map<ProblemEntity, List<DrugEntity>>>> = _uiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init {
        fetchLoggedInUser()
        fetchAndSaveMedicalDataRemote()
    }

    fun fetchMedicalDataLocal() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                // get all problems
                prescriptionRepository.getAllProblems().collect { problems ->
                    if (problems.isNotEmpty()) {
                        val problemsWithDrugs = mutableMapOf<ProblemEntity, List<DrugEntity>>()

                        for (problem in problems) {
                            val drugsForProblem = mutableListOf<DrugEntity>()

                            val medications = prescriptionRepository.getMedicationsForProblem(problem.id).firstOrNull()
                            medications?.forEach { medication ->
                                // drugs for each medication
                                val drugs = prescriptionRepository.getDrugsForMedication(medication.id).firstOrNull()
                                if (drugs != null) {
                                    drugsForProblem.addAll(drugs)
                                }
                            }

                            problemsWithDrugs[problem] = drugsForProblem
                        }

                        _uiState.value = UiState.Success(problemsWithDrugs)
                    } else {
                        _uiState.value = UiState.Error("No local data found")
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching local data: ${e.message}")
                _uiState.value = UiState.Error("Error fetching local data")
            }
        }
    }

    private fun fetchLoggedInUser() {
        viewModelScope.launch {
            userRepository.getLoggedInUser().collect { userEntity ->
                _user.value = userEntity
            }
        }
    }

    fun fetchAndSaveMedicalDataRemote() {
        viewModelScope.launch {
            when (val response = medicineRepository.getMedicalData()) {
                is APIResource.Success -> {
                    val medicalData = response.value
                    prescriptionRepository.purgeAllMedicalData()
                    delay(2000)
                    saveMedicalDataToDatabase(medicalData)
                }
                is APIResource.Error -> {
                    _uiState.value = UiState.Error(mapErrorToMessage(response.errorCode))
                }
                else -> {
                    _uiState.value = UiState.Error("Something went wrong")
                }
            }
        }
    }

    private fun saveMedicalDataToDatabase(medicalData: ApiResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("HomeViewModel", "Save data to local: $medicalData")
                medicalData.problems.forEach { problemMap ->
                    problemMap.forEach { (conditionName, problemsList) ->
                        problemsList.forEach { problem ->
                            // insert the condition into the database and get its ID
                            val conditionId = prescriptionRepository.insertProblem(
                                ProblemEntity(name = conditionName)
                            )

                            // process medications for each problem
                            problem.medications?.forEach { medication ->
                                medication.medicationsClasses?.forEach { medicationClass ->
                                    medicationClass.className?.forEach { classNameEntry ->
                                        processDrugEntries(classNameEntry.associatedDrug, conditionId, conditionName)
                                        processDrugEntries(classNameEntry.associatedDrug2, conditionId, conditionName)
                                    }

                                    medicationClass.className2?.forEach { classNameEntry ->
                                        processDrugEntries(classNameEntry.associatedDrug, conditionId, conditionName)
                                        processDrugEntries(classNameEntry.associatedDrug2, conditionId, conditionName)
                                    }

                                    delay(2000)
                                    fetchMedicalDataLocal()
                                }
                            }

                            // Pprocess labs for each problem (todo)
                            problem.labs?.forEach { lab ->

                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error saving medical data: ${e.message}")
                _uiState.value = UiState.Error("Error saving data to database")
            }
        }
    }

    private suspend fun processDrugEntries(
        associatedDrugs: List<Drug>?,
        conditionId: Long,
        conditionName: String
    ) {
        associatedDrugs?.forEach { drug ->
            val medicationId = prescriptionRepository.insertMedication(
                MedicationEntity(problemId = conditionId, medicationClass = conditionName)
            )

            prescriptionRepository.insertDrug(
                DrugEntity(medicationId = medicationId, name = drug.name ?: "", dose = drug.dose ?: "", strength = drug.strength ?: "")
            )
        }
    }

    private fun mapErrorToMessage(errorCode: Int?): String {
        return when (errorCode) {
            404 -> "Resource not found"
            500 -> "Server error"
            else -> "Something went wrong"
        }
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String? = null) : UiState<Nothing>()
}