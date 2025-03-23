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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext



import com.hacksolotls.tracker.ui.theme.TrackerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel = hiltViewModel(), navController: NavController) {

    // Get the Context using LocalContext
    val context = LocalContext.current

    // Initialize PreferencesManager with the current Context
    val preferencesManager = PreferencesManager(context)

    // Retrieve saved values from SharedPreferences
    val name by remember { mutableStateOf(preferencesManager.getName() ?: "name") }
    val isDarkMode by remember { mutableStateOf(preferencesManager.isDarkMode()) }

    val pad = 16.dp

    // Initialize the DrawerState and CoroutineScope
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    TrackerTheme(darkTheme = isDarkMode) {

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Stuffs", modifier = Modifier.padding(16.dp))
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text = "Calculator") },
                        selected = false,
                        onClick = { navController.navigate("calculator") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Calendar") },
                        selected = false,
                        onClick = { navController.navigate("calendar") }
                    )
                    NavigationDrawerItem(
                        label = { Text(text = "Settings") },
                        selected = false,
                        onClick = { navController.navigate("settings") }
                    )
                }
            },
        ) {

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        // Toggle drawer based on its current state
                                        if (drawerState.isClosed) {
                                            drawerState.open()
                                        } else {
                                            drawerState.close()
                                        }
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },

                        title = { Text(text = "Welcome, $name") },
                        actions = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Log"
                                )
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
                            val chartData =
                                listOf(listOf(1.0, 2.5), listOf(2.0, 3.0), listOf(3.0, 0.0))
                            val scatterData =
                                listOf(listOf(1.0, 1.0), listOf(2.0, 2.0), listOf(3.0, 3.0))
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
                            onClick = { navController.navigate("calculator") },
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
                            onClick = { navController.navigate("calendar") },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp, 16.dp, 16.dp, 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Calendar")
                        }
                    }

                    // Next expected display
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Card(
                            modifier = Modifier.padding(pad, pad, pad, pad),
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
}

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Navigation", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Options
        NavigationDrawerItem(label = { Text("Home") }, selected = false, onClick = {
            scope.launch { drawerState.close() }
            navController.navigate("home")
        })

        NavigationDrawerItem(label = { Text("Calculator") }, selected = false, onClick = {
            scope.launch { drawerState.close() }
            navController.navigate("calculator")
        })
    }
}

