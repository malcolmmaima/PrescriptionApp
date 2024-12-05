package com.prescription.features.networking.repository

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
    private lateinit var productRepository: com.prescription.features.networking.repository.MedicineRepository

    @Before
    fun setUp() {
        apiService = mockk()
        productRepository =
            com.prescription.features.networking.repository.MedicineRepository(apiService)
    }

    @Test
    fun `getProducts returns success response`() = runTest {

        val mockResponse = listOf(
            ProductItemResponse(
                id = 1,
                name = "Product 1",
                description = "Description 1",
                price = 10.0,
                currencyCode = "USD",
                currencySymbol = "$",
                quantity = 5,
                imageLocation = "image1.png",
                status = "Available"
            ),
            ProductItemResponse(
                id = 2,
                name = "Product 2",
                description = "Description 2",
                price = 20.0,
                currencyCode = "USD",
                currencySymbol = "$",
                quantity = 10,
                imageLocation = "image2.png",
                status = "Available"
            )
        )
        val mockResource = APIResource.Success(mockResponse)
        coEvery { apiService.getMedicalData() } returns mockResponse

        val result = productRepository.getMedicalData()

        assertEquals(mockResource, result)
        coVerify { apiService.getMedicalData() }
    }

    @Test
    fun `getProducts returns error response`() = runTest {
        val errorBody = Response.error<List<ProductItemResponse>>(404, mockk(relaxed = true)).errorBody()
        val exception = HttpException(Response.error<List<ProductItemResponse>>(404, errorBody))

        coEvery { apiService.getMedicalData() } throws exception

        val mockResource = APIResource.Error(
            isNetworkError = false,
            errorCode = 404,
            errorBody = errorBody
        )

        val result = productRepository.getMedicalData()

        assertEquals(mockResource, result)
        coVerify { apiService.getMedicalData() }
    }
}






