package com.prescription.app.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.prescription.app.ui.NavGraphs

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navHostEngine = rememberAnimatedNavHostEngine()

    DestinationsNavHost(
        navController = navController,
        engine = navHostEngine,
        navGraph = NavGraphs.root,
        startRoute = NavGraphs.root.startRoute,
    )
}
