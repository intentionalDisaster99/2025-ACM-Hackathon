package com.hacksolotls.tracker.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.hacksolotls.tracker.data.LogEvent
import com.hacksolotls.tracker.data.LogState
import com.hacksolotls.tracker.data.util.InstantConverter
import com.hacksolotls.tracker.data.util.millisToLocalDate
import com.josiwhitlock.estresso.Ester
import com.josiwhitlock.estresso.Estresso
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertLogDialog(
    state: LogState, onEvent: (LogEvent) -> Unit, modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var selectedDate by remember { mutableStateOf<Long?>(null)}

    AlertDialog(onDismissRequest = {
        onEvent(LogEvent.HideDialog)
    }, confirmButton = {
        Button(onClick = {
            if (state.dosage.isDigitsOnly() && state.daysTilNext.isDigitsOnly()) {
                onEvent(LogEvent.SaveLog)
                onEvent(LogEvent.HideDialog)
            }
        }) {
            Text(
                text = "Save Log"
            )
        }
    }, title = { Text(text = "Log Dose") }, text = {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            /* --------------------- Form --------------------- */
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
                    .fillMaxWidth()
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
                                selectedDate = datePickerState.selectedDateMillis?.let { millis ->
                                    Instant.ofEpochMilli(millis)
                                        .atZone(ZoneId.of("UTC")) // Convert to ZonedDateTime in UTC
                                        .toLocalDate() // Extract the date part (ignoring the time)
                                        .atStartOfDay(ZoneId.systemDefault()) // Get the start of the day (midnight) in the USER's time zone
                                        .toInstant() // Convert back to Instant
                                        .toEpochMilli() // Convert to milliseconds and add one
                                }
                                println(selectedDate)
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
            /* TODO: Probably add back in, with the log-level dosage taking precedence
             *       probably just to indicate defaults
             */
        }
    })
}