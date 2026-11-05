package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ColorType
import com.example.model.GamePeriod
import com.example.model.OutcomeType
import com.example.ui.theme.BigColor
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GreenOutcome
import com.example.ui.theme.RedOutcome
import com.example.ui.theme.SecondaryNeon
import com.example.ui.theme.SmallColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletOutcome

@Composable
fun RoadmapView(
    history: List<GamePeriod>,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val completedRounds = history.filter { it.actualOutcome != null }
    val totalCount = completedRounds.size
    val bigCount = completedRounds.count { it.actualOutcome == OutcomeType.BIG }
    val smallCount = completedRounds.count { it.actualOutcome == OutcomeType.SMALL }
    val bigPct = if (totalCount > 0) (bigCount.toFloat() / totalCount * 100).toInt() else 50
    val smallPct = 100 - bigPct

    Column(modifier = modifier.fillMaxWidth()) {
        // Big vs Small Distribution Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${if (language == AppLanguage.BENGALI) "বিগ" else "BIG"}: $bigCount ($bigPct%)",
                        color = BigColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${if (language == AppLanguage.BENGALI) "স্মল" else "SMALL"}: $smallCount ($smallPct%)",
                        color = SmallColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Split Progress Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .weight(bigPct.coerceAtLeast(5).toFloat())
                            .height(8.dp)
                            .background(BigColor)
                    )
                    Box(
                        modifier = Modifier
                            .weight(smallPct.coerceAtLeast(5).toFloat())
                            .height(8.dp)
                            .background(SmallColor)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bead Plate (Sequential 6-row Matrix)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (language == AppLanguage.BENGALI) "বিড প্লেট প্যাটার্ন (ক্রমিক রোডম্যাপ)" else "BEAD PLATE (Sequential Matrix)",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                val rowCount = 6
                val reversedList = completedRounds.take(48).reversed()
                val colCount = ((reversedList.size + rowCount - 1) / rowCount).coerceAtLeast(8)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (c in 0 until colCount) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (r in 0 until rowCount) {
                                val index = (c * rowCount) + r
                                if (index < reversedList.size) {
                                    val item = reversedList[index]
                                    val isBig = item.actualOutcome == OutcomeType.BIG
                                    val badgeColor = if (isBig) BigColor else SmallColor
                                    val letter = if (isBig) "B" else "S"

                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(badgeColor.copy(alpha = 0.2f))
                                            .border(1.5.dp, badgeColor, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${item.actualNumber ?: letter}",
                                            color = badgeColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                } else {
                                    // Empty slot
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(DarkSurface)
                                            .border(0.5.dp, DarkCardBorder, CircleShape)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Big Road (Streak Matrix)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (language == AppLanguage.BENGALI) "বিগ রোড (ট্রেন্ড স্ট্রাইক কলাম)" else "BIG ROAD (Streak Columns)",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Build Big Road Columns
                val streakColumns = mutableListOf<List<GamePeriod>>()
                var currentColumn = mutableListOf<GamePeriod>()
                var currentOutcome: OutcomeType? = null

                for (round in completedRounds.reversed()) {
                    val out = round.actualOutcome ?: continue
                    if (currentOutcome == null || out == currentOutcome) {
                        currentOutcome = out
                        currentColumn.add(round)
                    } else {
                        if (currentColumn.isNotEmpty()) {
                            streakColumns.add(currentColumn.toList())
                        }
                        currentColumn = mutableListOf(round)
                        currentOutcome = out
                    }
                }
                if (currentColumn.isNotEmpty()) {
                    streakColumns.add(currentColumn)
                }

                val maxRows = 6
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    streakColumns.takeLast(16).forEach { colRounds ->
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            colRounds.take(maxRows).forEach { item ->
                                val isBig = item.actualOutcome == OutcomeType.BIG
                                val color = if (isBig) BigColor else SmallColor
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isBig) "B" else "S",
                                        color = Color.Black,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                            // Empty pads to maintain alignment
                            val padCount = maxRows - colRounds.take(maxRows).size
                            for (p in 0 until padCount) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                        .background(Color.Transparent)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hot & Cold Number Occurrence Grid
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = if (language == AppLanguage.BENGALI) "হট ও কোল্ড সংখ্যার পৌনঃপুনিকতা" else "HOT & COLD NUMBER FREQUENCY",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                val numberCounts = (0..9).associateWith { num ->
                    completedRounds.count { it.actualNumber == num }
                }
                val maxCount = (numberCounts.values.maxOrNull() ?: 1).coerceAtLeast(1)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    (0..9).forEach { num ->
                        val count = numberCounts[num] ?: 0
                        val isBig = num in 5..9
                        val col = if (isBig) BigColor else SmallColor

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = count.toString(),
                                color = if (count == maxCount) SecondaryNeon else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(col.copy(alpha = 0.2f))
                                    .border(1.dp, col.copy(alpha = 0.6f), RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = num.toString(),
                                    color = col,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
