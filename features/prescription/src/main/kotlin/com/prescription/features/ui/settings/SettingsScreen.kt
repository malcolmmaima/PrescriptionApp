package com.prescription.features.ui.settings

import android.window.SplashScreen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.prescription.core.design.theme.PrescriptionTheme
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import com.prescription.utils.app.getAppVersion
import com.prescription.utils.preview.UIModePreviews

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val versionName = getAppVersion(context)

    val showLogoutDialog = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.SpaceBetween // Space out items
    ) {
        Column {
            // App Version Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "App Version", style = MaterialTheme.typography.body1)
                Text(text = versionName, style = MaterialTheme.typography.body1)
            }

            Divider()
        }

        // Logout Button
        Button(
            onClick = { showLogoutDialog.value = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Clear Data")
        }

        if (showLogoutDialog.value) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog.value = false },
                title = { Text("Clear Data Confirmation") },
                text = { Text("Are you sure you want to clear data?") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.clearAllData()
                        showLogoutDialog.value = false
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showLogoutDialog.value = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
@UIModePreviews
fun PreviewSettingsScreen() {
    SettingsScreen(navigator = EmptyDestinationsNavigator)
}