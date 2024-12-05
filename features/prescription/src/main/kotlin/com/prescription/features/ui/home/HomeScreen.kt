package com.prescription.features.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.rememberLottieComposition
import com.prescription.core.design.ds.InfiniteLottieAnimation
import com.prescription.database.entities.DrugEntity
import com.prescription.database.entities.ProblemEntity
import com.prescription.features.ui.condition_detail.ConditionDetailsScreen
import com.ramcosta.composedestinations.annotation.Destination
import java.time.LocalTime

@OptIn(ExperimentalMaterialApi::class)
@Destination(start = true)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navigator: DestinationsNavigator) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val user by viewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val currentHour = LocalTime.now().hour
    val greeting = when {
        currentHour in 5..11 -> "Good Morning"
        currentHour in 12..17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    var selectedProblemId by remember { mutableStateOf<String?>(null) }
    val refreshing = remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing = refreshing.value, onRefresh = {
        refreshing.value = true
        viewModel.fetchMedicalDataLocal()
        refreshing.value = false
    })

    if (selectedProblemId != null) {
        ConditionDetailsScreen(
            problemId = selectedProblemId!!,
            navigator = navigator
        )
    } else {
        Scaffold(
            content = { paddingValues ->
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 32.dp)
                            .fillMaxSize()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 4.dp,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray)
                                )
                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = "$greeting, ${user?.username ?: ""}!",
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        when (uiState) {
                            is UiState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    InfiniteLottieAnimation(
                                        modifier = Modifier
                                            .size(150.dp)
                                            .align(Alignment.Center),
                                        animationId = com.prescription.utils.R.raw.health
                                    )
                                }
                            }
                            is UiState.Success -> {
                                val problemsWithDrugs =
                                    (uiState as UiState.Success<Map<ProblemEntity, List<DrugEntity>>>).data

                                LazyColumn(
                                    contentPadding = paddingValues,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    problemsWithDrugs.forEach { (problem, drugs) ->
                                        item {
                                            MedicineCard(
                                                problem = problem,
                                                drugs = drugs,
                                                onClick = {
                                                    requireNotNull(problem.id) { "Problem ID must not be null" }
                                                    selectedProblemId = problem.id.toString()
                                                }
                                            )
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
                                            text = (uiState as UiState.Error).message
                                                ?: "Unknown error",
                                            color = Color.Red
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Button(
                                            onClick = {
                                                coroutineScope.launch {
                                                    viewModel.fetchMedicalDataLocal()
                                                }
                                            }
                                        ) {
                                            Text(text = "Retry")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun MedicineCard(problem: ProblemEntity, drugs: List<DrugEntity>, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Condition: ${problem.name}", style = MaterialTheme.typography.subtitle1)

            Spacer(modifier = Modifier.height(8.dp))

            drugs.forEach { drug ->

                Text(
                    text = "• Drug: ${drug.medicationId}",
                    style = MaterialTheme.typography.subtitle2
                )

                Column(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp, bottom = 8.dp)
                ) {
                    Text(text = "Medicine: ${drug.name}", style = MaterialTheme.typography.body2)
                    Text(text = "Dose: ${drug.dose}", style = MaterialTheme.typography.body2)
                    Text(text = "Strength: ${drug.strength}", style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}
