package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.components.GameSyncDialog
import com.example.ui.components.HeaderBar
import com.example.ui.components.ManualResultDialog
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GuideScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.RoadmapScreen
import com.example.ui.screens.StrategiesScreen
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.viewmodel.PredictorViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PredictorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: PredictorViewModel) {
    val state by viewModel.uiState.collectAsState()

    // Handle back button to return to Dashboard tab
    if (state.selectedTab != 0) {
        BackHandler {
            viewModel.selectTab(0)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg),
        topBar = {
            HeaderBar(
                demoBalance = state.stats.demoBalance,
                isAutoMode = state.isAutoMode,
                language = state.language,
                isVibrationEnabled = state.isVibrationEnabled,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onToggleVibration = { viewModel.toggleVibration() }
            )
        },
        bottomBar = {
            PredictorBottomNav(
                selectedTab = state.selectedTab,
                language = state.language,
                onSelectTab = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBg)
                .padding(innerPadding)
        ) {
            when (state.selectedTab) {
                0 -> DashboardScreen(
                    periodId = state.currentPeriodId,
                    pendingPrediction = state.pendingPrediction,
                    history = state.history,
                    stats = state.stats,
                    activeAlgorithm = state.activeAlgorithm,
                    isAutoMode = state.isAutoMode,
                    isTimerRunning = state.isTimerRunning,
                    secondsRemaining = state.timerSecondsRemaining,
                    timerDuration = state.timerDuration,
                    language = state.language,
                    lastResultFlash = state.lastResultFlash,
                    lastWonStatus = state.lastWonStatus,
                    onToggleAutoMode = { viewModel.toggleAutoMode() },
                    onToggleTimerRunning = { viewModel.toggleTimerRunning() },
                    onSelectDuration = { viewModel.setTimerDuration(it) },
                    onSimulateNow = { viewModel.resolveRoundWithSimulatedNumber() },
                    onOpenManualDialog = { viewModel.setManualDialogVisible(true) },
                    onOpenSyncDialog = { viewModel.setSyncDialogVisible(true) },
                    isLiveApiConnected = state.isLiveApiConnected,
                    apiSyncMessage = state.apiSyncMessage,
                    onSelectAlgorithm = { viewModel.setAlgorithm(it) }
                )
                1 -> RoadmapScreen(
                    history = state.history,
                    language = state.language
                )
                2 -> HistoryScreen(
                    history = state.history,
                    language = state.language,
                    onClearHistory = { viewModel.clearHistory() }
                )
                3 -> StrategiesScreen(
                    activeAlgorithm = state.activeAlgorithm,
                    activeRiskStrategy = state.activeRiskStrategy,
                    language = state.language,
                    onSelectAlgorithm = { viewModel.setAlgorithm(it) },
                    onSelectRiskStrategy = { viewModel.setRiskStrategy(it) },
                    onResetBalance = { viewModel.resetDemoBalance() }
                )
                4 -> GuideScreen(
                    language = state.language
                )
            }

            // Manual Entry Modal Dialog
            if (state.isManualDialogVisible) {
                ManualResultDialog(
                    periodId = state.currentPeriodId,
                    language = state.language,
                    onDismiss = { viewModel.setManualDialogVisible(false) },
                    onSelectNumber = { viewModel.resolveRoundWithManualNumber(it) }
                )
            }

            // Game & Live API Synchronization Dialog
            if (state.isSyncDialogVisible) {
                GameSyncDialog(
                    currentPeriodId = state.currentPeriodId,
                    currentSecondsRemaining = state.timerSecondsRemaining,
                    liveApiUrl = state.liveApiUrl,
                    isLiveApiConnected = state.isLiveApiConnected,
                    isApiPolling = state.isApiPolling,
                    apiSyncMessage = state.apiSyncMessage,
                    language = state.language,
                    onDismiss = { viewModel.setSyncDialogVisible(false) },
                    onSyncGame = { pId, sec, nums ->
                        viewModel.syncWithUserGame(pId, sec, nums)
                    },
                    onConnectLiveApi = { url ->
                        viewModel.connectLiveApi(url)
                    },
                    onDisconnectLiveApi = {
                        viewModel.disconnectLiveApi()
                    }
                )
            }
        }
    }
}

@Composable
fun PredictorBottomNav(
    selectedTab: Int,
    language: AppLanguage,
    onSelectTab: (Int) -> Unit
) {
    val navItems = listOf(
        Triple(0, if (language == AppLanguage.BENGALI) "প্রেডিক্টর" else "Predictor", Icons.Default.Bolt),
        Triple(1, if (language == AppLanguage.BENGALI) "রোডম্যাপ" else "Roadmap", Icons.Default.ShowChart),
        Triple(2, if (language == AppLanguage.BENGALI) "ইতিহাস" else "History", Icons.Default.History),
        Triple(3, if (language == AppLanguage.BENGALI) "অ্যালগরিদম" else "Strategy", Icons.Default.AutoAwesome),
        Triple(4, if (language == AppLanguage.BENGALI) "গাইড" else "Guide", Icons.Default.MenuBook)
    )

    NavigationBar(
        containerColor = DarkSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
    ) {
        navItems.forEach { (index, title, icon) ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelectTab(index) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkBg,
                    selectedTextColor = PrimaryNeon,
                    indicatorColor = PrimaryNeon,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("nav_item_$index")
            )
        }
    }
}
