package com.hacksolotls.estrotracker.ui.composables.charting

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisGuidelineComponent
import com.patrykandpatrick.vico.compose.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.LineCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.Insets
import com.patrykandpatrick.vico.compose.common.LayeredComponent
import com.patrykandpatrick.vico.compose.common.MarkerCornerBasedShape
import com.patrykandpatrick.vico.compose.common.component.ShapeComponent
import com.patrykandpatrick.vico.compose.common.component.TextComponent
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent

val MarkerValueFormatter = DefaultCartesianMarker.ValueFormatter { _, targets ->
    val target = targets.first()

    // 1. Get the X (timestamp)
    val date = java.time.Instant.ofEpochMilli(target.x.toLong())
        .atZone(java.time.ZoneId.systemDefault())
        .format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, HH:00"))

    // 2. Get the Y (dosage level)
    // In Vico, targets for line charts contain 'points'
    val yValue = if (target is LineCartesianLayerMarkerTarget) {
        target.points.firstOrNull()?.entry?.y ?: 0f
    } else {
        0f
    }

    "$date: ${"%.2f".format(yValue)}"
}

@Composable
fun rememberMarker(
    valueFormatter: DefaultCartesianMarker.ValueFormatter = DefaultCartesianMarker.ValueFormatter.default(),
    showIndicator: Boolean = true,
): CartesianMarker {
    val labelBackgroundShape = MarkerCornerBasedShape(CircleShape)
    val labelBackground = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.surfaceVariant),
        shape = labelBackgroundShape,
        strokeFill = Fill(MaterialTheme.colorScheme.outline),
        strokeThickness = 1.dp,
    )
    val label = rememberTextComponent(
        style = TextStyle(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
        ),
        padding = Insets(8.dp, 4.dp),
        background = labelBackground,
        minWidth = TextComponent.MinWidth.fixed(40.dp),
    )
    val indicatorFrontComponent = rememberShapeComponent(
        fill = Fill(MaterialTheme.colorScheme.surface),
        shape = CircleShape
    )
    val guideline = rememberAxisGuidelineComponent()

    return rememberDefaultCartesianMarker(
        label = label,
        valueFormatter = valueFormatter,
        indicator = if (showIndicator) {
            { color ->
                LayeredComponent(
                    back = ShapeComponent(Fill(color.copy(alpha = 0.15f)), CircleShape),
                    front = LayeredComponent(
                        back = ShapeComponent(fill = Fill(color), shape = CircleShape),
                        front = indicatorFrontComponent,
                        padding = Insets(5.dp),
                    ),
                    padding = Insets(10.dp),
                )
            }
        } else null,
        indicatorSize = 36.dp,
        guideline = guideline,
    )
}