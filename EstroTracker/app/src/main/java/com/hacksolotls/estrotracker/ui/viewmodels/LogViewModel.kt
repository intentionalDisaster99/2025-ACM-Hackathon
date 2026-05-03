package com.hacksolotls.estrotracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.hacksolotls.estrotracker.data.db.ChartData
import com.hacksolotls.estrotracker.data.db.Log
import com.hacksolotls.estrotracker.data.db.LogDao
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val logDao: LogDao
) : ViewModel() {

}

