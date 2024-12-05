package com.prescription.app.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.prescription.core.design.theme.PrescriptionTheme
import com.prescription.features.ui.home.HomeScreen
import com.prescription.features.ui.settings.SettingsScreen
import com.prescription.utils.navigation.FadeInOutAnimation
import com.prescription.utils.navigation.MockDestinationsNavigator
import com.prescription.utils.preview.UIModePreviews

@Destination(style = FadeInOutAnimation::class)
@Composable
fun NavigationScreen(
    navigator: DestinationsNavigator,
) {
    val (currentBottomTab, setCurrentBottomTab) = rememberSaveable {
        mutableStateOf(BottomBarHomeItem.HOME)
    }

    Scaffold(
        bottomBar = {
            HomeBottomNavigation(
                bottomTab = currentBottomTab,
                setCurrentBottomTab = setCurrentBottomTab
            )
        },
        content = { paddingValues ->
            val modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()

            when (currentBottomTab) {
                BottomBarHomeItem.HOME -> HomeScreen(
                    modifier = modifier,
                    navigator = navigator
                )

                BottomBarHomeItem.SETTINGS -> SettingsScreen(
                    modifier = modifier,
                    navigator = navigator
                )
            }
        }
    )
}

@Composable
private fun HomeBottomNavigation(
    bottomTab: BottomBarHomeItem,
    setCurrentBottomTab: (BottomBarHomeItem) -> Unit
) {
    val pages = BottomBarHomeItem.entries.toTypedArray()
    val bottomBarHeight = 100.dp

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()
            .height(bottomBarHeight)
    ) {
        pages.forEach { page ->
            val selected = page == bottomTab
            val selectedLabelColor = if (selected) {
                MaterialTheme.colors.primary
            } else {
                Gray
            }
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = rememberVectorPainter(image = page.icon),
                        contentDescription = stringResource(page.title)
                    )
                },
                label = {
                    Text(
                        text = stringResource(page.title),
                        color = selectedLabelColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                selected = selected,
                onClick = {
                    setCurrentBottomTab.invoke(page)
                },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Gray,
                alwaysShowLabel = true,
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}

@UIModePreviews
@Composable
private fun HomeScreenPreview() {
    PrescriptionTheme {
        HomeScreen(
            navigator = MockDestinationsNavigator()
        )
    }
}
