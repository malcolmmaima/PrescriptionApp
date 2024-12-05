package com.prescription.features.viewmodel

import ApiResponse
import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.prescription.database.entities.DrugEntity
import com.prescription.database.entities.MedicationEntity
import com.prescription.database.entities.ProblemEntity
import com.prescription.database.entities.UserCredentialEntity
import com.prescription.database.repository.PrescriptionRepository
import com.prescription.database.repository.UserCredentialRepository
import com.prescription.features.networking.repository.MedicineRepository
import com.prescription.features.networking.util.APIResource
import com.prescription.features.ui.home.HomeViewModel
import com.prescription.features.ui.home.UiState
import io.mockk.*
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private val medicineRepository: MedicineRepository = mockk(relaxed = true)
    private val prescriptionRepository: PrescriptionRepository = mockk(relaxed = true)
    private val userRepository: UserCredentialRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        coEvery { medicineRepository.getMedicalData() } returns APIResource.Success(ApiResponse(emptyList()))
        coEvery { prescriptionRepository.getAllProblems() } returns flowOf(emptyList())
        coEvery { userRepository.getLoggedInUser() } returns flowOf(null)

        viewModel = HomeViewModel(userRepository, medicineRepository, prescriptionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMedicalDataLocal should update uiState with problems on success`() = runTest {
        val problems = listOf(ProblemEntity(name = "Problem 1"))
        val medications = listOf(MedicationEntity(id = 1, problemId = 1, medicationClass = "Class A"))
        val drugs = listOf(DrugEntity(name = "Drug 1", medicationId = 1, dose = "2mg", strength = "Strong"))

        coEvery { prescriptionRepository.getAllProblems() } returns flowOf(problems)
        coEvery { prescriptionRepository.getMedicationsForProblem(any()) } returns flowOf(medications)
        coEvery { prescriptionRepository.getDrugsForMedication(any()) } returns flowOf(drugs)

        viewModel.fetchMedicalDataLocal()
        advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is UiState.Success)
        val data = (uiState as UiState.Success).data

        @Suppress("UNCHECKED_CAST")
        val typedData = data as Map<String, List<Any>>

        assertTrue(typedData.isNotEmpty())
    }

    @Test
    fun `fetchMedicalDataLocal should update uiState with error when no local data found`() = runTest {
        coEvery { prescriptionRepository.getAllProblems() } returns flowOf(emptyList())

        viewModel.fetchMedicalDataLocal()
        advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is UiState.Error)
        assertEquals("No local data found", uiState.message)
    }


    @Test
    fun `fetchMedicalDataRemote should update uiState with error on failure`() = runTest {
        coEvery { medicineRepository.getMedicalData() } returns APIResource.Error(isNetworkError = false, errorCode = 404, errorBody = null)

        viewModel.fetchAndSaveMedicalDataRemote()
        advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is UiState.Error)
        assertEquals("Resource not found", uiState.message)
    }

    @Test
    fun `fetchMedicalDataRemote should handle network error correctly`() = runTest {
        coEvery { medicineRepository.getMedicalData() } returns APIResource.Error(isNetworkError = true, errorCode = null, errorBody = null)

        viewModel.fetchAndSaveMedicalDataRemote()
        advanceUntilIdle()

        val uiState = viewModel.uiState.first()
        assertTrue(uiState is UiState.Error)
        assertEquals("Something went wrong", uiState.message)
    }

    @Test
    fun `fetchLoggedInUser should update user state when user is found`() = runTest {
        val user = UserCredentialEntity(username = "user", hashedPassword = "hashed_password")
        coEvery { userRepository.getLoggedInUser() } returns flowOf(user)

        viewModel.fetchLoggedInUser()
        advanceUntilIdle()

        val currentUser = viewModel.user.value
        assertEquals(user, currentUser)
    }

    @Test
    fun `fetchLoggedInUser should not update user state when user is not found`() = runTest {
        coEvery { userRepository.getLoggedInUser() } returns flowOf(null)

        viewModel.fetchLoggedInUser()
        advanceUntilIdle()

        val currentUser = viewModel.user.value
        assertNull(currentUser)
    }
}
