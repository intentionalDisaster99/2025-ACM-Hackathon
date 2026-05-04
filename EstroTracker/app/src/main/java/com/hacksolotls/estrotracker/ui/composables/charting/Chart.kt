package com.hacksolotls.estrotracker.ui.composables.charting

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.hacksolotls.estrotracker.data.db.ChartData
import com.hacksolotls.estrotracker.data.util.HorizontalAxisValueFormatter
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.LineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.cartesian.Scroll
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.cartesian.Zoom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.compose.common.DashedShape
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent

@Composable
fun Chart(
    modelProducer: CartesianChartModelProducer,
    chartData: List<ChartData>,
    bounds: Pair<Long, Long>?,
    modifier: Modifier
) {

    LaunchedEffect(chartData) {
        modelProducer.runTransaction {
            if (chartData.isNotEmpty()) {
                lineSeries {
                    series(
                        x = chartData.map { it.timestamp },
                        y = chartData.map { it.eLevel }
                    )
                }
            } else {
                // Do nothing
            }
        }
    }

    val rangeProvider = remember(bounds) {
        bounds?.let { (min, max) ->
            CartesianLayerRangeProvider.fixed(
                minX = min.toDouble(),
                maxX = max.toDouble()
            )
        } ?: CartesianLayerRangeProvider.auto()
    }

    val marker = rememberMarker(valueFormatter = MarkerValueFormatter)

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineProvider = LineCartesianLayer.LineProvider.series(
                    LineCartesianLayer.rememberLine(
                        fill = LineCartesianLayer.LineFill.single(Fill(MaterialTheme.colorScheme.onSecondaryContainer)),
                        areaFill =
                            LineCartesianLayer.AreaFill.single(
                                Fill(
                                    Brush.verticalGradient(listOf(MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.4f), Color.Transparent))
                                )
                            ),
                        interpolator = LineCartesianLayer.Interpolator.catmullRom(alpha = 0.2f)
                    )
                ),
                rangeProvider = rangeProvider // This forces the X-axis view
            ),
            bottomAxis = HorizontalAxis.rememberBottom(
                label = rememberAxisLabelComponent(
                    style = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer),
                    margins = Insets(4.dp),
                    padding = Insets(4.dp, 4.dp),
                ),
                valueFormatter = HorizontalAxisValueFormatter, // The formatter we made earlier
                guideline = rememberAxisGuidelineComponent(
                    fill = Fill(MaterialTheme.colorScheme.outline),
                    thickness = 1.dp,
                    shape = DashedShape(shape = CircleShape, dashLength = 1.dp)
                ),
            ),
            startAxis = VerticalAxis.rememberStart(
                label = rememberAxisLabelComponent(
                    style = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer),
                    margins = Insets(4.dp),
                    padding = Insets(4.dp, 4.dp),
                ),
                title = { "Estrogen Level (pg/mL)" },
                titleComponent = rememberTextComponent(TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer)),
                valueFormatter = CartesianValueFormatter { _, value, _ ->
                    (value.toInt()).toString()
                },
                itemPlacer = VerticalAxis.ItemPlacer.step({ 5.0 }),
                guideline = rememberAxisGuidelineComponent(
                    fill = Fill(MaterialTheme.colorScheme.outline),
                    thickness = 1.dp,
                    shape = DashedShape(shape = CircleShape, dashLength = 1.dp)
                ),
            ),
            marker = marker
        ),
        modelProducer = modelProducer,
        modifier = modifier.fillMaxSize().pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    // If the user has a finger down on the chart,
                    // tell parents not to intercept the movement
                    event.changes.forEach { it.consume() }
                }
            }
        },//.height(250.dp)
        scrollState = rememberVicoScrollState(
            scrollEnabled = false,
            initialScroll = Scroll.Absolute.Start
        ),
        zoomState = rememberVicoZoomState(
            zoomEnabled = false,
            initialZoom = Zoom.fixed(0f)
        )
    )
}

