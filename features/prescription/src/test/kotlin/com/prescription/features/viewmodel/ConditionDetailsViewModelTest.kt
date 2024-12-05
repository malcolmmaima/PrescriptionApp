package com.prescription.features.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.prescription.database.entities.DrugEntity
import com.prescription.database.entities.ProblemEntity
import com.prescription.database.repository.PrescriptionRepository
import com.prescription.features.ui.condition_detail.ConditionDetailsViewModel
import com.prescription.features.ui.home.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class ConditionDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ConditionDetailsViewModel
    private val repository: PrescriptionRepository = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = ConditionDetailsViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchProblemDetails should update uiState with success when problem and drugs are found`() = runTest {
        val problemId = "1"
        val problem = ProblemEntity(id = 1, name = "Problem 1") // Adjust according to your entity
        val drugs = listOf(DrugEntity(name = "Drug 1", medicationId = 1, dose = "", strength = "500 mg"))

        coEvery { repository.getProblemById(problemId) } returns flow { emit(problem) }
        coEvery { repository.getDrugsByProblemId(problemId.toInt()) } returns flow { emit(drugs) }

        viewModel.fetchProblemDetails(problemId)

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Loading)

            val successState = awaitItem()
            assertTrue(successState is UiState.Success)
            val result = (successState as UiState.Success).data
            assertTrue(result.first == problem)
            assertTrue(result.second == drugs)
        }
    }

    @Test
    fun `fetchProblemDetails should update uiState with error when problem is not found`() = runTest {
        val problemId = "1"

        coEvery { repository.getProblemById(problemId) } returns flow { emit(null) }

        viewModel.fetchProblemDetails(problemId)

        viewModel.uiState.test {
            assertTrue(awaitItem() is UiState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is UiState.Error)
            val errorMessage = (errorState as UiState.Error).message
            assertTrue(errorMessage == "Problem not found")
        }
    }
}
