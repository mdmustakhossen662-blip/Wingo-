package com.example

import com.example.engine.PredictionEngine
import com.example.model.AlgorithmType
import com.example.model.ColorType
import com.example.model.OutcomeType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun verifyBigSmallCategorization() {
    assertEquals(OutcomeType.SMALL, OutcomeType.fromNumber(0))
    assertEquals(OutcomeType.SMALL, OutcomeType.fromNumber(1))
    assertEquals(OutcomeType.SMALL, OutcomeType.fromNumber(2))
    assertEquals(OutcomeType.SMALL, OutcomeType.fromNumber(3))
    assertEquals(OutcomeType.SMALL, OutcomeType.fromNumber(4))

    assertEquals(OutcomeType.BIG, OutcomeType.fromNumber(5))
    assertEquals(OutcomeType.BIG, OutcomeType.fromNumber(6))
    assertEquals(OutcomeType.BIG, OutcomeType.fromNumber(7))
    assertEquals(OutcomeType.BIG, OutcomeType.fromNumber(8))
    assertEquals(OutcomeType.BIG, OutcomeType.fromNumber(9))
  }

  @Test
  fun verifyColorCategorization() {
    assertEquals(ColorType.VIOLET, ColorType.fromNumber(0))
    assertEquals(ColorType.GREEN, ColorType.fromNumber(1))
    assertEquals(ColorType.RED, ColorType.fromNumber(2))
    assertEquals(ColorType.GREEN, ColorType.fromNumber(3))
    assertEquals(ColorType.RED, ColorType.fromNumber(4))
    assertEquals(ColorType.VIOLET, ColorType.fromNumber(5))
    assertEquals(ColorType.RED, ColorType.fromNumber(6))
    assertEquals(ColorType.GREEN, ColorType.fromNumber(7))
    assertEquals(ColorType.RED, ColorType.fromNumber(8))
    assertEquals(ColorType.GREEN, ColorType.fromNumber(9))
  }

  @Test
  fun verifyPredictionEngineAlgorithms() {
    AlgorithmType.values().forEach { algo ->
      val pred = PredictionEngine.predictNext(emptyList(), algo)
      assertNotNull(pred.outcome)
      assertTrue(pred.confidence in 50..100)
      assertTrue(pred.numbers.isNotEmpty())
      assertNotNull(pred.color)
      assertTrue(pred.rationaleEn.isNotEmpty())
      assertTrue(pred.rationaleBn.isNotEmpty())
    }
  }
}
