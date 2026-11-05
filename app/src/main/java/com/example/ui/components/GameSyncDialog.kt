package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.ColorType
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
import com.example.ui.theme.WinGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSyncDialog(
    currentPeriodId: String,
    currentSecondsRemaining: Int,
    liveApiUrl: String,
    isLiveApiConnected: Boolean,
    isApiPolling: Boolean,
    apiSyncMessage: String,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSyncGame: (periodId: String, secondsRemaining: Int, pastNumbers: List<Int>) -> Unit,
    onConnectLiveApi: (url: String) -> Unit,
    onDisconnectLiveApi: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Quick Sync, 1: Live API URL

    // State for Quick Sync
    var inputPeriod by remember { mutableStateOf(currentPeriodId) }
    var inputSeconds by remember { mutableStateOf(currentSecondsRemaining.toString()) }
    val selectedPastNumbers = remember { mutableStateListOf<Int>() }

    // State for API URL
    var inputApiUrl by remember { mutableStateOf(liveApiUrl) }

    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, PrimaryNeon.copy(alpha = 0.5f), RoundedCornerShape(24.dp)),
            color = DarkCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(PrimaryNeon.copy(alpha = 0.15f))
                                .border(1.dp, PrimaryNeon, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = "Sync",
                                tint = PrimaryNeon,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "গেম ও API সিঙ্ক্রোনাইজ" else "Game & API Sync",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (language == AppLanguage.BENGALI) "আপনার লাইভ গেমের সাথে মিলান" else "Sync with live game results",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tab Switcher (Quick Game Sync vs Live API URL)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurface)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == 0) PrimaryNeon else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.BENGALI) "১. সরাসরি গেম মিলান" else "1. Direct Game Match",
                            color = if (selectedTab == 0) Color.Black else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (selectedTab == 1) PrimaryNeon else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.BENGALI) "২. লাইভ API URL" else "2. Live API URL",
                            color = if (selectedTab == 1) Color.Black else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedTab == 0) {
                    // TAB 0: Direct Match (Period ID, Seconds, Past Numbers)
                    Text(
                        text = if (language == AppLanguage.BENGALI)
                            "আপনার অ্যাপের চলমান রাউন্ড নম্বর (Period ID) দিন:"
                        else
                            "Enter the active Period ID from your game:",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = inputPeriod,
                        onValueChange = { inputPeriod = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sync_period_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        placeholder = { Text("e.g. 2026110500155", color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.BENGALI)
                            "আপনার স্ক্রিনে বাকি সময় (সেকেন্ড):"
                        else
                            "Remaining seconds on your screen:",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = inputSeconds,
                        onValueChange = { inputSeconds = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sync_seconds_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        placeholder = { Text("e.g. 25", color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.BENGALI)
                            "বিগত কয়েকটি আসল সংখ্যা নির্বাচন করুন (ঐচ্ছিক):"
                        else
                            "Select last winning numbers to calibrate AI (Optional):",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    // Number selector chips 0-9
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        (0..4).forEach { num ->
                            NumberChip(
                                num = num,
                                isSelected = selectedPastNumbers.contains(num),
                                onToggle = {
                                    if (selectedPastNumbers.contains(num)) {
                                        selectedPastNumbers.remove(num)
                                    } else {
                                        selectedPastNumbers.add(num)
                                    }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        (5..9).forEach { num ->
                            NumberChip(
                                num = num,
                                isSelected = selectedPastNumbers.contains(num),
                                onToggle = {
                                    if (selectedPastNumbers.contains(num)) {
                                        selectedPastNumbers.remove(num)
                                    } else {
                                        selectedPastNumbers.add(num)
                                    }
                                }
                            )
                        }
                    }

                    if (selectedPastNumbers.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "নির্বাচিত ক্রম: " else "Selected: ",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Text(
                                text = selectedPastNumbers.joinToString(", "),
                                color = SecondaryNeon,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Clear",
                                color = Color.Red.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                modifier = Modifier.clickable { selectedPastNumbers.clear() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val sec = inputSeconds.toIntOrNull() ?: 30
                            onSyncGame(inputPeriod, sec, selectedPastNumbers.toList())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_sync_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Sync",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.BENGALI) "এখনই সিঙ্ক ও প্রেডিক্ট করুন" else "Apply Sync & Predict",
                            color = Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                } else {
                    // TAB 1: Live API URL
                    Text(
                        text = if (language == AppLanguage.BENGALI)
                            "আপনার গেম সার্ভার বা উইঙ্গো রেজাল্ট API লিঙ্ক দিন:"
                        else
                            "Enter live JSON API URL for your game platform:",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = inputApiUrl,
                        onValueChange = { inputApiUrl = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("api_url_input"),
                        singleLine = false,
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNeon,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        placeholder = { Text("https://example.com/api/wingo/history", color = TextMuted, fontSize = 11.sp) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // API Status Box
                    if (apiSyncMessage.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurface)
                                .border(1.dp, if (isLiveApiConnected) WinGreen else DarkCardBorder, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isApiPolling) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        color = PrimaryNeon,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                } else if (isLiveApiConnected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Connected",
                                        tint = WinGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text(
                                    text = apiSyncMessage,
                                    color = if (isLiveApiConnected) WinGreen else TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onConnectLiveApi(inputApiUrl) },
                            enabled = !isApiPolling,
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNeon),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("connect_api_button")
                        ) {
                            Text(
                                text = if (language == AppLanguage.BENGALI) "কানেক্ট ও সিঙ্ক" else "Connect & Sync",
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (isLiveApiConnected) {
                            Button(
                                onClick = onDisconnectLiveApi,
                                colors = ButtonDefaults.buttonColors(containerColor = DarkSurface),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(0.8f)
                            ) {
                                Text(
                                    text = "Disconnect",
                                    color = Color.Red.copy(alpha = 0.9f),
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

@Composable
private fun NumberChip(
    num: Int,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val isBig = num in 5..9
    val col = if (isBig) BigColor else SmallColor
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) col else DarkSurface)
            .border(
                1.5.dp,
                if (isSelected) col else DarkCardBorder,
                RoundedCornerShape(10.dp)
            )
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = num.toString(),
            color = if (isSelected) Color.Black else TextPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black
        )
    }
}
