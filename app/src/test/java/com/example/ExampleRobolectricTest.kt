package com.example

import com.example.engine.PredictionEngine
import com.example.model.AlgorithmType
import com.example.model.GamePeriod
import com.example.model.OutcomeType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExamplePredictionTest {

  @Test
  fun testDragonStreakLogic() {
    val completedRounds = listOf(
      GamePeriod(
        periodId = "1001",
        predictedOutcome = OutcomeType.BIG,
        confidence = 80,
        predictedNumbers = listOf(5, 6, 7, 8),
        predictedColor = com.example.model.ColorType.RED,
        multiplierLevel = 1,
        algorithmUsed = AlgorithmType.DRAGON_STREAK,
        rationaleEn = "Test",
        rationaleBn = "টেস্ট",
        actualNumber = 7,
        actualOutcome = OutcomeType.BIG,
        actualColor = com.example.model.ColorType.GREEN,
        isWon = true
      ),
      GamePeriod(
        periodId = "1000",
        predictedOutcome = OutcomeType.BIG,
        confidence = 80,
        predictedNumbers = listOf(5, 6, 7, 8),
        predictedColor = com.example.model.ColorType.RED,
        multiplierLevel = 1,
        algorithmUsed = AlgorithmType.DRAGON_STREAK,
        rationaleEn = "Test",
        rationaleBn = "টেস্ট",
        actualNumber = 8,
        actualOutcome = OutcomeType.BIG,
        actualColor = com.example.model.ColorType.RED,
        isWon = true
      )
    )

    val prediction = PredictionEngine.predictNext(completedRounds, AlgorithmType.DRAGON_STREAK)
    assertEquals(OutcomeType.BIG, prediction.outcome)
    assertTrue(prediction.confidence >= 70)
  }

  @Test
  fun testMeanReversionLogic() {
    // 4 consecutive BIGs should trigger reversion to SMALL
    val completedRounds = (1..4).map { idx ->
      GamePeriod(
        periodId = "100$idx",
        predictedOutcome = OutcomeType.BIG,
        confidence = 80,
        predictedNumbers = listOf(5, 6, 7, 8),
        predictedColor = com.example.model.ColorType.RED,
        multiplierLevel = 1,
        algorithmUsed = AlgorithmType.MEAN_REVERSION,
        rationaleEn = "Test",
        rationaleBn = "টেস্ট",
        actualNumber = 6,
        actualOutcome = OutcomeType.BIG,
        actualColor = com.example.model.ColorType.RED,
        isWon = true
      )
    }

    val prediction = PredictionEngine.predictNext(completedRounds, AlgorithmType.MEAN_REVERSION)
    assertEquals(OutcomeType.SMALL, prediction.outcome)
    assertTrue(prediction.confidence >= 80)
  }
}
