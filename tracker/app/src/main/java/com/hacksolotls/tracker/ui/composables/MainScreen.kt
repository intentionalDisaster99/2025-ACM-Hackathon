package com.hacksolotls.tracker.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hacksolotls.tracker.ui.viewmodels.MainScreenViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


import com.hacksolotls.tracker.ui.theme.TrackerTheme

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

                // The graph
                Row(
                    modifier = Modifier
                        .weight(5f)
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

                // The calculator button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    // A button to take them to the calculator
                    Button(
                        onClick = { /* todo */ },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp, 16.dp, 16.dp, 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Calculator")
                    }
                }

                // The calendar button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    // A button to take them to the calculator
                    Button(
                        onClick = { /* todo */ },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp, 16.dp, 16.dp, 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Open Calendar")
                    }
                }

                // Next expected display
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier.padding(pad, pad, pad, pad / 2),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        // Telling them what it is
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)  // Optional, to add some space around the text
                        ) {

                            // Todo get the next date
                            var displayString: String = "Next dose: MMM DD"
                            

                            Text(
                                text = displayString,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }

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

