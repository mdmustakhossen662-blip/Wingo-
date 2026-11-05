package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.GamePeriod
import com.example.model.OutcomeType
import com.example.ui.theme.BigColor
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GreenOutcome
import com.example.ui.theme.LossRed
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.RedOutcome
import com.example.ui.theme.SmallColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletOutcome
import com.example.ui.theme.WinGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    history: List<GamePeriod>,
    language: AppLanguage,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = remember(history, selectedFilter) {
        when (selectedFilter) {
            "WON" -> history.filter { it.isWon == true }
            "LOST" -> history.filter { it.isWon == false }
            "BIG" -> history.filter { it.actualOutcome == OutcomeType.BIG }
            "SMALL" -> history.filter { it.actualOutcome == OutcomeType.SMALL }
            else -> history
        }
    }

    val totalCompleted = history.count { it.isWon != null }
    val winCount = history.count { it.isWon == true }
    val lossCount = history.count { it.isWon == false }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // Top Header & Clear Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.BENGALI) "প্রেডিকশন ইতিহাস ও লগ" else "PREDICTION AUDIT LOG",
                    color = TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "$totalCompleted ${if (language == AppLanguage.BENGALI) "রাউন্ড সংরক্ষিত" else "rounds logged"} ($winCount W / $lossCount L)",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            IconButton(
                onClick = onClearHistory,
                modifier = Modifier.testTag("clear_history_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear History",
                    tint = TextMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Chips Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "ALL" to (if (language == AppLanguage.BENGALI) "সব" else "ALL"),
                "WON" to (if (language == AppLanguage.BENGALI) "জয়" else "WON"),
                "LOST" to (if (language == AppLanguage.BENGALI) "পরাজয়" else "LOST"),
                "BIG" to (if (language == AppLanguage.BENGALI) "বিগ" else "BIG"),
                "SMALL" to (if (language == AppLanguage.BENGALI) "স্মল" else "SMALL")
            ).forEach { (key, label) ->
                val isSelected = selectedFilter == key
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) PrimaryNeon else DarkCard)
                        .border(
                            1.dp,
                            if (isSelected) PrimaryNeon else DarkCardBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedFilter = key }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = if (isSelected) Color.Black else TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // History List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = if (language == AppLanguage.BENGALI) "কোনো রাউন্ড পাওয়া যায়নি" else "No matching rounds in history",
                    color = TextMuted,
                    fontSize = 13.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredList) { round ->
                    HistoryItemCard(round = round, language = language)
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun HistoryItemCard(
    round: GamePeriod,
    language: AppLanguage
) {
    val isBig = round.actualOutcome == OutcomeType.BIG
    val outcomeColor = if (isBig) BigColor else SmallColor
    val isWon = round.isWon == true
    val timeStr = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date(round.timestamp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCard)
            .border(
                1.dp,
                if (isWon) WinGreen.copy(alpha = 0.4f) else LossRed.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Period ID, Time, Prediction
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = round.periodId,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = timeStr,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "প্রেডিকশন: " else "PRED: ",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                    Text(
                        text = if (round.predictedOutcome == OutcomeType.BIG) "BIG" else "SMALL",
                        color = if (round.predictedOutcome == OutcomeType.BIG) BigColor else SmallColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = " (${round.confidence}%)",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${round.multiplierLevel}X",
                        color = PrimaryNeon,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Right: Actual Number, Outcome badge, Result
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Actual Number circle
                val numColor = when (round.actualNumber) {
                    0, 5 -> VioletOutcome
                    1, 3, 7, 9 -> GreenOutcome
                    else -> RedOutcome
                }
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(numColor.copy(alpha = 0.2f))
                        .border(1.5.dp, numColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${round.actualNumber ?: "-"}",
                        color = numColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Outcome (BIG / SMALL)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(outcomeColor.copy(alpha = 0.15f))
                        .border(1.dp, outcomeColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isBig) "BIG" else "SMALL",
                        color = outcomeColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // WON / LOST Tag
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isWon) Icons.Default.CheckCircle else Icons.Default.Close,
                            contentDescription = if (isWon) "Win" else "Loss",
                            tint = if (isWon) WinGreen else LossRed,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = if (isWon) "WON" else "LOST",
                            color = if (isWon) WinGreen else LossRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Text(
                        text = (if (round.virtualProfit >= 0) "+$" else "-$") +
                                String.format(Locale.US, "%.1f", kotlin.math.abs(round.virtualProfit)),
                        color = if (round.virtualProfit >= 0) WinGreen else LossRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
