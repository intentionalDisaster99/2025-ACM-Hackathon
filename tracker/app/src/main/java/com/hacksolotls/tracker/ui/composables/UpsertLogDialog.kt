//package com.hacksolotls.tracker.ui.composables
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun UpsertLogDialog(
//    state: LogState, onEvent: (LogEvent) -> Unit, modifier: Modifier = Modifier
//) {
//    // Med Form
//
//    AlertDialog(onDismissRequest = {
//        onEvent(LogEvent.HideDialog)
//    }, confirmButton = {
//        Button(onClick = {
//            if (state.name.isNotBlank() && state.intervalValue >= 1) {
//                onEvent(LogEvent.SaveMedication)
//                onEvent(LogEvent.HideDialog)
//            }
//        }) {
//            Text(
//                text = "Save Log"
//            )
//        }
//    }, title = { Text(text = "Log Dose") }, text = {
//        Column(
//            verticalArrangement = Arrangement.Center
//        ) {
//            /* --------------------- Med Name --------------------- */
//            TextField(
//                value = state.name,
//                onValueChange = {
//                    onEvent(LogEvent.SetName(it))
//                },
//                label = {
//                    Text(text = "Medication Name")
//                }
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            /* --------------------- Form --------------------- */
//            DropDownMenuBox(
//                items = MedicationForm.entries,
//                selectedItem = state.form,
//                itemLabel = { it.toString() },
//                label = "Select a medication",
//                onItemSelected = { onEvent(MedicationEvent.SetForm((it as MedicationForm).toEster())) }
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            /* TODO: Probably add back in, with the log-level dosage taking precedence
//             *       probably just to indicate defaults
//             */
//
//            /* --------------------- Dosage --------------------- */
//            Text(
//                modifier = Modifier,
//                text = "Dosage",
//                fontWeight = FontWeight.Bold
//            )
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//
////                Spacer(modifier = Modifier.width(10.dp))
////                Spacer(modifier = Modifier.width(10.dp))
//                TextField(
//                    value = state.dosage.toString(),
//                    onValueChange = { newText ->
//
//                        val newValue: Float? = if (newText.isNotBlank()) {
//                            newText.toFloatOrNull()
//                        } else {
//                            null
//                        }
//
//                        onEvent(MedicationEvent.SetDosage(newValue ?: state.dosage))
//                    },
//                    label = { Text("Amount") },
//                    modifier = Modifier.weight(3f)
//                )
//
//                Spacer(modifier = Modifier.width(10.dp))
//
//                DropDownMenuBox(
//                    items = DosageUnit.entries,
//                    selectedItem = state.dosageUnit,
//                    onItemSelected = { onEvent(MedicationEvent.SetDosageUnit(it.toString())) },
//                    itemLabel = { it.toString() },
//                    label = "Unit",
//                    modifier = Modifier.weight(3f)
//                )
//            }
//        }
//    })
//}