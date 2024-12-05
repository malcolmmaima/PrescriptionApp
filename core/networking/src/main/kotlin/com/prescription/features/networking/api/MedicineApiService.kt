package com.prescription.features.networking.api

import ApiResponse
import retrofit2.http.GET

interface MedicineApiService {
    @GET("v3/8d9343fd-c7e8-46f4-b53c-5cf351d91fc0")
    suspend fun getMedicalData() : ApiResponse

}
