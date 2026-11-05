package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.GamePeriod
import com.example.ui.components.RoadmapView
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun RoadmapScreen(
    history: List<GamePeriod>,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = if (language == AppLanguage.BENGALI) "ট্রেন্ড রোডম্যাপ ও প্যাটার্ন অ্যানালাইসিস" else "TREND ROADMAP & PATTERNS",
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = if (language == AppLanguage.BENGALI)
                "বিড প্লেট এবং বিগ রোড কলামের মাধ্যমে ড্রাগন স্ট্রাইক এবং অল্টারনেটিং প্যাটার্ন পর্যবেক্ষণ করুন"
            else
                "Visualize historical streaks, dragon runs and alternating patterns like professional baccarat/wingo roadmaps",
            color = TextSecondary,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoadmapView(
            history = history,
            language = language
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
