package com.hacksolotls.estrotracker.ui.composables


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hacksolotls.estrotracker.data.LogEvent
import com.hacksolotls.estrotracker.data.util.longToDateTime
import com.hacksolotls.estrotracker.data.util.millisToLocalDate
import com.hacksolotls.estrotracker.ui.composables.charting.Chart
import com.hacksolotls.estrotracker.ui.theme.TrackerTheme
import com.hacksolotls.estrotracker.ui.util.PreferencesManager
import com.hacksolotls.estrotracker.ui.viewmodels.ChartViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.LogDialogViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.MainScreenViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.UiEvent
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    logDialogViewModel: LogDialogViewModel = hiltViewModel(),
    viewModel: MainScreenViewModel = hiltViewModel(),
    chartViewModel: ChartViewModel = hiltViewModel(),
    navController: NavController
) {

    // Observing the log data so that we can actually access it
    //val logData by chartViewModel.logData.observeAsState(emptyList())

    // Get the Context using LocalContext
    val context = LocalContext.current

    // Might could have hilt perform this instead if more entities need access to Prefs
    // Initialize PreferencesManager with the current Context
    val preferencesManager = PreferencesManager(context)

    // Retrieve saved values from SharedPreferences
    val name by remember { mutableStateOf(preferencesManager.getName() ?: "name") }
    val isDarkMode by remember { mutableStateOf(preferencesManager.isDarkMode()) }

    // String to append next dose to
    val dayDisplayString: String = "Next dose: "

    // Tells the viewModel to start updating the most recent log
    LaunchedEffect(Unit) {
        viewModel.getMostRecentLog()
    }

    // Store the most recent log
    val log by viewModel.log.observeAsState()

    // The scope for the drawer
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val pad = 16.dp

    // current LogState
    val state = logDialogViewModel.state.collectAsState()

    // Snackbar stuff
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        logDialogViewModel.events.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    // -----Chart stuff-----
    val displayBounds by chartViewModel.displayBounds.observeAsState()
    val logs by chartViewModel.calculationData.observeAsState(emptyList())

    val chartData = remember(logs, displayBounds) {
        displayBounds?.let { bounds ->
            chartViewModel.logsToChartData(logs, bounds)
        } ?: emptyList()
    }



    TrackerTheme(darkTheme = isDarkMode) {

        ModalNavigationDrawer(
            drawerContent = {
                DrawerContent(navController, drawerState)
            },
            drawerState = drawerState,
            gesturesEnabled = false
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            IconButton(onClick = {
                                navController.navigate("settings")
//                                scope.launch {
//                                    drawerState.apply {
//                                        if (isClosed) open() else close()
//                                    }
//                                }
                            }) {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                            }
                        },
                        title = { Text(text = "Welcome, $name") },
                        actions = {
                            IconButton(onClick = {
                                logDialogViewModel.onEvent(LogEvent.ShowDialog)
                            }) {
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
                    Row(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxWidth()
                            .padding(pad, pad / 4, pad, pad / 4)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(onClick = { chartViewModel.shiftFocus(-1) }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowLeft,
                                    contentDescription = "left",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        Column(modifier = Modifier.fillMaxHeight().weight(8f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                            Text(
                                text = "${
                                    longToDateTime(
                                        displayBounds?.first ?: 1)
                                } - ${
                                    longToDateTime(
                                        displayBounds?.second ?: 1
                                    )
                                }",
                                modifier.fillMaxSize().wrapContentHeight(Alignment.CenterVertically),
                                textAlign = TextAlign.Center,
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(onClick = { chartViewModel.shiftFocus(1) }) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "right",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    // The graph
                    Row(
                        modifier = Modifier
                            .weight(5f)
                            .fillMaxSize()
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(pad, pad, pad, pad / 2)
                                .fillMaxSize(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                            )
                        ) {
                            if (logs.isEmpty()) {
                                Text(
                                    text = "No logs found for this period",
                                    modifier = Modifier.fillMaxSize().wrapContentHeight(Alignment.CenterVertically),
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,

                                )
                            } else {
                                Chart(
                                    modelProducer = chartViewModel.modelProducer,
                                    chartData = chartData,
                                    bounds = displayBounds,
                                    modifier = Modifier.padding(pad / 4, pad / 4, pad / 4, pad)
                                )
                            }
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
                                    "We couldn't read your logs. Sorry!"
                                } else {
                                    var time = log!!.timestamp.toEpochMilli()
                                    time = Instant.ofEpochMilli(
                                        time + (log?.daysTilNext ?: 0) * 86400000
                                    )
                                        .atZone(ZoneId.of("UTC")) // Convert to ZonedDateTime in UTC
                                        .toLocalDate() // Extract the date part (ignoring the time)
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                                        .toEpochMilli()
                                    dayDisplayString + millisToLocalDate(time).format(
                                        DateTimeFormatter.ofPattern("MM/dd/yyyy")
                                    )
                                }


                                Text(
                                    text = displayString,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }

                // Show the add log dialog when the user clicks the button
                if (state.value.isAddingLog) {
                    UpsertLogDialog(
                        state = state.value,
                        onEvent = logDialogViewModel::onEvent,
                        modifier = Modifier
                    )
                }

                NotificationPermissionRequester(
                    onPermissionGranted = {
                        println("Notification permission was granted!")
                        // Now it's safe to schedule notifications
                    },
                    onPermissionDenied = {
                        println("Notification permission was denied. Cannot show reminders.")
                        // Inform the user, maybe guide them to app settings
                    }
                )
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
                .padding(16.dp)
        )

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

