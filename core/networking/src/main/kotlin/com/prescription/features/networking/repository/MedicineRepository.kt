package com.prescription.features.networking.repository

import com.prescription.features.networking.api.MedicineApiService
import com.prescription.features.networking.util.BaseRepo
import javax.inject.Inject

class MedicineRepository @Inject constructor(
    private val apiService: MedicineApiService
) : BaseRepo() {

    suspend fun getMedicalData() = safeApiCall {
        apiService.getMedicalData()
    }
}







