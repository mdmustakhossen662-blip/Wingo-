package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import com.example.model.AppLanguage
import com.example.model.ColorType
import com.example.model.OutcomeType
import com.example.ui.theme.BigColor
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GreenOutcome
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.RedOutcome
import com.example.ui.theme.SmallColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletOutcome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualResultDialog(
    periodId: String,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSelectNumber: (Int) -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, DarkCardBorder, RoundedCornerShape(24.dp)),
            color = DarkCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.BENGALI) "প্রকৃত ফলাফল লিখুন" else "Enter Actual Result",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${if (language == AppLanguage.BENGALI) "রাউন্ড: " else "Period: "}$periodId",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("dialog_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (language == AppLanguage.BENGALI)
                        "আপনার অ্যাপে আসা ০ থেকে ৯ এর মধ্যে যেকোনো সংখ্যা নির্বাচন করুন:"
                    else
                        "Select the actual drawn winning number (0 - 9) to resolve prediction:",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Small Numbers: 0, 1, 2, 3, 4
                Text(
                    text = if (language == AppLanguage.BENGALI) "স্মল সংখ্যা (০ - ৪):" else "SMALL (0 - 4):",
                    color = SmallColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    (0..4).forEach { num ->
                        val color = ColorType.fromNumber(num)
                        val numColor = when (color) {
                            ColorType.VIOLET -> VioletOutcome
                            ColorType.GREEN -> GreenOutcome
                            ColorType.RED -> RedOutcome
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurface)
                                .border(1.5.dp, SmallColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .clickable { onSelectNumber(num) }
                                .testTag("num_btn_$num"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = num.toString(),
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(numColor)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Big Numbers: 5, 6, 7, 8, 9
                Text(
                    text = if (language == AppLanguage.BENGALI) "বিগ সংখ্যা (৫ - ৯):" else "BIG (5 - 9):",
                    color = BigColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    (5..9).forEach { num ->
                        val color = ColorType.fromNumber(num)
                        val numColor = when (color) {
                            ColorType.VIOLET -> VioletOutcome
                            ColorType.GREEN -> GreenOutcome
                            ColorType.RED -> RedOutcome
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(DarkSurface)
                                .border(1.5.dp, BigColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .clickable { onSelectNumber(num) }
                                .testTag("num_btn_$num"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = num.toString(),
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(numColor)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
