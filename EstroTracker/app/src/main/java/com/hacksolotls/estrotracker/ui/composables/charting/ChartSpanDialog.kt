package com.hacksolotls.estrotracker.ui.composables.charting

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.hacksolotls.estrotracker.ui.viewmodels.ViewSpan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartSpanDialog(onDismiss: (ViewSpan?) -> Unit, span: ViewSpan) {
    var selectedIndex by remember { mutableIntStateOf(span.ordinal) }
    AlertDialog(
        onDismissRequest = { onDismiss(ViewSpan.entries[selectedIndex]) },
        title = { Text("Chart Time Span") },
        text = {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "How much time should the chart show?",
                    modifier = Modifier.padding(0.dp,0.dp, 0.dp, 5.dp),
                    style = TextStyle(
                        fontSize = 14.sp
                    )
                )

                val options = listOf("2 weeks", "1 month", "3 Months")

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    options.forEachIndexed { index, label ->
                        val isSelected = selectedIndex == index
                        val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceVariant
                        val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant

                        Surface(
                            onClick = { selectedIndex = index },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = containerColor,
                            contentColor = contentColor
                        ) {
                            Box(
                                modifier = Modifier.padding(12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = label, style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss(ViewSpan.entries[selectedIndex]) }) { Text("Save") }
        },
    )
}