package com.example.engine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class FetchedGameRound(
    val periodId: String,
    val drawnNumber: Int,
    val timestamp: Long = System.currentTimeMillis()
)

object LiveApiSyncManager {

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    /**
     * Attempts to fetch and parse the latest round results from a user-specified or custom API URL.
     * Supports standard formats (e.g., list of records with period and number).
     */
    suspend fun fetchLatestRoundsFromApi(apiUrl: String): Result<List<FetchedGameRound>> = withContext(Dispatchers.IO) {
        try {
            if (apiUrl.isBlank() || !apiUrl.startsWith("http")) {
                return@withContext Result.failure(IllegalArgumentException("Invalid URL format. Must start with http:// or https://"))
            }

            val request = Request.Builder()
                .url(apiUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .header("Accept", "application/json")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("HTTP Error ${response.code}: ${response.message}"))
            }

            val bodyString = response.body?.string() ?: ""
            if (bodyString.isBlank()) {
                return@withContext Result.failure(Exception("Empty response body from API"))
            }

            val parsedRounds = parseApiResponse(bodyString)
            if (parsedRounds.isEmpty()) {
                return@withContext Result.failure(Exception("Could not find period or number fields in JSON response"))
            }

            Result.success(parsedRounds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseApiResponse(jsonStr: String): List<FetchedGameRound> {
        val results = mutableListOf<FetchedGameRound>()
        val trimmed = jsonStr.trim()

        try {
            if (trimmed.startsWith("[")) {
                val array = JSONArray(trimmed)
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i) ?: continue
                    parseSingleObject(obj)?.let { results.add(it) }
                }
            } else if (trimmed.startsWith("{")) {
                val root = JSONObject(trimmed)
                // Look for common nested array keys like data, list, rows, result, games
                val candidates = listOf("data", "list", "rows", "result", "history", "items")
                var foundArray: JSONArray? = null

                for (key in candidates) {
                    if (root.has(key)) {
                        val v = root.opt(key)
                        if (v is JSONArray) {
                            foundArray = v
                            break
                        } else if (v is JSONObject) {
                            // Check nested data.list
                            for (subKey in candidates) {
                                val sub = v.opt(subKey)
                                if (sub is JSONArray) {
                                    foundArray = sub
                                    break
                                }
                            }
                        }
                    }
                }

                if (foundArray != null) {
                    for (i in 0 until foundArray.length()) {
                        val obj = foundArray.optJSONObject(i) ?: continue
                        parseSingleObject(obj)?.let { results.add(it) }
                    }
                } else {
                    // Try parsing root as single object
                    parseSingleObject(root)?.let { results.add(it) }
                }
            }
        } catch (_: Exception) {
            // Ignore parse exceptions
        }

        return results
    }

    private fun parseSingleObject(obj: JSONObject): FetchedGameRound? {
        val periodKeys = listOf(
            "issueNumber", "period", "periodId", "stageNumber", "issue", "id", "expect", "round"
        )
        val numberKeys = listOf(
            "number", "openCode", "result", "premium", "winNumber", "lotteryNumber", "digit", "val"
        )

        var period: String? = null
        for (pk in periodKeys) {
            if (obj.has(pk)) {
                period = obj.optString(pk, "")
                if (period.isNotBlank()) break
            }
        }

        var number: Int? = null
        for (nk in numberKeys) {
            if (obj.has(nk)) {
                val rawVal = obj.opt(nk)
                when (rawVal) {
                    is Int -> number = rawVal
                    is String -> {
                        // Extract first digit or integer
                        val cleaned = rawVal.filter { it.isDigit() }
                        if (cleaned.isNotBlank()) {
                            number = cleaned.last().digitToIntOrNull()
                        }
                    }
                }
                if (number != null) break
            }
        }

        return if (!period.isNullOrBlank() && number != null && number in 0..9) {
            FetchedGameRound(periodId = period, drawnNumber = number)
        } else {
            null
        }
    }
}
