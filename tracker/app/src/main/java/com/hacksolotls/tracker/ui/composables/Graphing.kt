package com.hacksolotls.tracker.ui.composables

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hacksolotls.tracker.ui.viewmodels.MainScreenViewModel
import com.patrykandpatrick.vico.compose.cartesian.*
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.dimensions
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.compose.common.shape.dashedShape
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.*
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext


import com.hacksolotls.tracker.ui.theme.TrackerTheme
import com.hacksolotls.tracker.ui.viewmodels.ChartViewModel
import com.hacksolotls.tracker.ui.viewmodels.LogDialogViewModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.component.LineComponent

/**
 * Displays a graph with the given data.
 * @param modifier modifier for the graph
 * @param data input data for the graph
 * @param scatterData blood work data imposed over the graph
 */
@Composable
fun VicoGraph(
    modifier: Modifier = Modifier.fillMaxSize(),
    data: List<List<Double>>,
    // scatterData: List<List<Double>>
) {

    // Get the Context using LocalContext
    val context = LocalContext.current

    // Initialize PreferencesManager with the current Context
    val preferencesManager = PreferencesManager(context)

    // Retrieve saved values from SharedPreferences
    val isDarkMode by remember { mutableStateOf(preferencesManager.isDarkMode()) }


    TrackerTheme(darkTheme = isDarkMode) {

        val chartData = if (data.isEmpty()) {
            println("No data defaulting to nothing")
            listOf(
                listOf(System.currentTimeMillis().toDouble(), 01.0),  // Current time in ms
                listOf(System.currentTimeMillis().toDouble() + 1000000, 01.0)  // A future time, offset by 1 million ms
            )
        } else {
            println("We have data now")
            data
        }


        val modelProducer = remember { CartesianChartModelProducer() }
        val colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.outlineVariant)
        val dateFormatter = remember { SimpleDateFormat("MM/dd", Locale.getDefault()) }

        LaunchedEffect(chartData /*, scatterData */) {
            modelProducer.runTransaction {
                // Line Chart Data
                val xsLine = chartData.map { it[0] }
                val ysLine = chartData.map { it[1] }
                lineSeries {
                    series(xsLine, ysLine)
                }

                // Bar Chart Data
//                val xsScatter = scatterData.map { it[0] }
//                val ysScatter = scatterData.map { it[1] }
//                columnSeries {
//                    series(xsScatter, ysScatter)
//                }
            }
        }

        CartesianChartHost(
            modifier = modifier.background(MaterialTheme.colorScheme.background),
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    LineCartesianLayer.LineProvider.series(
                        LineCartesianLayer.rememberLine(
                            fill = remember(colors) {
                                LineCartesianLayer.LineFill.double(fill(colors[0]), fill(colors[1]))
                            },
                            areaFill = remember(colors) {
                                LineCartesianLayer.AreaFill.double(
                                    topFill = fill(
                                        DynamicShader.verticalGradient(
                                            arrayOf(colors[0], Color.Transparent)
                                        )
                                    ),
                                    bottomFill = fill(
                                        DynamicShader.verticalGradient(
                                            arrayOf(Color.Transparent, Color.Black)
                                        )
                                    )
                                )
                            },
                            pointConnector = remember {
                                LineCartesianLayer.PointConnector.cubic(curvature = 0.4f)
                            }
                        )
                    )
                ),
                rememberColumnCartesianLayer(
                    ColumnCartesianLayer.ColumnProvider.series(
                        LineComponent(
                            fill(MaterialTheme.colorScheme.tertiary)
                        )
                    )
                ),
                startAxis = VerticalAxis.rememberStart(
                    label = rememberAxisLabelComponent(
                        color = MaterialTheme.colorScheme.secondary,
                        margins = dimensions(4.dp),
                        padding = dimensions(8.dp, 4.dp),
                        background = rememberShapeComponent(
                            fill(Color.Transparent),
                            CorneredShape.rounded(allPercent = 25)
                        )
                    ),
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside,
                    title = "Estrogen Level pg/mL",
                    titleComponent = rememberAxisLabelComponent(
                        color = MaterialTheme.colorScheme.onTertiary,
                        margins = dimensions(4.dp),
                        padding = dimensions(8.dp, 4.dp),
                        background = rememberShapeComponent(
                            fill(MaterialTheme.colorScheme.tertiary),
                            CorneredShape.rounded(allPercent = 25)
                        ),
                        textSize = 15.sp
                    ),
                    guideline = rememberLineComponent(
                        fill = fill(MaterialTheme.colorScheme.outlineVariant),
                        shape = dashedShape(
                            shape = CorneredShape.rounded(allPercent = 25),
                            dashLength = 4.dp,
                            gapLength = 6.dp
                        )
                    )
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    label = rememberAxisLabelComponent(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        margins = dimensions(4.dp),
                        padding = dimensions(8.dp, 4.dp),
                        background = rememberShapeComponent(
                            fill(MaterialTheme.colorScheme.secondaryContainer),
                            CorneredShape.Pill
                        )
                    ),
                    title = "Date",
                    titleComponent = rememberAxisLabelComponent(
                        color = MaterialTheme.colorScheme.onTertiary,
                        margins = dimensions(4.dp),
                        padding = dimensions(8.dp, 4.dp),
                        background = rememberShapeComponent(
                            fill(MaterialTheme.colorScheme.tertiary),
                            CorneredShape.rounded(allPercent = 25)
                        ),
                        textSize = 15.sp
                    ),
                    valueFormatter = { _, value, _ ->
                        try {
                            if (value.toLong() == 0L) {
                                "No Date"
                            } else {
                                dateFormatter.format(value.toLong())
                            }
                        } catch (e: Exception) {
                            "Error"
                        }
                    },
                    guideline = null,

                )
            ),
            modelProducer = modelProducer
        )
    }
}