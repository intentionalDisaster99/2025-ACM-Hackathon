package com.hacksolotls.tracker.ui.composables

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


import com.hacksolotls.tracker.ui.theme.TrackerTheme

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
    scatterData: List<List<Double>>
) {
    TrackerTheme(darkTheme = false) {
        val modelProducer = remember { CartesianChartModelProducer() }
        val colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.outlineVariant)
        val columnChartColors = listOf(Color(0xFF6200EE), Color(0xFF3700B3), Color(0xFF03DAC5))
        val dateFormatter = remember { SimpleDateFormat("MM/dd", Locale.getDefault()) }

        LaunchedEffect(data, scatterData) {
            modelProducer.runTransaction {
                // Line Chart Data
                val xsLine = data.map { it[0] }
                val ysLine = data.map { it[1] }
                lineSeries {
                    series(xsLine, ysLine)
                }

                // Bar Chart Data (Added Without Changing Anything Else)
                val xsScatter = scatterData.map { it[0] }
                val ysScatter = scatterData.map { it[1] }
                columnSeries {
                    series(xsScatter, ysScatter)
                }
            }
        }

        CartesianChartHost(
            modifier = modifier.background(MaterialTheme.colorScheme.onPrimary),
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
                                            arrayOf(colors[2], Color.Transparent)
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
                rememberColumnCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(
                    label = rememberAxisLabelComponent(
                        color = MaterialTheme.colorScheme.onSecondary,
                        margins = dimensions(4.dp),
                        padding = dimensions(8.dp, 4.dp),
                        background = rememberShapeComponent(
                            fill(MaterialTheme.colorScheme.secondary),
                            CorneredShape.rounded(allPercent = 25)
                        )
                    ),
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Outside,
                    title = "Estrogen Level unit/volume",
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
                            dateFormatter.format(value.toLong())
                        } catch (e: Exception) {
                            ""
                        }
                    },
                    guideline = null,
                    itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() }
                )
            ),
            modelProducer = modelProducer
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel = hiltViewModel()) {
    val pad = 16.dp

    TrackerTheme(darkTheme = false) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    title = { Text(text = "Welcome, name") },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Log")
                        }
                    }
                )
            }
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
                ) {
                    Card(
                        modifier = Modifier.padding(pad, pad, pad, pad / 2),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        val chartData = listOf(listOf(1.0, 2.5), listOf(2.0, 3.0), listOf(3.0, 0.0))
                        val scatterData = listOf(listOf(1.0, 1.0), listOf(2.0, 2.0), listOf(3.0, 3.0))
                        VicoGraph(Modifier.fillMaxSize(), chartData, scatterData)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PrevMainScreen() {
    MainScreen()
}

@Preview
@Composable
private fun PrevChart() {
    val chartData = listOf(listOf(1.0, 2.5), listOf(2.0, 3.0), listOf(3.0, 0.0))
    val scatterData = listOf(listOf(1.0, 1.0), listOf(2.0, 2.0))
    VicoGraph(Modifier.fillMaxSize(), chartData, scatterData)
}
