package com.hacksolotls.tracker.ui.viewmodels

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.josiwhitlock.estresso.Estresso
import com.hacksolotls.tracker.data.db.ChartData
import com.hacksolotls.tracker.data.db.Log
import com.hacksolotls.tracker.data.db.LogDao
import com.josiwhitlock.estresso.Ester
import com.josiwhitlock.estresso.Estresso.e2multidose3C
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val logDao: LogDao
) : ViewModel() {
    private val millisPerDay = 86400000
    private val step = (0.5 * millisPerDay).toLong()

    val logData: LiveData<List<Log>> = logDao.getAllLogs()// TODO: change to getXDaysOfLogsFromDate

    fun logsToChartData(logs: List<Log>): List<ChartData> {
        val times: List<Long> = logs.map { it.timestamp.toEpochMilli() }
        val doses: List<Double> = logs.map { it.dosage }
        val esters: List<Ester> = logs.map { it.medication }

        val chartDatas: MutableList<ChartData> = mutableListOf()

        // Forty days from yesterday
        var time = times[0]
        while (time < times[times.count() - 1]) {
            val temp = ChartData(
                timestamp = time,
                eLevel = e2multidose3C(
                    t = time / millisPerDay.toDouble(),
                    doses = doses,
                    times = times.map { it.toDouble() },
                    models = esters
                )
            )
            chartDatas.add(temp)

            time += step
        }

        return emptyList()
    }
}