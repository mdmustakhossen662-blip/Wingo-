package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AlgorithmType
import com.example.model.AppLanguage
import com.example.model.GamePeriod
import com.example.model.OutcomeType
import com.example.model.PredictionStats
import com.example.ui.components.CountdownTimerCard
import com.example.ui.components.PredictionHeroCard
import com.example.ui.theme.BigColor
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.LossRed
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryNeon
import com.example.ui.theme.SmallColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WinGreen

@Composable
fun DashboardScreen(
    periodId: String,
    pendingPrediction: GamePeriod?,
    history: List<GamePeriod>,
    stats: PredictionStats,
    activeAlgorithm: AlgorithmType,
    isAutoMode: Boolean,
    isTimerRunning: Boolean,
    secondsRemaining: Int,
    timerDuration: Int,
    language: AppLanguage,
    lastResultFlash: Boolean,
    lastWonStatus: Boolean?,
    onToggleAutoMode: () -> Unit,
    onToggleTimerRunning: () -> Unit,
    onSelectDuration: (Int) -> Unit,
    onSimulateNow: () -> Unit,
    onOpenManualDialog: () -> Unit,
    onOpenSyncDialog: () -> Unit,
    isLiveApiConnected: Boolean = false,
    apiSyncMessage: String = "",
    onSelectAlgorithm: (AlgorithmType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Countdown & Period Header
        CountdownTimerCard(
            periodId = periodId,
            secondsRemaining = secondsRemaining,
            totalDuration = timerDuration,
            isAutoMode = isAutoMode,
            isTimerRunning = isTimerRunning,
            language = language,
            onToggleAutoMode = onToggleAutoMode,
            onToggleTimerRunning = onToggleTimerRunning,
            onSelectDuration = onSelectDuration,
            onSimulateNow = onSimulateNow,
            onOpenManualDialog = onOpenManualDialog
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Game & API Match Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(if (isLiveApiConnected) WinGreen.copy(alpha = 0.12f) else DarkCard)
                .border(
                    1.dp,
                    if (isLiveApiConnected) WinGreen.copy(alpha = 0.4f) else DarkCardBorder,
                    RoundedCornerShape(12.dp)
                )
                .clickable { onOpenSyncDialog() }
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .testTag("open_sync_dialog_btn")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(if (isLiveApiConnected) WinGreen else PrimaryNeon),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isLiveApiConnected) "API" else "SYNC",
                            color = Color.Black,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.BENGALI) "আমার গেম বা API এর সাথে মিলান" else "Sync with My Game / Live API",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isLiveApiConnected) apiSyncMessage.ifBlank { "Live API Online" }
                            else (if (language == AppLanguage.BENGALI) "রাউন্ড নম্বর, সময় বা লাইভ API লিঙ্ক সেট করতে ট্যাপ করুন" else "Tap to sync live period, timer or custom API URL"),
                            color = if (isLiveApiConnected) WinGreen else TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(PrimaryNeon.copy(alpha = 0.15f))
                        .border(1.dp, PrimaryNeon.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "মিলান" else "SYNC",
                        color = PrimaryNeon,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Prediction Giant Display
        PredictionHeroCard(
            prediction = pendingPrediction,
            language = language,
            lastResultFlash = lastResultFlash,
            lastWonStatus = lastWonStatus
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Algorithm Selector Chips Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AlgorithmType.values().forEach { algo ->
                val isSelected = algo == activeAlgorithm
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) PrimaryNeon else DarkCard)
                        .border(
                            1.dp,
                            if (isSelected) PrimaryNeon else DarkCardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onSelectAlgorithm(algo) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("algo_chip_${algo.name}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) algo.titleBn else algo.title,
                        color = if (isSelected) Color.Black else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent 8 Rounds Quick Strip
        Text(
            text = if (language == AppLanguage.BENGALI) "সাম্প্রতিক রাউন্ডসমূহ" else "RECENT ROUNDS",
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        val recentList = history.take(8)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            recentList.forEach { round ->
                val isBig = round.actualOutcome == OutcomeType.BIG
                val outcomeColor = if (isBig) BigColor else SmallColor
                val isWon = round.isWon == true

                Box(
                    modifier = Modifier
                        .width(76.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkCard)
                        .border(
                            1.dp,
                            if (isWon) WinGreen.copy(alpha = 0.5f) else LossRed.copy(alpha = 0.5f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = round.periodId.takeLast(4),
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(outcomeColor.copy(alpha = 0.2f))
                                .border(1.5.dp, outcomeColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${round.actualNumber ?: (if (isBig) "B" else "S")}",
                                color = outcomeColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isWon) Icons.Default.CheckCircle else Icons.Default.Close,
                                contentDescription = if (isWon) "Win" else "Loss",
                                tint = if (isWon) WinGreen else LossRed,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = if (isWon) "WIN" else "LOSS",
                                color = if (isWon) WinGreen else LossRed,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Performance Statistics Grid (2x2)
        Text(
            text = if (language == AppLanguage.BENGALI) "কার্যক্ষমতার পরিসংখ্যান" else "PERFORMANCE ANALYTICS",
            color = TextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Win Rate
            StatCard(
                title = if (language == AppLanguage.BENGALI) "সঠিকতার হার" else "WIN ACCURACY",
                value = "${String.format(java.util.Locale.US, "%.1f", stats.winRate)}%",
                subtitle = "${stats.wins} W / ${stats.losses} L",
                color = PrimaryNeon,
                modifier = Modifier.weight(1f)
            )

            // Current Streak
            StatCard(
                title = if (language == AppLanguage.BENGALI) "চলতি ধারাবাহিকতা" else "CURRENT STREAK",
                value = "${stats.currentStreak} " + if (stats.isCurrentWinStreak) "WINS" else "LOSS",
                subtitle = "Best: ${stats.bestWinStreak} Wins",
                color = if (stats.isCurrentWinStreak) WinGreen else LossRed,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Big / Small Ratio
            StatCard(
                title = if (language == AppLanguage.BENGALI) "বিগ বনাম স্মল" else "BIG vs SMALL",
                value = "${stats.bigCount}B / ${stats.smallCount}S",
                subtitle = "Total: ${stats.totalPredictions} Rounds",
                color = SecondaryNeon,
                modifier = Modifier.weight(1f)
            )

            // Virtual Profit
            StatCard(
                title = if (language == AppLanguage.BENGALI) "ভার্চুয়াল পি/এল" else "VIRTUAL P/L",
                value = (if (stats.totalProfit >= 0) "+$" else "-$") +
                        String.format(java.util.Locale.US, "%.2f", kotlin.math.abs(stats.totalProfit)),
                subtitle = "Wallet: $${String.format(java.util.Locale.US, "%.0f", stats.demoBalance)}",
                color = if (stats.totalProfit >= 0) WinGreen else LossRed,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(DarkCard)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = title,
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 10.sp
            )
        }
    }
}
