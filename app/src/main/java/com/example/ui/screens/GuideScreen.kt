package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.theme.BigColor
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.GreenOutcome
import com.example.ui.theme.PrimaryNeon
import com.example.ui.theme.RedOutcome
import com.example.ui.theme.SecondaryNeon
import com.example.ui.theme.SmallColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VioletOutcome

@Composable
fun GuideScreen(
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.MenuBook,
                contentDescription = null,
                tint = PrimaryNeon,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (language == AppLanguage.BENGALI) "বিগ / স্মল প্রেডিকশন গাইড" else "BIG / SMALL PREDICTION GUIDE",
                color = TextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Basic Rules Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.BENGALI) "১. বিগ ও স্মল সংখ্যার নিয়ম" else "1. Big & Small Rulebook",
                    color = PrimaryNeon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Small card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurface)
                            .border(1.dp, SmallColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "স্মল (SMALL)" else "SMALL (0 - 4)",
                                color = SmallColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "0, 1, 2, 3, 4",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Big card
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(DarkSurface)
                            .border(1.dp, BigColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "বিগ (BIG)" else "BIG (5 - 9)",
                                color = BigColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "5, 6, 7, 8, 9",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Colors breakdown
                Text(
                    text = if (language == AppLanguage.BENGALI) "রং অনুযায়ী বিভাজন:" else "Color Associations:",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ColorBadge("GREEN", "1, 3, 7, 9", GreenOutcome)
                    ColorBadge("RED", "2, 4, 6, 8", RedOutcome)
                    ColorBadge("VIOLET", "0, 5", VioletOutcome)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // How Auto Prediction Works Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.BENGALI) "২. অটো প্রেডিকশন কীভাবে কাজ করে?" else "2. How Auto Prediction Works",
                    color = SecondaryNeon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (language == AppLanguage.BENGALI)
                        "• কাউন্টডাউন টাইমার প্রতি রাউন্ডের জন্য সময় নির্ধারণ করে (১৫ সে, ৩০ সে বা ৬০ সে)।\n" +
                        "• অটো মোড চালু থাকলে সময় শেষ হওয়া মাত্র সিস্টেম অটোমেটিক পরবর্তী রাউন্ডের জন্য এআই বিশ্লেষণ করে বিগ বা স্মল প্রেডিকশন দেয়।\n" +
                        "• আপনি যেকোনো সময় 'ম্যানুয়াল ইনপুট' বাটনে চাপ দিয়ে ০-৯ এর মধ্যে আসল ফলাফল এন্ট্রি করতে পারবেন এবং সাথে সাথে পরবর্তী প্রেডিকশন পেয়ে যাবেন।\n" +
                        "• নির্ভরযোগ্যতার হার (Confidence %) দেখায় কোন দিকে বেশি সম্ভাবনা রয়েছে।"
                    else
                        "• The timer counts down for each period (15s, 30s, or 60s).\n" +
                        "• When Auto Mode is active, the system automatically simulates and analyzes mathematical trend continuations to yield a real-time BIG or SMALL signal.\n" +
                        "• You can tap 'ENTER' at any moment to input the actual live draw number (0-9) from your game for instant tailored prediction.\n" +
                        "• The confidence meter indicates the probability level of the active algorithmic consensus.",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Algorithms & Strategies
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.BENGALI) "৩. অ্যালগরিদম কৌশলসমূহ" else "3. Mathematical Strategy Models",
                    color = PrimaryNeon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (language == AppLanguage.BENGALI)
                        "• ড্রাগন ট্রেন্ড: পর পর একই ফলাফল (যেমন বিগ-বিগ-বিগ) আসলে ট্রেন্ড ধরে রাখার নির্দেশ করে।\n" +
                        "• অ্যান্টি-স্ট্রাইক: ৩ বা ৪ বারের বেশি টানা একই ফল আসলে বিপরীতমুখী পরিবর্তনের পূর্বাভাস দেয়।\n" +
                        "• মারকভ চেইন: পূর্ববর্তী ফলাফলের গাণিতিক রূপান্তর সম্ভাব্যতা হিসাব করে।\n" +
                        "• মার্টিঙ্গেল ৩X: কোনো রাউন্ড হারলে পরের রাউন্ডে ৩ গুণ দিয়ে রিকভারির সুযোগ তৈরি করে (১x -> ৩x -> ৯x -> ২৭x)।"
                    else
                        "• Dragon Streak: Rides hot winning runs when identical outcomes cluster.\n" +
                        "• Anti-Streak Reversal: Triggers contrarian signals when a streak reaches 3+.\n" +
                        "• Markov Chain Matrix: Calculates conditional state transitions P(State | Previous State).\n" +
                        "• Martingale 3X: Follows progressive recovery stages on loss (1X -> 3X -> 9X -> 27X).",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 17.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Responsible Use / Disclaimer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkSurface)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = "Notice",
                    tint = SecondaryNeon,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == AppLanguage.BENGALI)
                        "দাবিত্যাগ: এই অ্যাপটি কেবল পরিসংখ্যানগত বিশ্লেষণ, প্যাটার্ন রোডম্যাপ এবং ডেমো সিমুলেশনের জন্য তৈরি। কোনো প্রকার আর্থিক নিশ্চয়তা প্রদান করা হয় না।"
                    else
                        "Disclaimer: This application is designed solely as a statistical trend analysis and simulation tool. Predictions are mathematical probabilities and do not guarantee outcomes.",
                    color = TextMuted,
                    fontSize = 10.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ColorBadge(name: String, numbers: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = name, color = color, fontSize = 9.sp, fontWeight = FontWeight.Black)
            Text(text = numbers, color = TextPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}
