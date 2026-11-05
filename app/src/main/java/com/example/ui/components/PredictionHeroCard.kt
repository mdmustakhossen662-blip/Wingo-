package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ColorType
import com.example.model.GamePeriod
import com.example.model.OutcomeType
import com.example.ui.theme.BigColor
import com.example.ui.theme.BigDarkColor
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GreenOutcome
import com.example.ui.theme.LossRed
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.RedOutcome
import com.example.ui.theme.SecondaryNeon
import com.example.ui.theme.SmallColor
import com.example.ui.theme.SmallDarkColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletOutcome
import com.example.ui.theme.WinGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PredictionHeroCard(
    prediction: GamePeriod?,
    language: AppLanguage,
    lastResultFlash: Boolean,
    lastWonStatus: Boolean?,
    modifier: Modifier = Modifier
) {
    if (prediction == null) return

    val isBig = prediction.predictedOutcome == OutcomeType.BIG
    val mainColor = if (isBig) BigColor else SmallColor
    val darkGradientColor = if (isBig) BigDarkColor else SmallDarkColor

    // Subtle breathing pulse animation for prediction badge
    val infiniteTransition = rememberInfiniteTransition(label = "heroPulse")
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Flash background color when round finishes
    val flashBorderColor by animateColorAsState(
        targetValue = when {
            lastResultFlash && lastWonStatus == true -> WinGreen
            lastResultFlash && lastWonStatus == false -> LossRed
            else -> mainColor.copy(alpha = 0.5f)
        },
        animationSpec = tween(300),
        label = "flashBorder"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(DarkCard)
            .border(2.dp, flashBorderColor, RoundedCornerShape(20.dp))
            .padding(18.dp)
            .testTag("prediction_hero_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Algorithm badge & Period
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "AI",
                        tint = PrimaryNeon,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.BENGALI) prediction.algorithmUsed.titleBn else prediction.algorithmUsed.title,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Confidence badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(mainColor.copy(alpha = 0.15f))
                        .border(1.dp, mainColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${prediction.confidence}% " + if (language == AppLanguage.BENGALI) "নির্ভরযোগ্যতা" else "CONFIDENCE",
                        color = mainColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Prediction Giant Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scaleAnim)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                mainColor.copy(alpha = 0.22f),
                                DarkSurface
                            )
                        )
                    )
                    .border(1.5.dp, mainColor, RoundedCornerShape(16.dp))
                    .padding(vertical = 18.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "পরবর্তী অটো প্রেডিকশন" else "NEXT PREDICTION",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // HUGE PREDICTION TEXT
                    Text(
                        text = if (language == AppLanguage.BENGALI) prediction.predictedOutcome.labelBn else prediction.predictedOutcome.label,
                        color = mainColor,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = if (isBig) {
                            if (language == AppLanguage.BENGALI) "সংখ্যার পরিসীমা: ৫, ৬, ৭, ৮, ৯" else "NUMBER RANGE: 5, 6, 7, 8, 9"
                        } else {
                            if (language == AppLanguage.BENGALI) "সংখ্যার পরিসীমা: ০, ১, ২, ৩, ৪" else "NUMBER RANGE: 0, 1, 2, 3, 4"
                        },
                        color = TextPrimary.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Confidence Progress Bar
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "এআই মডেল কনভার্জেন্স" else "AI Model Convergence",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${prediction.confidence}%",
                        color = mainColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { prediction.confidence / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = mainColor,
                    trackColor = DarkSurface,
                    strokeCap = StrokeCap.Round
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Recommended Numbers & Color Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Numbers
                Column {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "পছন্দের সংখ্যা:" else "RECOMMENDED NUMBERS:",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        prediction.predictedNumbers.forEach { num ->
                            val numColor = when (num) {
                                0, 5 -> VioletOutcome
                                1, 3, 7, 9 -> GreenOutcome
                                else -> RedOutcome
                            }
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(numColor)
                                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = num.toString(),
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }

                // Color & Multiplier Level
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "ঝুঁকি ব্যবস্থাপনা:" else "RISK MULTIPLIER:",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SecondaryNeon.copy(alpha = 0.18f))
                            .border(1.dp, SecondaryNeon.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${prediction.multiplierLevel}X ($${String.format(java.util.Locale.US, "%.0f", prediction.virtualStake)})",
                            color = SecondaryNeon,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Rationale / Reasoning
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurface)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(10.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Rationale",
                        tint = PrimaryNeon,
                        modifier = Modifier
                            .size(15.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (language == AppLanguage.BENGALI) prediction.rationaleBn else prediction.rationaleEn,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
