package com.prescription.features.ui.condition_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.ui.Alignment
import com.airbnb.lottie.compose.LottieAnimation
import com.prescription.core.design.ds.InfiniteLottieAnimation
import com.prescription.database.entities.DrugEntity
import com.prescription.database.entities.ProblemEntity
import com.prescription.features.ui.home.UiState
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ConditionDetailsScreen(
    problemId: String,
    navigator: DestinationsNavigator,
    viewModel: ConditionDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(problemId) {
        viewModel.fetchProblemDetails(problemId)
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                val problemName = (uiState as? UiState.Success<Pair<ProblemEntity, List<DrugEntity>>>)
                    ?.data?.first?.name ?: "Condition Details"

                androidx.compose.material.TopAppBar(
                    title = { Text(text = problemName) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    val data = (uiState as UiState.Success<Pair<ProblemEntity, List<DrugEntity>>>).data
                    val drugs = data.second

                    if (drugs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            InfiniteLottieAnimation(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .aspectRatio(1f),
                                animationId = com.prescription.utils.R.raw.empty
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            items(drugs) { drug ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    elevation = 4.dp
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Drug: ${drug.medicationId}",
                                            style = MaterialTheme.typography.subtitle1
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "Name: ${drug.name}",
                                            style = MaterialTheme.typography.body1
                                        )
                                        Text(
                                            text = "Dose: ${drug.dose}",
                                            style = MaterialTheme.typography.body1
                                        )
                                        Text(
                                            text = "Strength: ${drug.strength}",
                                            style = MaterialTheme.typography.body1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = (uiState as UiState.Error).message ?: "Unknown error",
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.fetchProblemDetails(problemId) }) {
                                Text(text = "Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

