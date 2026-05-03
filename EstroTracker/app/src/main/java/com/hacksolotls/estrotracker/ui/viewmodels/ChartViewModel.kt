package com.hacksolotls.estrotracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.hacksolotls.estrotracker.data.db.ChartData
import com.hacksolotls.estrotracker.data.db.Log
import com.hacksolotls.estrotracker.data.db.LogDao
import com.josiwhitlock.estresso.Estresso.e2multidose3C
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val logDao: LogDao,
) : ViewModel() {
    private val millisPerDay = 86400000L
    private val step = (0.5 * millisPerDay).toLong()

    val modelProducer = CartesianChartModelProducer()

    // 1. Unified State
    val displayConfig = MutableLiveData(DisplayConfig(ZonedDateTime.now(), ViewSpan.Month))

    // 2. Automated Display Bounds
    // This defines exactly what the X-axis should show
    val displayBounds: LiveData<Pair<Long, Long>> = displayConfig.map { config ->
        val start = when (config.span) {
            ViewSpan.Week -> config.anchorDate.minusWeeks(1)
            ViewSpan.Month -> config.anchorDate.withDayOfMonth(1)
            ViewSpan.ThreeMonths -> config.anchorDate.minusMonths(1).withDayOfMonth(1)
        }.toInstant().toEpochMilli()

        val end = when (config.span) {
            ViewSpan.Week -> config.anchorDate.plusWeeks(1) // Showing a 2-week window around anchor
            ViewSpan.Month -> config.anchorDate.plusMonths(1).withDayOfMonth(1)
            ViewSpan.ThreeMonths -> config.anchorDate.plusMonths(2).withDayOfMonth(1)
        }.toInstant().toEpochMilli()

        start to end
    }

    // 3. Calculation Data (Display Range + 1 month buffer on both sides)
    val calculationData: LiveData<List<Log>> = displayBounds.switchMap { bounds ->
        val buffer = 30L * millisPerDay
        val calcStart = bounds.first - buffer
        val calcEnd = bounds.second + buffer
        logDao.getLogsInDateRange(calcStart, calcEnd)
    }

    /**
     * Shifts the anchor date based on the current ViewSpan.
     * @param direction 1 for forward, -1 for backward.
     */
    fun shiftFocus(direction: Int) {
        val current = displayConfig.value ?: return
        val amount = direction.toLong()

        val newAnchor = when (current.span) {
            ViewSpan.Week -> current.anchorDate.plusWeeks(amount)
            ViewSpan.Month -> current.anchorDate.plusMonths(amount)
            ViewSpan.ThreeMonths -> current.anchorDate.plusMonths(amount * 3)
        }

        displayConfig.value = current.copy(anchorDate = newAnchor)
    }

    /**
     * Sets a specific ViewSpan (e.g., switching from Month to Week view).
     */
    fun updateSpan(newSpan: ViewSpan) {
        displayConfig.value = displayConfig.value?.copy(span = newSpan)
    }

    suspend fun updateChart(logs: List<Log>, bounds: Pair<Long, Long>) {
        val chartData = logsToChartData(logs, bounds)

        modelProducer.runTransaction {
            if (chartData.isNotEmpty()) {
                lineSeries {
                    series(
                        x = chartData.map { it.timestamp },
                        y = chartData.map { it.eLevel }
                    )
                }
            }
        }
    }

    fun logsToChartData(logs: List<Log>, bounds: Pair<Long, Long>): List<ChartData> {
        if (logs.isEmpty()) return emptyList()

        val times = logs.map { it.timestamp.toEpochMilli() }
        val doses = logs.map { it.dosage }
        val esters = logs.map { it.medication }

        val chartDatas = mutableListOf<ChartData>()

        // START at the beginning of the display range, not the first log
        var currentTime = bounds.first

        // END at the end of the display range
        while (currentTime <= bounds.second) {
            val level = e2multidose3C(
                t = currentTime / millisPerDay.toDouble(),
                doses = doses,
                times = times.map { it.toDouble() / millisPerDay.toDouble() },
                models = esters
            )

            chartDatas.add(ChartData(currentTime, level))
            currentTime += step
        }

        return chartDatas
    }
}

enum class ViewSpan { Week, Month, ThreeMonths }

data class DisplayConfig(
    val anchorDate: ZonedDateTime,
    val span: ViewSpan
)