package com.hacksolotls.tracker.ui.composables


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hacksolotls.tracker.data.LogEvent
import com.hacksolotls.tracker.data.util.millisToLocalDate
import com.hacksolotls.tracker.ui.theme.TrackerTheme
import com.hacksolotls.tracker.ui.util.PreferencesManager
import com.hacksolotls.tracker.ui.viewmodels.ChartViewModel
import com.hacksolotls.tracker.ui.viewmodels.LogDialogViewModel
import com.hacksolotls.tracker.ui.viewmodels.MainScreenViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    logDialogViewModel: LogDialogViewModel = hiltViewModel(),
    viewModel: MainScreenViewModel = hiltViewModel(),
    chartViewModel: ChartViewModel = hiltViewModel(),
    navController: NavController
) {

    // Observing the log data so that we can actualy access it
    val logData by chartViewModel.logData.observeAsState(emptyList())

    // Get the Context using LocalContext
    val context = LocalContext.current

    // Initialize PreferencesManager with the current Context
    val preferencesManager = PreferencesManager(context)

    // Retrieve saved values from SharedPreferences
    val name by remember { mutableStateOf(preferencesManager.getName() ?: "name") }
    val isDarkMode by remember { mutableStateOf(preferencesManager.isDarkMode()) }

    // Remember next dose date
    val daysTilNext by remember { mutableLongStateOf(0L) }

    var dayDisplayString: String = "Next dose: "

    LaunchedEffect(Unit) {
        viewModel.getMostRecentLog()
    }

    val log by viewModel.log.observeAsState()

    // The scope for the drawer
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val pad = 16.dp

    val state = logDialogViewModel.state.collectAsState()

    TrackerTheme(darkTheme = isDarkMode) {

        ModalNavigationDrawer(
            drawerContent = {
                DrawerContent(navController, drawerState)
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        title = { Text(text = "Welcome, $name") },
                        actions = {
                            IconButton(onClick = {
                                logDialogViewModel.onEvent(LogEvent.ShowDialog)
                            }) {
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

                            val chartData = chartViewModel.logsToChartDataForGraph(logData)
//                            val chartData =
//                                listOf(listOf(1.0, 2.5), listOf(2.0, 3.0), listOf(3.0, 0.0), listOf(4.0, 3.0), listOf(5.0, 1.0))

                            for (innerList in chartData) {
                                for (value in innerList) {
                                    println("" + value)
                                }
                            }

                            // When we get blood work implemented, then we can add this back in
//                             val scatterData = listOf(emptyList<Double>())

                            VicoGraph(Modifier.fillMaxSize(), data = chartData/* , scatterData = scatterData TODO */)
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

                                val displayString = if (log == null) {
                                    "Unknown"
                                } else {
                                    var time = log!!.timestamp.toEpochMilli()
                                    time = Instant.ofEpochMilli(time + (log?.daysTilNext ?: 0) * 86400000)
                                        .atZone(ZoneId.of("UTC")) // Convert to ZonedDateTime in UTC
                                        .toLocalDate() // Extract the date part (ignoring the time)
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                                        .toEpochMilli()
                                    dayDisplayString + millisToLocalDate(time).format(
                                        DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                                }


                                Text(
                                    text = displayString,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
                if (state.value.isAddingLog) {
                    UpsertLogDialog(
                        state = state.value,
                        onEvent = logDialogViewModel::onEvent,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}


@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()


    Surface(
        modifier = Modifier
            .fillMaxWidth(0.4f) // Set width to 75% of the screen
            .fillMaxHeight()
            .padding(end = 8.dp), // Padding to prevent clipping
        shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp), // Rounded corners
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
            .fillMaxWidth(0.3f)
            .fillMaxHeight()
            .padding(16.dp))

        {
            Text(text = "Stuffs", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))


            // Navigation Options
            NavigationDrawerItem(label = { Text("Calendar") }, selected = false, onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("calendar")
            })

            NavigationDrawerItem(label = { Text("Calculator") }, selected = false, onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("calculator")
            })

            NavigationDrawerItem(label = { Text("Settings") }, selected = false, onClick = {
                scope.launch { drawerState.close() }
                navController.navigate("settings")
            })
        }
    }
}

