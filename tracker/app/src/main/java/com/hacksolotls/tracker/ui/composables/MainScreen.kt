package com.hacksolotls.tracker.ui.composables

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hacksolotls.tracker.ui.viewmodels.MainScreenViewModel
import com.patrykandpatryk.vico.compose.chart.Chart
import com.patrykandpatryk.vico.compose.chart.line.lineChart
import com.patrykandpatryk.vico.core.chart.line.LineChart.LineSpec

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel = hiltViewModel()) {
    val pad = 16.dp

    Scaffold(
        floatingActionButton = {

        },

        topBar = {
            CenterAlignedTopAppBar(
                // Side Menu button
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                title = {
                    Text(text = "Welcome, name")
                },
                actions = {
                    // Log Button
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = "Log")
                    }
                }
            )
        },


        ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .weight(6f)
                    .fillMaxWidth()
                //.border(2.dp, Color.Black)
            ) {
                Card(
                    modifier = Modifier.padding(pad, pad, pad, pad / 2),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    // FilledGraph based on last month

                    val data = listOf(1f, 2.5f, 1.8f, 3.4f, 2.2f)
//                    Chart(
//                        chart = lineChart(
//                            lines = listOf(
//                                LineSpec(
//                                    //lineColor = lineColor,
//                                    //lineThicknessDp = lineThicknessDp,
//                                    //lineBackgroundShader = lineBackgroundShader,
//                                    //lineCap = lineCap,
//                                    //point = point,
//                                    //pointSizeDp = pointSizeDp,
//                                    //dataLabel = dataLabel,
//                                    //dataLabelVerticalPosition = dataLabelVerticalPosition,
//                                    //dataLabelValueFormatter = dataLabelValueFormatter,
//                                    //dataLabelRotationDegrees = dataLabelRotationDegrees,
//                                    //pointPosition = pointPosition,
//                                    //pointConnector = DefaultPointConnector(cubicStrength = cubicStrength),
//                                )
//                            )
//                        ),
//                        model = TODO(),
//                        modifier = TODO(),
//                        startAxis = TODO(),
//                        topAxis = TODO(),
//                        endAxis = TODO(),
//                        bottomAxis = TODO(),
//                        marker = TODO(),
//                        markerVisibilityChangeListener = TODO(),
//                        legend = TODO(),
//                        isHorizontalScrollEnabled = TODO(),
//                        isZoomEnabled = TODO()
//                    )
//
//                }
                }}

            // Medications button? Could be swapped to have a list of previous logs
            Row(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                //.border(2.dp, Color.Black),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(pad, pad, pad, pad / 2),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    //CentralLogButton()
                }
            }

            // Log button
            Row(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                //.border(2.dp, Color.Black),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(pad, pad / 2, pad, pad),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    //CentralLogButton()
                }
            }

            // Next expected log date
            Row(
                modifier = Modifier
                    .weight(3f)
                    .background(Color.Black)
                    .border(2.dp, Color.Black)
                    .fillMaxWidth()
            ) {
                Text(text = "Placeholder")

            }
        }
    }
}

@Preview
@Composable
private fun PrevMainScreen() {
    MainScreen()
}