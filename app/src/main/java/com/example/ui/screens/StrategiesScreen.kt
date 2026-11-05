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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AlgorithmType
import com.example.model.AppLanguage
import com.example.model.StrategyRisk
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.SecondaryNeon
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun StrategiesScreen(
    activeAlgorithm: AlgorithmType,
    activeRiskStrategy: StrategyRisk,
    language: AppLanguage,
    onSelectAlgorithm: (AlgorithmType) -> Unit,
    onSelectRiskStrategy: (StrategyRisk) -> Unit,
    onResetBalance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Section: AI Prediction Engine
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = PrimaryNeon,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == AppLanguage.BENGALI) "এআই প্রেডিকশন অ্যালগরিদম" else "AI PREDICTION ALGORITHMS",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )
        }

        Text(
            text = if (language == AppLanguage.BENGALI)
                "পরবর্তী বিগ / স্মল প্রেডিকশনের জন্য সক্রিয় অ্যালগরিদম নির্ধারণ করুন:"
            else
                "Choose the active mathematical pattern engine used to forecast next rounds:",
            color = TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        AlgorithmType.values().forEach { algo ->
            val isSelected = algo == activeAlgorithm
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) DarkCard else DarkSurface)
                    .border(
                        1.5.dp,
                        if (isSelected) PrimaryNeon else DarkCardBorder,
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onSelectAlgorithm(algo) }
                    .padding(14.dp)
                    .testTag("algo_select_${algo.name}")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.BENGALI) algo.titleBn else algo.title,
                                color = if (isSelected) PrimaryNeon else TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (algo == AlgorithmType.AI_ENSEMBLE) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SecondaryNeon.copy(alpha = 0.2f))
                                        .border(1.dp, SecondaryNeon, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "RECOMMENDED",
                                        color = SecondaryNeon,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (language == AppLanguage.BENGALI) algo.descriptionBn else algo.description,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(PrimaryNeon),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Risk Management Multipliers
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = SecondaryNeon,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == AppLanguage.BENGALI) "ঝুঁকি ও মাল্টিপ্লায়ার কৌশল" else "RISK & STAKE MULTIPLIERS",
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
            )
        }

        Text(
            text = if (language == AppLanguage.BENGALI)
                "পরাজয়ের পর কীভাবে স্টেক বাড়ানো বা কমানো হবে তার কৌশল:"
            else
                "Select progression formula for stake multipliers across rounds:",
            color = TextMuted,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        StrategyRisk.values().forEach { strat ->
            val isSelected = strat == activeRiskStrategy
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isSelected) DarkCard else DarkSurface)
                    .border(
                        1.5.dp,
                        if (isSelected) SecondaryNeon else DarkCardBorder,
                        RoundedCornerShape(14.dp)
                    )
                    .clickable { onSelectRiskStrategy(strat) }
                    .padding(14.dp)
                    .testTag("strat_select_${strat.name}")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.BENGALI) strat.titleBn else strat.title,
                            color = if (isSelected) SecondaryNeon else TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = strat.description,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(SecondaryNeon),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Demo Balance Reset Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.BENGALI) "ডেমো ব্যালেন্স রিসেট" else "Reset Demo Wallet",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (language == AppLanguage.BENGALI) "ব্যালেন্স পুনরায় $1,000 এ সেট করুন" else "Restore default $1,000.00 bankroll",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onResetBalance,
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("reset_balance_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Reset",
                        tint = SecondaryNeon,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == AppLanguage.BENGALI) "রিসেট" else "Reset",
                        color = SecondaryNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
