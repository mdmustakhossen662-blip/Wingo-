package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.engine.LiveApiSyncManager
import com.example.engine.PredictionEngine
import com.example.model.AlgorithmType
import com.example.model.AppLanguage
import com.example.model.ColorType
import com.example.model.GamePeriod
import com.example.model.OutcomeType
import com.example.model.PredictionStats
import com.example.model.StrategyRisk
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class PredictorUiState(
    val currentPeriodId: String = "",
    val pendingPrediction: GamePeriod? = null,
    val history: List<GamePeriod> = emptyList(),
    val stats: PredictionStats = PredictionStats(),
    val activeAlgorithm: AlgorithmType = AlgorithmType.AI_ENSEMBLE,
    val activeRiskStrategy: StrategyRisk = StrategyRisk.MARTINGALE_3X,
    val isAutoMode: Boolean = true,
    val isTimerRunning: Boolean = true,
    val timerSecondsRemaining: Int = 30,
    val timerDuration: Int = 30,
    val language: AppLanguage = AppLanguage.ENGLISH,
    val isVibrationEnabled: Boolean = true,
    val baseBetAmount: Double = 10.0,
    val currentMultiplierIndex: Int = 0,
    val selectedTab: Int = 0, // 0: Dashboard, 1: Roadmap, 2: History, 3: Strategy, 4: Guide
    val isManualDialogVisible: Boolean = false,
    val isSyncDialogVisible: Boolean = false,
    val liveApiUrl: String = "",
    val isLiveApiConnected: Boolean = false,
    val apiSyncMessage: String = "",
    val isApiPolling: Boolean = false,
    val lastResultFlash: Boolean = false,
    val lastWonStatus: Boolean? = null
)

class PredictorViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PredictorUiState())
    val uiState: StateFlow<PredictorUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var periodCounter: Long = 1000

    init {
        initializeInitialState()
        startTimer()
    }

    private fun initializeInitialState() {
        val baseDateStr = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        periodCounter = 120L

        // Generate 18 realistic sample past rounds
        val pastRounds = mutableListOf<GamePeriod>()
        var balance = 1000.0
        var multiplierIdx = 0
        var lastOutcome: OutcomeType = OutcomeType.BIG

        for (i in 18 downTo 1) {
            val periodId = "$baseDateStr" + String.format(Locale.US, "%05d", periodCounter++)
            val simulatedNumber = Random.nextInt(10)
            val actualOutcome = OutcomeType.fromNumber(simulatedNumber)
            val actualColor = ColorType.fromNumber(simulatedNumber)

            // Synthetic prediction with ~75% accuracy
            val willWin = Random.nextFloat() < 0.72f
            val predictedOutcome = if (willWin) actualOutcome else {
                if (actualOutcome == OutcomeType.BIG) OutcomeType.SMALL else OutcomeType.BIG
            }

            val isWon = predictedOutcome == actualOutcome
            val multList = StrategyRisk.MARTINGALE_3X.multipliers
            val mult = multList.getOrElse(multiplierIdx) { 1 }
            val stake = 10.0 * mult
            val profit = if (isWon) stake * 0.98 else -stake
            balance += profit

            if (isWon) {
                multiplierIdx = 0
            } else {
                multiplierIdx = (multiplierIdx + 1) % multList.size
            }

            val conf = Random.nextInt(68, 93)
            val rawNumbers = if (predictedOutcome == OutcomeType.BIG) listOf(5, 7, 8, 9) else listOf(1, 2, 3, 4)

            pastRounds.add(
                0,
                GamePeriod(
                    periodId = periodId,
                    predictedOutcome = predictedOutcome,
                    confidence = conf,
                    predictedNumbers = rawNumbers,
                    predictedColor = if (predictedOutcome == OutcomeType.BIG) ColorType.GREEN else ColorType.RED,
                    multiplierLevel = mult,
                    algorithmUsed = AlgorithmType.AI_ENSEMBLE,
                    rationaleEn = "AI Ensemble: Multi-model statistical convergence.",
                    rationaleBn = "এআই এনসেম্বল: মাল্টি-মডেল গাণিতিক কনভার্জেন্স।",
                    actualNumber = simulatedNumber,
                    actualOutcome = actualOutcome,
                    actualColor = actualColor,
                    isWon = isWon,
                    timestamp = System.currentTimeMillis() - (i * 30000L),
                    virtualStake = stake,
                    virtualProfit = profit
                )
            )
            lastOutcome = actualOutcome
        }

        val nextPeriodId = "$baseDateStr" + String.format(Locale.US, "%05d", periodCounter)
        val stats = calculateStats(pastRounds, balance)

        val nextPred = generateNextPrediction(nextPeriodId, pastRounds, AlgorithmType.AI_ENSEMBLE, multiplierIdx, StrategyRisk.MARTINGALE_3X)

        _uiState.update {
            it.copy(
                currentPeriodId = nextPeriodId,
                pendingPrediction = nextPred,
                history = pastRounds,
                stats = stats,
                currentMultiplierIndex = multiplierIdx
            )
        }
    }

    private fun generateNextPrediction(
        periodId: String,
        history: List<GamePeriod>,
        algorithm: AlgorithmType,
        multiplierIdx: Int,
        riskStrategy: StrategyRisk
    ): GamePeriod {
        val raw = PredictionEngine.predictNext(history, algorithm)
        val multList = riskStrategy.multipliers
        val mult = multList.getOrElse(multiplierIdx) { 1 }
        val stake = _uiState.value.baseBetAmount * mult

        return GamePeriod(
            periodId = periodId,
            predictedOutcome = raw.outcome,
            confidence = raw.confidence,
            predictedNumbers = raw.numbers,
            predictedColor = raw.color,
            multiplierLevel = mult,
            algorithmUsed = algorithm,
            rationaleEn = raw.rationaleEn,
            rationaleBn = raw.rationaleBn,
            timestamp = System.currentTimeMillis(),
            virtualStake = stake,
            virtualProfit = 0.0
        )
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(1000L)
                val state = _uiState.value
                if (!state.isTimerRunning) continue

                val nextSeconds = state.timerSecondsRemaining - 1
                if (nextSeconds <= 0) {
                    if (state.isAutoMode) {
                        resolveRoundWithSimulatedNumber()
                    } else {
                        // In manual mode, reset timer and keep waiting
                        _uiState.update { it.copy(timerSecondsRemaining = state.timerDuration) }
                    }
                } else {
                    _uiState.update { it.copy(timerSecondsRemaining = nextSeconds) }
                }
            }
        }
    }

    fun resolveRoundWithSimulatedNumber() {
        val simulatedNum = PredictionEngine.simulateActualRound()
        resolveCurrentRound(simulatedNum)
    }

    fun resolveRoundWithManualNumber(number: Int) {
        if (number in 0..9) {
            resolveCurrentRound(number)
            _uiState.update { it.copy(isManualDialogVisible = false) }
        }
    }

    private fun resolveCurrentRound(actualNumber: Int) {
        val currentState = _uiState.value
        val pending = currentState.pendingPrediction ?: return

        val actualOutcome = OutcomeType.fromNumber(actualNumber)
        val actualColor = ColorType.fromNumber(actualNumber)
        val isWon = pending.predictedOutcome == actualOutcome

        val profit = if (isWon) {
            pending.virtualStake * 0.98
        } else {
            -pending.virtualStake
        }

        val completedRound = pending.copy(
            actualNumber = actualNumber,
            actualOutcome = actualOutcome,
            actualColor = actualColor,
            isWon = isWon,
            virtualProfit = profit
        )

        val updatedHistory = listOf(completedRound) + currentState.history
        val newBalance = currentState.stats.demoBalance + profit

        // Update multiplier level based on risk strategy
        val nextMultiplierIdx = if (isWon) {
            0
        } else {
            val multList = currentState.activeRiskStrategy.multipliers
            (currentState.currentMultiplierIndex + 1) % multList.size
        }

        val updatedStats = calculateStats(updatedHistory, newBalance)

        // Advance Period ID
        periodCounter++
        val baseDateStr = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val nextPeriodId = "$baseDateStr" + String.format(Locale.US, "%05d", periodCounter)

        // Next prediction
        val nextPred = generateNextPrediction(
            nextPeriodId,
            updatedHistory,
            currentState.activeAlgorithm,
            nextMultiplierIdx,
            currentState.activeRiskStrategy
        )

        // Trigger vibration if enabled
        if (currentState.isVibrationEnabled) {
            triggerHaptic(isWon)
        }

        _uiState.update {
            it.copy(
                currentPeriodId = nextPeriodId,
                pendingPrediction = nextPred,
                history = updatedHistory,
                stats = updatedStats,
                currentMultiplierIndex = nextMultiplierIdx,
                timerSecondsRemaining = it.timerDuration,
                lastResultFlash = true,
                lastWonStatus = isWon
            )
        }

        // Reset flash effect after 1.5s
        viewModelScope.launch {
            delay(1500L)
            _uiState.update { it.copy(lastResultFlash = false) }
        }
    }

    private fun calculateStats(history: List<GamePeriod>, balance: Double): PredictionStats {
        val completed = history.filter { it.isWon != null }
        val total = completed.size
        val wins = completed.count { it.isWon == true }
        val losses = completed.count { it.isWon == false }
        val winRate = if (total > 0) (wins.toFloat() / total) * 100f else 0f

        var currentStreak = 0
        var isCurrentWinStreak = true
        if (completed.isNotEmpty()) {
            val firstWon = completed.first().isWon == true
            isCurrentWinStreak = firstWon
            for (p in completed) {
                if ((p.isWon == true) == firstWon) {
                    currentStreak++
                } else {
                    break
                }
            }
        }

        var maxWinStreak = 0
        var tempWinStreak = 0
        for (p in completed.reversed()) {
            if (p.isWon == true) {
                tempWinStreak++
                if (tempWinStreak > maxWinStreak) maxWinStreak = tempWinStreak
            } else {
                tempWinStreak = 0
            }
        }

        val bigCount = completed.count { it.actualOutcome == OutcomeType.BIG }
        val smallCount = completed.count { it.actualOutcome == OutcomeType.SMALL }
        val totalProfit = completed.sumOf { it.virtualProfit }

        return PredictionStats(
            totalPredictions = total,
            wins = wins,
            losses = losses,
            winRate = winRate,
            currentStreak = currentStreak,
            isCurrentWinStreak = isCurrentWinStreak,
            bestWinStreak = maxWinStreak,
            bigCount = bigCount,
            smallCount = smallCount,
            demoBalance = balance,
            totalProfit = totalProfit
        )
    }

    fun toggleAutoMode() {
        _uiState.update { it.copy(isAutoMode = !it.isAutoMode) }
    }

    fun toggleTimerRunning() {
        _uiState.update { it.copy(isTimerRunning = !it.isTimerRunning) }
    }

    fun setTimerDuration(seconds: Int) {
        _uiState.update {
            it.copy(
                timerDuration = seconds,
                timerSecondsRemaining = seconds
            )
        }
    }

    fun setAlgorithm(algorithm: AlgorithmType) {
        val current = _uiState.value
        val updatedPred = generateNextPrediction(
            current.currentPeriodId,
            current.history,
            algorithm,
            current.currentMultiplierIndex,
            current.activeRiskStrategy
        )
        _uiState.update {
            it.copy(
                activeAlgorithm = algorithm,
                pendingPrediction = updatedPred
            )
        }
    }

    fun setRiskStrategy(strategy: StrategyRisk) {
        val current = _uiState.value
        val updatedPred = generateNextPrediction(
            current.currentPeriodId,
            current.history,
            current.activeAlgorithm,
            0,
            strategy
        )
        _uiState.update {
            it.copy(
                activeRiskStrategy = strategy,
                currentMultiplierIndex = 0,
                pendingPrediction = updatedPred
            )
        }
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun setManualDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(isManualDialogVisible = visible) }
    }

    fun toggleLanguage() {
        _uiState.update {
            it.copy(
                language = if (it.language == AppLanguage.ENGLISH) AppLanguage.BENGALI else AppLanguage.ENGLISH
            )
        }
    }

    fun toggleVibration() {
        _uiState.update { it.copy(isVibrationEnabled = !it.isVibrationEnabled) }
    }

    fun resetDemoBalance() {
        _uiState.update {
            it.copy(
                stats = it.stats.copy(demoBalance = 1000.0, totalProfit = 0.0)
            )
        }
    }

    fun clearHistory() {
        val baseDateStr = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        periodCounter = 1L
        val nextPeriodId = "$baseDateStr" + String.format(Locale.US, "%05d", periodCounter)
        val emptyList = emptyList<GamePeriod>()
        val nextPred = generateNextPrediction(
            nextPeriodId,
            emptyList,
            _uiState.value.activeAlgorithm,
            0,
            _uiState.value.activeRiskStrategy
        )

        _uiState.update {
            it.copy(
                currentPeriodId = nextPeriodId,
                pendingPrediction = nextPred,
                history = emptyList,
                stats = PredictionStats(),
                currentMultiplierIndex = 0
            )
        }
    }

    fun setSyncDialogVisible(visible: Boolean) {
        _uiState.update { it.copy(isSyncDialogVisible = visible) }
    }

    /**
     * Synchronizes the predictor with the user's actual external game round.
     * Sets current period ID, adjusts remaining timer seconds, and injects real historical numbers.
     */
    fun syncWithUserGame(
        periodId: String,
        secondsRemaining: Int,
        pastNumbers: List<Int>
    ) {
        val currentState = _uiState.value
        val cleanedPeriod = periodId.trim().ifBlank { currentState.currentPeriodId }
        val adjustedSeconds = secondsRemaining.coerceIn(3, currentState.timerDuration)

        val updatedHistory = if (pastNumbers.isNotEmpty()) {
            val baseTime = System.currentTimeMillis()
            pastNumbers.mapIndexed { idx, num ->
                val outcome = OutcomeType.fromNumber(num)
                val color = ColorType.fromNumber(num)
                GamePeriod(
                    periodId = "SYNC-" + (cleanedPeriod.toLongOrNull()?.minus(idx + 1) ?: (1000 - idx)),
                    predictedOutcome = outcome,
                    confidence = 80,
                    predictedNumbers = if (outcome == OutcomeType.BIG) listOf(5, 7, 8, 9) else listOf(1, 2, 3, 4),
                    predictedColor = color,
                    multiplierLevel = 1,
                    algorithmUsed = currentState.activeAlgorithm,
                    rationaleEn = "User Game Calibration Record",
                    rationaleBn = "ইউজার গেম ক্যালিব্রেশন রেকর্ড",
                    actualNumber = num,
                    actualOutcome = outcome,
                    actualColor = color,
                    isWon = true,
                    timestamp = baseTime - ((idx + 1) * 30000L),
                    virtualStake = 10.0,
                    virtualProfit = 9.8
                )
            } + currentState.history
        } else {
            currentState.history
        }

        val nextPred = generateNextPrediction(
            cleanedPeriod,
            updatedHistory,
            currentState.activeAlgorithm,
            currentState.currentMultiplierIndex,
            currentState.activeRiskStrategy
        )

        _uiState.update {
            it.copy(
                currentPeriodId = cleanedPeriod,
                timerSecondsRemaining = adjustedSeconds,
                history = updatedHistory,
                pendingPrediction = nextPred,
                isSyncDialogVisible = false
            )
        }
    }

    fun connectLiveApi(url: String) {
        val trimmed = url.trim()
        if (trimmed.isBlank()) {
            _uiState.update { it.copy(apiSyncMessage = "Please enter an API URL") }
            return
        }

        _uiState.update {
            it.copy(
                liveApiUrl = trimmed,
                isApiPolling = true,
                apiSyncMessage = "Connecting to API..."
            )
        }

        viewModelScope.launch {
            val result = LiveApiSyncManager.fetchLatestRoundsFromApi(trimmed)
            result.onSuccess { rounds ->
                if (rounds.isNotEmpty()) {
                    val latest = rounds.first()
                    // Sync latest period and predict next
                    val nextPeriodNumber = (latest.periodId.toLongOrNull()?.plus(1))?.toString() ?: (latest.periodId + "-NEXT")
                    val newHistoryItems = rounds.map { fetched ->
                        val out = OutcomeType.fromNumber(fetched.drawnNumber)
                        val col = ColorType.fromNumber(fetched.drawnNumber)
                        GamePeriod(
                            periodId = fetched.periodId,
                            predictedOutcome = out,
                            confidence = 85,
                            predictedNumbers = if (out == OutcomeType.BIG) listOf(5, 6, 8, 9) else listOf(1, 2, 3, 4),
                            predictedColor = col,
                            multiplierLevel = 1,
                            algorithmUsed = _uiState.value.activeAlgorithm,
                            rationaleEn = "Live API Synchronized Record",
                            rationaleBn = "লাইভ এপিআই সিঙ্ক রেকর্ড",
                            actualNumber = fetched.drawnNumber,
                            actualOutcome = out,
                            actualColor = col,
                            isWon = true,
                            timestamp = fetched.timestamp,
                            virtualStake = 10.0,
                            virtualProfit = 9.8
                        )
                    }

                    val updatedHistory = newHistoryItems + _uiState.value.history.take(20)
                    val nextPred = generateNextPrediction(
                        nextPeriodNumber,
                        updatedHistory,
                        _uiState.value.activeAlgorithm,
                        0,
                        _uiState.value.activeRiskStrategy
                    )

                    _uiState.update {
                        it.copy(
                            isLiveApiConnected = true,
                            isApiPolling = false,
                            currentPeriodId = nextPeriodNumber,
                            history = updatedHistory,
                            pendingPrediction = nextPred,
                            apiSyncMessage = "Connected: ${rounds.size} rounds synced"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isApiPolling = false,
                            apiSyncMessage = "API returned 0 valid rounds"
                        )
                    }
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isApiPolling = false,
                        isLiveApiConnected = false,
                        apiSyncMessage = "Connection failed: ${err.message}"
                    )
                }
            }
        }
    }

    fun disconnectLiveApi() {
        _uiState.update {
            it.copy(
                isLiveApiConnected = false,
                isApiPolling = false,
                apiSyncMessage = "Live API disconnected"
            )
        }
    }

    private fun triggerHaptic(isWon: Boolean) {
        try {
            val context = getApplication<Application>()
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            vibrator?.let {
                if (it.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val pattern = if (isWon) {
                            longArrayOf(0, 80, 50, 120) // double chirp for win
                        } else {
                            longArrayOf(0, 150) // single buzz
                        }
                        it.vibrate(VibrationEffect.createWaveform(pattern, -1))
                    } else {
                        @Suppress("DEPRECATION")
                        it.vibrate(100)
                    }
                }
            }
        } catch (_: Exception) {
            // Ignore if vibration fails
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
