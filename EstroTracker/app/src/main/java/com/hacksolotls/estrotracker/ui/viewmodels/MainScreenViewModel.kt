package com.hacksolotls.estrotracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.hacksolotls.estrotracker.data.db.Log
import com.hacksolotls.estrotracker.data.db.LogDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val logDao: LogDao
) : ViewModel() {

    private val _log = MutableLiveData<Log?>()
    val log: LiveData<Log?> get() = _log

    fun getMostRecentLog() {
        logDao.getMostRecentLog().observeForever { log ->
            _log.value = log
        }
    }
}

