package com.example.model

enum class OutcomeType(val label: String, val labelBn: String) {
    BIG("BIG", "বিগ"),
    SMALL("SMALL", "স্মল");

    companion object {
        fun fromNumber(number: Int): OutcomeType {
            return if (number in 5..9) BIG else SMALL
        }
    }
}

enum class ColorType(val label: String, val labelBn: String) {
    GREEN("GREEN", "গ্রিন"),
    RED("RED", "রেড"),
    VIOLET("VIOLET", "ভায়োলেট");

    companion object {
        fun fromNumber(number: Int): ColorType {
            return when (number) {
                0, 5 -> VIOLET
                1, 3, 7, 9 -> GREEN
                2, 4, 6, 8 -> RED
                else -> RED
            }
        }
    }
}

enum class AlgorithmType(
    val title: String,
    val titleBn: String,
    val description: String,
    val descriptionBn: String
) {
    AI_ENSEMBLE(
        "AI Smart Ensemble",
        "এআই স্মার্ট এনসেম্বল",
        "Combines weighted multi-model probability, streak memory & volatility metrics",
        "মাল্টি-মডেল প্রবাবিলিটি এবং ট্রেন্ড মেমোরির সম্মিলিত এআই প্রেডিকশন"
    ),
    DRAGON_STREAK(
        "Dragon Streak Follower",
        "ড্রাগন ট্রেন্ড ফলোয়ার",
        "Rides continuous winning patterns and streak momentum",
        "চলমান একাধারে বিগ বা স্মল ট্রেন্ডকে অনুসরণ করে"
    ),
    MEAN_REVERSION(
        "Anti-Streak Reversal",
        "অ্যান্টি-স্ট্রাইক রিভার্সাল",
        "Anticipates pattern breakout and statistical flips after 3+ streaks",
        "টানা ৩ বারের বেশি একই ফল আসলে বিপরীতে পরিবর্তনের সংকেত"
    ),
    MARKOV_CHAIN(
        "Markov Probability Matrix",
        "মারকভ প্রবাবিলিটি মেট্রিক্স",
        "Calculates conditional probability transition P(Next | Previous States)",
        "পূর্ববর্তী রাউন্ডের ক্রম অনুসারে গাণিতিক সম্ভাব্যতার হিসাব"
    ),
    HOT_COLD(
        "Hot & Cold Equilibrium",
        "হট-কোল্ড ইকুইলিব্রিয়াম",
        "Balances historical frequency ratios and bets on lagging outcomes",
        "পূর্বের রাউন্ডসমূহের অনুপাত বিশ্লেষণ করে ভারসাম্য রক্ষা"
    )
}

enum class StrategyRisk(
    val title: String,
    val titleBn: String,
    val description: String,
    val multipliers: List<Int>
) {
    FLAT_1X(
        "Flat 1X (Safe)",
        "ফ্ল্যাট ১x (নিরাপদ)",
        "Maintains consistent 1X stake on every round",
        listOf(1, 1, 1, 1, 1, 1)
    ),
    MARTINGALE_3X(
        "Martingale 3X (Standard)",
        "মার্টিঙ্গেল ৩x (স্ট্যান্ডার্ড)",
        "Increases stake on loss: 1X -> 3X -> 9X -> 27X to recover immediately",
        listOf(1, 3, 9, 27, 81)
    ),
    FIBONACCI(
        "Fibonacci Moderate",
        "ফিবোনাচ্চি ব্যালান্সড",
        "Uses natural sequence: 1X -> 1X -> 2X -> 3X -> 5X -> 8X",
        listOf(1, 1, 2, 3, 5, 8)
    ),
    DALEMBERT(
        "D'Alembert (+1X / -1X)",
        "ডি'আলেমবার্ট (+১x / -১x)",
        "Adds 1X stake after loss, subtracts 1X stake after win",
        listOf(1, 2, 3, 4, 5, 6)
    )
}

enum class AppLanguage {
    ENGLISH,
    BENGALI
}

data class GamePeriod(
    val periodId: String,
    val predictedOutcome: OutcomeType,
    val confidence: Int, // 50 to 98%
    val predictedNumbers: List<Int>,
    val predictedColor: ColorType,
    val multiplierLevel: Int,
    val algorithmUsed: AlgorithmType,
    val rationaleEn: String,
    val rationaleBn: String,
    val actualNumber: Int? = null,
    val actualOutcome: OutcomeType? = null,
    val actualColor: ColorType? = null,
    val isWon: Boolean? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val virtualStake: Double = 10.0,
    val virtualProfit: Double = 0.0
)

data class PredictionStats(
    val totalPredictions: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val winRate: Float = 0f,
    val currentStreak: Int = 0,
    val isCurrentWinStreak: Boolean = true,
    val bestWinStreak: Int = 0,
    val bigCount: Int = 0,
    val smallCount: Int = 0,
    val demoBalance: Double = 1000.0,
    val totalProfit: Double = 0.0
)
