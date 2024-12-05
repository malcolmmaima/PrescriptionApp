package com.prescription.app.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prescription.app.ui.splash.SplashViewContract.Effect
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.prescription.app.R
import com.prescription.core.design.ds.InfiniteLottieAnimation
import com.prescription.core.design.theme.PrescriptionTheme
import com.prescription.app.ui.NavGraphs
import com.prescription.app.ui.destinations.LoginScreenDestination
import com.prescription.utils.mvi.EffectCollector
import com.prescription.utils.preview.UIModePreviews

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    vm: SplashViewModel = hiltViewModel(),
) {
    EffectCollector(effect = vm.effect) { effect ->
        when (effect) {
            Effect.Completed -> navigator.navigate(LoginScreenDestination) {
                popUpTo(NavGraphs.root.route) { inclusive = true }
                launchSingleTop = true
            }

        }
    }

    SplashScreenContent()
}

@Composable
private fun SplashScreenContent() {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InfiniteLottieAnimation(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .aspectRatio(1f),
                    animationId = com.prescription.utils.R.raw.health
                )
                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h2,
                    fontStyle = MaterialTheme.typography.h2.fontStyle,
                    fontSize = 32.sp
                )
            }
        }
    }
}

@UIModePreviews
@Composable
private fun SplashScreenPreview() {
    PrescriptionTheme {
        SplashScreenContent()
    }
}
