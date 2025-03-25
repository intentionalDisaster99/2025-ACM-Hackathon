package com.hacksolotls.tracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hacksolotls.tracker.data.db.ChartData
import com.hacksolotls.tracker.data.db.Log
import com.hacksolotls.tracker.data.db.LogDao
import com.josiwhitlock.estresso.Ester
import com.josiwhitlock.estresso.Estresso.e2multidose3C
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val logDao: LogDao
) : ViewModel() {
    private val millisPerDay = 86400000
    private val step = (0.5 * millisPerDay).toLong()

    val logData: LiveData<List<Log>> = logDao.getAllLogs()// TODO: change to getXDaysOfLogsFromDate

    fun logsToChartData(logs: List<Log>): List<ChartData> {

        if (logs.isEmpty()) {
            return emptyList() // No data, return empty list
        }

        val times: List<Long> = logs.map { it.timestamp.toEpochMilli() }
        val doses: List<Double> = logs.map { it.dosage }
        val esters: List<Ester> = logs.map { it.medication }

        val chartDatas: MutableList<ChartData> = mutableListOf()

        // Forty days from yesterday
        var time = times[0]

        while (time < times[times.count() - 1] + millisPerDay * 7) {
            val temp = ChartData(
                timestamp = time,
                eLevel = e2multidose3C(
                    t = time / millisPerDay.toDouble(),
                    doses = doses,
                    times = times.map { it.toDouble() / millisPerDay.toDouble()},
                    models = esters
                )
            )
            println(time)
            println(temp.eLevel)
            chartDatas.add(temp)

            time += step
        }
        println("In the logs to chart data function")


        return chartDatas
    }

    fun logsToChartDataForGraph(logs: List<Log>): List<List<Double>> {
        val chartData = logsToChartData(logs)
        return if (chartData.isNotEmpty()) {
            chartData.map { listOf(it.timestamp.toDouble(), it.eLevel) }
        } else {
            // Return an empty list or handle it with a placeholder message
            emptyList()  // Empty list signifies no data available
        }
    }

}