package com.hacksolotls.estrotracker.ui.composables


import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hacksolotls.estrotracker.data.LogEvent
import com.hacksolotls.estrotracker.data.util.millisToLocalDate
import com.hacksolotls.estrotracker.ui.theme.TrackerTheme
import com.hacksolotls.estrotracker.ui.util.PreferencesManager
import com.hacksolotls.estrotracker.ui.viewmodels.ChartViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.LogDialogViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.MainScreenViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.UiEvent
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.Line
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
    val logData by chartViewModel.logData.observeAsState(emptyList())

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

    TrackerTheme(darkTheme = isDarkMode) {

        ModalNavigationDrawer(
            drawerContent = {
                DrawerContent(navController, drawerState)
            },
            drawerState = drawerState
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
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
                    Row(modifier = Modifier.weight(0.5f).fillMaxWidth().padding(pad, pad, pad, pad/2)) {
                        Column(modifier = Modifier.weight(1f)) { Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "left", modifier = Modifier.fillMaxSize())}

                        Column(modifier = Modifier.weight(8f)) { Text("04/25/26-05/02/26", modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center)}
                        Column(modifier = Modifier.weight(1f)) { Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "right", modifier = Modifier.fillMaxSize())}
                    }

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

//                            val chartData = chartViewModel.logsToChartDataForGraph(logData)
////                            val chartData =
////                                listOf(listOf(1.0, 2.5), listOf(2.0, 3.0), listOf(3.0, 0.0), listOf(4.0, 3.0), listOf(5.0, 1.0))
//
//                            for (innerList in chartData) {
//                                for (value in innerList) {
//                                    //println("" + value)
//                                }
//                            }
//
//                            // When we get blood work implemented, then we can add this back in
////                             val scatterData = listOf(emptyList<Double>())
//
//                            VicoGraph(Modifier.fillMaxSize(), data = chartData/* , scatterData = scatterData TODO */)

                            LineChart(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 22.dp),
                                data = remember {
                                    listOf(
                                        Line(
                                            label = "Windows",
                                            values = listOf(
                                                100.0,
                                                84.1,
                                                70.7,
                                                59.5,
                                                50.0,
                                                42.0,
                                                35.4,
                                                29.7,
                                                25.0,
                                                21.0,
                                                17.7,
                                                14.9,
                                                12.5,
                                                10.5,
                                                8.8,
                                                7.4,
                                                6.3,
                                                5.3,
                                                4.4,
                                                3.7,
                                                3.1,
                                                2.6,
                                                2.2,
                                                1.9,
                                                1.6
                                            ),
                                            color = SolidColor(Color(0xFF23af92)),
                                            firstGradientFillColor = Color(0xFF2BC0A1).copy(
                                                alpha = .5f
                                            ),
                                            secondGradientFillColor = Color.Transparent,
                                            strokeAnimationSpec = tween(
                                                2000,
                                                easing = EaseInOutCubic
                                            ),
                                            gradientAnimationDelay = 1000,
                                            drawStyle = DrawStyle.Stroke(width = 2.dp),
                                            curvedEdges = false,
                                        )
                                    )
                                },
                                animationMode = AnimationMode.Together(delayBuilder = {
                                    it * 500L
                                }),
                            )
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

