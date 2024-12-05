package com.prescription.features.networking.repository

import ApiResponse
import ClassNameEntry
import Drug
import Medication
import MedicationClass
import Problem
import com.prescription.features.networking.api.MedicineApiService
import com.prescription.features.networking.util.APIResource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import retrofit2.HttpException

@ExperimentalCoroutinesApi
class MedicineRepositoryTest {

    private lateinit var apiService: MedicineApiService
    private lateinit var medicineRepository: MedicineRepository

    @Before
    fun setUp() {
        apiService = mockk()
        medicineRepository = MedicineRepository(apiService)
    }

    @Test
    fun `getMedicalData returns success response`() = runTest {
        val mockResponse = ApiResponse(
            problems = listOf(
                mapOf(
                    "problem1" to listOf(
                        Problem(
                            medications = listOf(
                                Medication(
                                    medicationsClasses = listOf(
                                        MedicationClass(
                                            className = listOf(
                                                ClassNameEntry(associatedDrug = listOf(Drug(name = "Drug1", dose = "10mg", strength = "100mg")), associatedDrug2 = null)
                                            ),
                                            className2 = null
                                        )
                                    )
                                )
                            ),
                            labs = null
                        )
                    )
                )
            )
        )

        coEvery { apiService.getMedicalData() } returns mockResponse

        val result = medicineRepository.getMedicalData()

        val mockResource = APIResource.Success(mockResponse)

        assertEquals(mockResource, result)
        coVerify { apiService.getMedicalData() }
    }

    @Test
    fun `getMedicalData returns error response`() = runTest {
        val errorBody = Response.error<ApiResponse>(404, mockk(relaxed = true)).errorBody()
        val exception = HttpException(Response.error<ApiResponse>(404, errorBody))

        coEvery { apiService.getMedicalData() } throws exception

        val mockResource = APIResource.Error(
            isNetworkError = false,
            errorCode = 404,
            errorBody = errorBody
        )

        val result = medicineRepository.getMedicalData()

        assertEquals(mockResource, result)
        coVerify { apiService.getMedicalData() }
    }
}






