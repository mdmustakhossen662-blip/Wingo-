package com.example.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
fun HeaderBar(
    demoBalance: Double,
    isAutoMode: Boolean,
    language: AppLanguage,
    isVibrationEnabled: Boolean,
    onToggleLanguage: () -> Unit,
    onToggleVibration: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = DarkSurface,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title and pulse status
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(PrimaryNeon, SecondaryNeon)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AI",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "বিগ/স্মল প্রেডিক্টর" else "BigSmall Predictor",
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Pulsing online dot
                            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                            val pulseAlpha by infiniteTransition.animateColor(
                                initialValue = if (isAutoMode) WinGreen else SecondaryNeon,
                                targetValue = (if (isAutoMode) WinGreen else SecondaryNeon).copy(alpha = 0.3f),
                                animationSpec = infiniteRepeatable(
                                    animation = tween(800),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "dot"
                            )

                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(pulseAlpha)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (isAutoMode) {
                                    if (language == AppLanguage.BENGALI) "অটো সিস্টেম সক্রিয়" else "AUTO ENGINE LIVE"
                                } else {
                                    if (language == AppLanguage.BENGALI) "ম্যানুয়াল মোড" else "MANUAL MODE"
                                },
                                color = if (isAutoMode) WinGreen else SecondaryNeon,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Balance & Quick Action Pills
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Demo Balance Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(DarkCard)
                            .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "ডেমো ব্যালেন্স" else "DEMO WALLET",
                                color = TextMuted,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$${String.format(java.util.Locale.US, "%,.2f", demoBalance)}",
                                color = SecondaryNeon,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Language Toggle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DarkCard)
                            .border(1.dp, DarkCardBorder, CircleShape)
                            .clickable { onToggleLanguage() }
                            .testTag("lang_toggle_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.ENGLISH) "বাং" else "EN",
                            color = PrimaryNeon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Vibration Toggle
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isVibrationEnabled) DarkCard else DarkSurface)
                            .border(
                                1.dp,
                                if (isVibrationEnabled) PrimaryNeon.copy(alpha = 0.5f) else DarkCardBorder,
                                CircleShape
                            )
                            .clickable { onToggleVibration() }
                            .testTag("vibration_toggle_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = "Toggle Vibration",
                            tint = if (isVibrationEnabled) PrimaryNeon else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
