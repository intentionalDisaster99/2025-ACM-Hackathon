package com.hacksolotls.tracker.ui.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.hacksolotls.tracker.data.LogEvent
import com.hacksolotls.tracker.data.LogState
import com.hacksolotls.tracker.data.util.millisToLocalDate
import com.josiwhitlock.estresso.Ester
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Composable that displays the dialog used to select properties and save them to the database.
 *
 * @param state properties of the [com.hacksolotls.tracker.data.db.Log]
 * @param onEvent [LogEvent] handler
 * @param modifier modifier for this composable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertLogDialog(
    state: LogState, onEvent: (LogEvent) -> Unit, modifier: Modifier = Modifier
) {
    // Show the date picker?
    var showDatePicker by remember { mutableStateOf(false) }
    // Keep track of the state of the datePicker
    val datePickerState = rememberDatePickerState()
    // Long version of the selected date
    var selectedDate by remember { mutableStateOf<Long?>(null)}

    AlertDialog(
        // If user clicks outside of dialog, dismiss
        onDismissRequest = {
            onEvent(LogEvent.HideDialog)
        },
        confirmButton = {
            Button(onClick = {
                // User pressed save, so try to save
                if (state.dosage.toDoubleOrNull() != null && state.daysTilNext.toIntOrNull() != null) {
                    Log.d("Database", "Saving log to database...")
                    onEvent(LogEvent.SaveLog)
                    onEvent(LogEvent.HideDialog)
                }
            }) {
                Text(
                    text = "Save Log"
                )
            }
        },
        // Dialog Title
        title = { Text(text = "Log Dose") },
        // Items for the dialog
        text = {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                /* --------------------- Ester --------------------- */
                DropDownMenuBox(
                    items = Ester.entries,
                    selectedItem = state.ester,
                    itemLabel = { it.toString() },
                    label = "Select a medication",
                    onItemSelected = { onEvent(LogEvent.SetEster((it))) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                /* --------------------- Dosage --------------------- */
                TextField(
                    value = state.dosage,
                    onValueChange = { newValue ->
                        // Ensure that only numbers are allowed
                        if (newValue.toDoubleOrNull() != null || newValue.isBlank()) {
                            onEvent(LogEvent.SetDosage(newValue))
                        }
                    },
                    label = {
                        Text(text = "Dosage (mg)")
                    }
                )

                /* --------------------- Date Picker --------------------- */
                TextField(
                    value = millisToLocalDate(selectedDate).format(
                        DateTimeFormatter.ofPattern("MM/dd/yyyy")
                    ),
                    onValueChange = {}, // Just for display
                    label = {
                        Text(
                            text = "Date Taken",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    modifier = Modifier
                        .clickable { showDatePicker = true },
                    enabled = false,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            Button(
                                onClick = {
                                    // Convert selected date millis to LocalDate
                                    selectedDate =
                                        datePickerState.selectedDateMillis?.let { millis ->
                                            Instant.ofEpochMilli(millis)
                                                // Convert to ZonedDateTime in UTC
                                                .atZone(ZoneId.of("UTC"))
                                                // Extract the date part (ignoring the time)
                                                .toLocalDate()
                                                // Get the start of the day (midnight) in the USER's time zone
                                                .atStartOfDay(ZoneId.systemDefault())
                                                // Convert back to Instant
                                                .toInstant()
                                                // Convert to milliseconds
                                                .toEpochMilli()
                                        }
                                    if (selectedDate == 0L || selectedDate == null) {
                                        // If no date is selected, use now
                                        onEvent(LogEvent.SetTime(Instant.now().toEpochMilli()))
                                    } else {
                                        // Set the date as the selected date, asserted as not null
                                        onEvent(LogEvent.SetTime(selectedDate!!))
                                    }
                                    showDatePicker = false
                                }
                            ) {
                                Text("OK")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }

                /* --------------------- Days Til Next --------------------- */
                TextField(
                    value = state.daysTilNext,
                    onValueChange = { newValue ->
                        // Ensure that only numbers are allowed
                        if (newValue.toDoubleOrNull() != null || newValue.isBlank()) {
                            onEvent(LogEvent.SetDaysTilNext(newValue))
                        }
                    },
                    label = {
                        Text(text = "Days until next dose")
                    }
                )
                /* TODO: Probably add back in, with the log-level dosage taking precedence
             *       probably just to indicate defaults
             */
            }
        }
    )
}