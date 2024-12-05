package com.prescription.features.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prescription.database.PrescriptionDatabase
import com.prescription.database.repository.PrescriptionRepository
import com.prescription.database.repository.UserCredentialRepository
import com.prescription.features.networking.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserCredentialRepository,
    private val prescriptionRepository: PrescriptionRepository
) : ViewModel() {

    fun clearAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUsersTable()
            prescriptionRepository.purgeAllMedicalData()
        }
    }
}
