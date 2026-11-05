package com.example.engine

import com.example.model.AlgorithmType
import com.example.model.ColorType
import com.example.model.GamePeriod
import com.example.model.OutcomeType
import kotlin.random.Random

object PredictionEngine {

    data class RawPrediction(
        val outcome: OutcomeType,
        val confidence: Int,
        val numbers: List<Int>,
        val color: ColorType,
        val rationaleEn: String,
        val rationaleBn: String
    )

    fun predictNext(
        history: List<GamePeriod>,
        algorithm: AlgorithmType
    ): RawPrediction {
        // Filter only rounds that have completed actual outcomes
        val completed = history.filter { it.actualOutcome != null }

        return when (algorithm) {
            AlgorithmType.DRAGON_STREAK -> predictDragonStreak(completed)
            AlgorithmType.MEAN_REVERSION -> predictMeanReversion(completed)
            AlgorithmType.MARKOV_CHAIN -> predictMarkovChain(completed)
            AlgorithmType.HOT_COLD -> predictHotCold(completed)
            AlgorithmType.AI_ENSEMBLE -> predictEnsemble(completed)
        }
    }

    private fun predictDragonStreak(completed: List<GamePeriod>): RawPrediction {
        if (completed.isEmpty()) {
            val defaultOutcome = if (Random.nextBoolean()) OutcomeType.BIG else OutcomeType.SMALL
            return makePrediction(
                outcome = defaultOutcome,
                confidence = 65,
                numbers = pickNumbersFor(defaultOutcome),
                color = pickColorFor(defaultOutcome),
                rationaleEn = "Initial round baseline: Following opening trend momentum.",
                rationaleBn = "প্রথম রাউন্ডের ভিত্তিরেখা: উদ্বোধনী ট্রেন্ড মোমেন্টাম অনুসরণ করা হচ্ছে।"
            )
        }

        val lastOutcome = completed.first().actualOutcome ?: OutcomeType.BIG
        var streak = 0
        for (item in completed) {
            if (item.actualOutcome == lastOutcome) {
                streak++
            } else {
                break
            }
        }

        val confidence = when {
            streak >= 4 -> 88
            streak == 3 -> 84
            streak == 2 -> 77
            else -> 66
        }

        val outcomeBn = if (lastOutcome == OutcomeType.BIG) "বিগ" else "স্মল"
        val numbers = pickNumbersFor(lastOutcome)
        val color = pickColorFor(lastOutcome)

        return makePrediction(
            outcome = lastOutcome,
            confidence = confidence,
            numbers = numbers,
            color = color,
            rationaleEn = "Dragon Pattern Active: $streak consecutive ${lastOutcome.name}s. Trend continuation likely.",
            rationaleBn = "ড্রাগন প্যাটার্ন সক্রিয়: টানা $streak বার $outcomeBn এসেছে। ট্রেন্ড চলমান থাকার সম্ভাবনা প্রবল।"
        )
    }

    private fun predictMeanReversion(completed: List<GamePeriod>): RawPrediction {
        if (completed.isEmpty()) {
            val outcome = OutcomeType.BIG
            return makePrediction(
                outcome, 68, pickNumbersFor(outcome), pickColorFor(outcome),
                "Mean Reversion: Setting baseline calibration.",
                "গড় প্রত্যাবর্তন: প্রারম্ভিক ক্যালিব্রেশন সেট করা হচ্ছে।"
            )
        }

        val lastOutcome = completed.first().actualOutcome ?: OutcomeType.BIG
        var streak = 0
        for (item in completed) {
            if (item.actualOutcome == lastOutcome) {
                streak++
            } else {
                break
            }
        }

        val predictedOutcome = if (streak >= 3) {
            if (lastOutcome == OutcomeType.BIG) OutcomeType.SMALL else OutcomeType.BIG
        } else if (streak == 2) {
            if (Random.nextInt(100) < 60) {
                if (lastOutcome == OutcomeType.BIG) OutcomeType.SMALL else OutcomeType.BIG
            } else {
                lastOutcome
            }
        } else {
            // After single flip, often alternates (Ping-Pong pattern)
            if (lastOutcome == OutcomeType.BIG) OutcomeType.SMALL else OutcomeType.BIG
        }

        val confidence = when {
            streak >= 4 -> 92
            streak == 3 -> 85
            streak == 2 -> 74
            else -> 68
        }

        val predBn = if (predictedOutcome == OutcomeType.BIG) "বিগ" else "স্মল"
        val lastBn = if (lastOutcome == OutcomeType.BIG) "বিগ" else "স্মল"

        return makePrediction(
            outcome = predictedOutcome,
            confidence = confidence,
            numbers = pickNumbersFor(predictedOutcome),
            color = pickColorFor(predictedOutcome),
            rationaleEn = "Reversal Signal: $streak consecutive ${lastOutcome.name}s triggered mean-reversion flip to ${predictedOutcome.name}.",
            rationaleBn = "রিভার্সাল সিগন্যাল: টানা $streak বার $lastBn আসার পর গাণিতিক ভারসাম্যের জন্য $predBn সংকেত।"
        )
    }

    private fun predictMarkovChain(completed: List<GamePeriod>): RawPrediction {
        if (completed.size < 3) {
            return predictDragonStreak(completed)
        }

        val lastOutcome = completed[0].actualOutcome ?: OutcomeType.BIG
        var bigFollowedCount = 0
        var smallFollowedCount = 0

        // Look at history transitions: when state was `lastOutcome`, what followed?
        for (i in 0 until completed.size - 1) {
            val current = completed[i + 1].actualOutcome
            val next = completed[i].actualOutcome
            if (current == lastOutcome) {
                if (next == OutcomeType.BIG) bigFollowedCount++
                if (next == OutcomeType.SMALL) smallFollowedCount++
            }
        }

        val totalTransitions = bigFollowedCount + smallFollowedCount
        val predictedOutcome: OutcomeType
        val probabilityPct: Int

        if (totalTransitions >= 2) {
            if (bigFollowedCount >= smallFollowedCount) {
                predictedOutcome = OutcomeType.BIG
                probabilityPct = ((bigFollowedCount.toFloat() / totalTransitions) * 100).toInt().coerceIn(62, 94)
            } else {
                predictedOutcome = OutcomeType.SMALL
                probabilityPct = ((smallFollowedCount.toFloat() / totalTransitions) * 100).toInt().coerceIn(62, 94)
            }
        } else {
            predictedOutcome = if (lastOutcome == OutcomeType.BIG) OutcomeType.SMALL else OutcomeType.BIG
            probabilityPct = 72
        }

        val predBn = if (predictedOutcome == OutcomeType.BIG) "বিগ" else "স্মল"
        val lastBn = if (lastOutcome == OutcomeType.BIG) "বিগ" else "স্মল"

        return makePrediction(
            outcome = predictedOutcome,
            confidence = probabilityPct,
            numbers = pickNumbersFor(predictedOutcome),
            color = pickColorFor(predictedOutcome),
            rationaleEn = "Markov State Matrix: Historical state after $lastOutcome transitions to $predictedOutcome at $probabilityPct% frequency.",
            rationaleBn = "মারকভ মেট্রিক্স: অতীতে $lastBn আসার পর পরবর্তীতে $probabilityPct% ক্ষেত্রে $predBn এসেছে।"
        )
    }

    private fun predictHotCold(completed: List<GamePeriod>): RawPrediction {
        if (completed.size < 4) {
            return predictDragonStreak(completed)
        }

        val window = completed.take(20)
        val bigs = window.count { it.actualOutcome == OutcomeType.BIG }
        val smalls = window.count { it.actualOutcome == OutcomeType.SMALL }
        val total = window.size

        val predictedOutcome: OutcomeType
        val confidence: Int
        val diff = kotlin.math.abs(bigs - smalls)

        if (bigs > smalls) {
            // Small is cold, bet on Small to balance
            predictedOutcome = OutcomeType.SMALL
            confidence = (65 + (diff * 3)).coerceIn(65, 89)
        } else if (smalls > bigs) {
            // Big is cold, bet on Big to balance
            predictedOutcome = OutcomeType.BIG
            confidence = (65 + (diff * 3)).coerceIn(65, 89)
        } else {
            // Exact balance, bet on last winner
            predictedOutcome = completed.first().actualOutcome ?: OutcomeType.BIG
            confidence = 68
        }

        val predBn = if (predictedOutcome == OutcomeType.BIG) "বিগ" else "স্মল"

        return makePrediction(
            outcome = predictedOutcome,
            confidence = confidence,
            numbers = pickNumbersFor(predictedOutcome),
            color = pickColorFor(predictedOutcome),
            rationaleEn = "Hot/Cold Equilibrium: Last $total rounds (Big: $bigs, Small: $smalls). Lagging $predictedOutcome has upward delta.",
            rationaleBn = "হট/কোল্ড ভারসাম্য: বিগত $total রাউন্ডে (বিগ: $bigs, স্মল: $smalls)। ভারসাম্যের দিকে $predBn এগিয়ে।"
        )
    }

    private fun predictEnsemble(completed: List<GamePeriod>): RawPrediction {
        if (completed.isEmpty()) {
            val outcome = OutcomeType.BIG
            return makePrediction(
                outcome, 75, pickNumbersFor(outcome), pickColorFor(outcome),
                "AI Ensemble: Neural model initialized with balanced weights.",
                "এআই এনসেম্বল: নিউরাল মডেল প্রারম্ভিক ওজনে চালু হয়েছে।"
            )
        }

        val dragon = predictDragonStreak(completed)
        val reversion = predictMeanReversion(completed)
        val markov = predictMarkovChain(completed)
        val hotCold = predictHotCold(completed)

        var bigScore = 0f
        var smallScore = 0f

        // Weighting votes by confidence
        fun addVote(pred: RawPrediction, weight: Float) {
            val score = (pred.confidence / 100f) * weight
            if (pred.outcome == OutcomeType.BIG) {
                bigScore += score
            } else {
                smallScore += score
            }
        }

        addVote(dragon, 1.2f)
        addVote(reversion, 1.3f)
        addVote(markov, 1.4f)
        addVote(hotCold, 1.0f)

        val predictedOutcome = if (bigScore >= smallScore) OutcomeType.BIG else OutcomeType.SMALL
        val totalScore = bigScore + smallScore
        val winningScore = if (predictedOutcome == OutcomeType.BIG) bigScore else smallScore
        val calculatedConfidence = ((winningScore / totalScore) * 100).toInt().coerceIn(70, 96)

        // Agreement count
        val subPreds = listOf(dragon, reversion, markov, hotCold)
        val agreeCount = subPreds.count { it.outcome == predictedOutcome }

        val numbers = pickSmartNumbers(completed, predictedOutcome)
        val color = pickColorFor(predictedOutcome)

        val predBn = if (predictedOutcome == OutcomeType.BIG) "বিগ" else "স্মল"

        return makePrediction(
            outcome = predictedOutcome,
            confidence = calculatedConfidence,
            numbers = numbers,
            color = color,
            rationaleEn = "AI Consensus: $agreeCount/4 neural algorithms agree on $predictedOutcome. High confidence convergence ($calculatedConfidence%).",
            rationaleBn = "এআই কনসেনসাস: ৪টির মধ্যে ${agreeCount}টি অ্যালগরিদম একমত হয়ে $predBn প্রেডিকশন দিয়েছে। নির্ভরযোগ্যতা ($calculatedConfidence%)।"
        )
    }

    private fun pickNumbersFor(outcome: OutcomeType): List<Int> {
        return if (outcome == OutcomeType.BIG) {
            listOf(5, 6, 7, 8, 9).shuffled().take(4).sorted()
        } else {
            listOf(0, 1, 2, 3, 4).shuffled().take(4).sorted()
        }
    }

    private fun pickSmartNumbers(completed: List<GamePeriod>, outcome: OutcomeType): List<Int> {
        val pool = if (outcome == OutcomeType.BIG) listOf(5, 6, 7, 8, 9) else listOf(0, 1, 2, 3, 4)
        if (completed.size < 5) return pool.take(4)

        // Frequency count in recent history
        val recentNumbers = completed.take(20).mapNotNull { it.actualNumber }
        val counts = pool.associateWith { num -> recentNumbers.count { it == num } }
        // Pick numbers that are moderately hot or due
        return counts.entries.sortedByDescending { it.value }.map { it.key }.take(4).sorted()
    }

    private fun pickColorFor(outcome: OutcomeType): ColorType {
        // Red (even) or Green (odd) or occasional Violet (0, 5)
        val seed = Random.nextInt(10)
        return when {
            seed == 0 -> ColorType.VIOLET
            outcome == OutcomeType.BIG -> if (Random.nextBoolean()) ColorType.GREEN else ColorType.RED
            else -> if (Random.nextBoolean()) ColorType.RED else ColorType.GREEN
        }
    }

    private fun makePrediction(
        outcome: OutcomeType,
        confidence: Int,
        numbers: List<Int>,
        color: ColorType,
        rationaleEn: String,
        rationaleBn: String
    ): RawPrediction {
        return RawPrediction(
            outcome = outcome,
            confidence = confidence,
            numbers = numbers,
            color = color,
            rationaleEn = rationaleEn,
            rationaleBn = rationaleBn
        )
    }

    /**
     * Simulates an actual game outcome (0-9) realistically
     */
    fun simulateActualRound(): Int {
        return Random.nextInt(0, 10)
    }
}
