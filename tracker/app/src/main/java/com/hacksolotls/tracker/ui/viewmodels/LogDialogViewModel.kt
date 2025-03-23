package com.hacksolotls.tracker.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hacksolotls.tracker.data.LogEvent
import com.hacksolotls.tracker.data.LogState
import com.hacksolotls.tracker.data.db.Database
import com.hacksolotls.tracker.data.db.Log
import com.hacksolotls.tracker.data.db.LogDao
import com.josiwhitlock.estresso.Ester
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    val database: Database,
    private val logDao: LogDao
) : ViewModel() {

    private val _state: MutableStateFlow<LogState>
    val state: StateFlow<LogState>

    init {
        _state = MutableStateFlow(LogState())
        state = _state.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            LogState()
        )
    }

    fun onEvent(event: LogEvent) {
        when (event) {
            LogEvent.HideDialog -> {
                _state.update {
                    it.copy(isAddingLog = false)
                }
            }

            LogEvent.ShowDialog -> {
                _state.update {
                    it.copy(isAddingLog = true)
                }
            }

            is LogEvent.DeleteMedication -> {
                viewModelScope.launch {
                    println("Uh hey no");
                }
            }

            LogEvent.SaveLog -> {
                val ester = state.value.ester
                val timestamp = state.value.timestamp
                val dosage = state.value.dosage
                val daysTilNext = state.value.daysTilNext

                if (timestamp > 0 || dosage > 0) return

                val log = Log(
                    medication = ester,
                    timestamp = Instant.ofEpochMilli(timestamp),
                    dosage = dosage,
                    daysTilNext = daysTilNext
                )

                viewModelScope.launch {
                    logDao.UpsertLog(log)
                }

                _state.update {
                    it.copy(
                        isAddingLog = false,
                        ester = Ester.VALERATE,
                        timestamp = 0,
                        dosage = 5.0,
                        daysTilNext = 5
                    )
                }
            }

            is LogEvent.SetEster -> {
                _state.update {
                    it.copy(ester = event.ester)
                }
            }
            is LogEvent.SetDosage -> {
                _state.update {
                    it.copy(dosage = event.dosage)
                }
            }
            is LogEvent.SetDaysTilNext -> {
                _state.update {
                    it.copy(daysTilNext = event.days)
                }
            }

            is LogEvent.SetTime -> TODO()
        }
    }
}