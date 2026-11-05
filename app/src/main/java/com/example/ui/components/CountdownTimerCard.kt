package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WinGreen

@Composable
fun CountdownTimerCard(
    periodId: String,
    secondsRemaining: Int,
    totalDuration: Int,
    isAutoMode: Boolean,
    isTimerRunning: Boolean,
    language: AppLanguage,
    onToggleAutoMode: () -> Unit,
    onToggleTimerRunning: () -> Unit,
    onSelectDuration: (Int) -> Unit,
    onSimulateNow: () -> Unit,
    onOpenManualDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (totalDuration > 0) {
        secondsRemaining.toFloat() / totalDuration.toFloat()
    } else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "timerProgress")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkCard)
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Period ID & Mode Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Period",
                        tint = PrimaryNeon,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.BENGALI) "রাউন্ড নং: " else "PERIOD: ",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = periodId,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Auto Mode Switch Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isAutoMode) PrimaryNeon.copy(alpha = 0.15f) else DarkSurface)
                        .border(
                            1.dp,
                            if (isAutoMode) PrimaryNeon else DarkCardBorder,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { onToggleAutoMode() }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .testTag("auto_mode_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isAutoMode) {
                            if (language == AppLanguage.BENGALI) "অটো মোড: চালু" else "AUTO: ON"
                        } else {
                            if (language == AppLanguage.BENGALI) "অটো মোড: বন্ধ" else "AUTO: OFF"
                        },
                        color = if (isAutoMode) PrimaryNeon else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Timer Centerpiece
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Countdown Ring
                Box(
                    modifier = Modifier.size(74.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { 1f },
                        modifier = Modifier.size(74.dp),
                        color = DarkSurface,
                        strokeWidth = 6.dp,
                    )
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(74.dp),
                        color = if (secondsRemaining <= 5) Color(0xFFEF4444) else PrimaryNeon,
                        strokeWidth = 6.dp,
                        strokeCap = StrokeCap.Round
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format(java.util.Locale.US, "%02ds", secondsRemaining),
                            color = if (secondsRemaining <= 5) Color(0xFFEF4444) else TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = if (language == AppLanguage.BENGALI) "বাকি সময়" else "LEFT",
                            color = TextMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Fast Action Buttons & Duration Selectors
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Duration Buttons (15s, 30s, 60s)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(15, 30, 60).forEach { dur ->
                            val isSelected = totalDuration == dur
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) SecondaryNeon else DarkSurface)
                                    .border(
                                        1.dp,
                                        if (isSelected) SecondaryNeon else DarkCardBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onSelectDuration(dur) }
                                    .padding(vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${dur}s",
                                    color = if (isSelected) Color.Black else TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Action buttons: Fast Sim & Manual Entry
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Fast Sim / Next
                        Box(
                            modifier = Modifier
                                .weight(1.1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryNeon)
                                .clickable { onSimulateNow() }
                                .padding(vertical = 7.dp)
                                .testTag("fast_sim_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Bolt,
                                    contentDescription = "Simulate",
                                    tint = Color.Black,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (language == AppLanguage.BENGALI) "ফাস্ট সিম" else "FAST SIM",
                                    color = Color.Black,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // Manual Entry button
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurface)
                                .border(1.dp, DarkCardBorder, RoundedCornerShape(8.dp))
                                .clickable { onOpenManualDialog() }
                                .padding(vertical = 7.dp)
                                .testTag("manual_entry_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Manual",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (language == AppLanguage.BENGALI) "ইনপুট" else "ENTER",
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
