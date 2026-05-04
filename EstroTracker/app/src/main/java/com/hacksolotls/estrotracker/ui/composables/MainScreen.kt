package com.hacksolotls.estrotracker.ui.composables


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.hacksolotls.estrotracker.ui.composables.charting.ChartSpanDialog
import com.hacksolotls.estrotracker.ui.theme.TrackerTheme
import com.hacksolotls.estrotracker.ui.util.PreferencesManager
import com.hacksolotls.estrotracker.ui.viewmodels.ChartViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.LogDialogViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.MainScreenViewModel
import com.hacksolotls.estrotracker.ui.viewmodels.UiEvent
import com.hacksolotls.estrotracker.ui.viewmodels.ViewSpan
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
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val name by remember { mutableStateOf(preferencesManager.getName() ?: "name") }
    val isDarkMode by remember { mutableStateOf(preferencesManager.isDarkMode()) }

    val log by viewModel.log.observeAsState()
    val state = logDialogViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // Chart Data
    val displayBounds by chartViewModel.displayBounds.observeAsState()
    val logs by chartViewModel.calculationData.observeAsState(emptyList())
    val chartData = remember(logs, displayBounds) {
        displayBounds?.let { bounds -> chartViewModel.logsToChartData(logs, bounds) } ?: emptyList()
    }

    var showSpanDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getMostRecentLog()
        logDialogViewModel.events.collect { event ->
            if (event is UiEvent.ShowSnackBar) snackbarHostState.showSnackbar(event.message)
        }
    }

    TrackerTheme(darkTheme = isDarkMode) {
        ModalNavigationDrawer(
            drawerContent = { DrawerContent(navController, drawerState) },
            drawerState = drawerState,
            gesturesEnabled = false
        ) {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = MaterialTheme.colorScheme.surface,
                topBar = {
                    CenterAlignedTopAppBar(
                        navigationIcon = {
                            IconButton(onClick = { navController.navigate("settings") }) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings")
                            }
                        },
                        title = { Text("Welcome, $name") },
                        actions = {
                            IconButton(onClick = { logDialogViewModel.onEvent(LogEvent.ShowDialog) }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Log")
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // --- Date Selection Row ---
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { chartViewModel.shiftFocus(-1) }) {
                            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showSpanDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${longToDateTime(displayBounds?.first ?: 1)} - ${longToDateTime(displayBounds?.second ?: 1)}",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }

                        IconButton(onClick = { chartViewModel.shiftFocus(1) }) {
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
                        }
                    }

                    // --- The Graph (Main Content) ---
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Takes up available space
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                    ) {
                        if (logs.isEmpty()) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No logs found for this period", style = MaterialTheme.typography.bodyMedium)
                            }
                        } else {
                            Chart(
                                modelProducer = chartViewModel.modelProducer,
                                chartData = chartData,
                                bounds = displayBounds,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    // --- Next Expected Dose Card ---
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        val displayString = if (log == null) {
                            "No recent logs found"
                        } else {
                            val time = Instant.ofEpochMilli(log!!.timestamp.toEpochMilli() + (log?.daysTilNext ?: 0) * 86400000L)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()
                            "Next dose: " + millisToLocalDate(time).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                        }

                        Text(
                            text = displayString,
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                    // --- Bottom Buttons ---
                    Column(
                        modifier = Modifier.padding(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate("calculator") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Calculator")
                        }

                        Button(
                            onClick = { navController.navigate("calendar") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Calendar")
                        }
                    }
                }

                // Dialogs & Permissions
                if (showSpanDialog) {
                    ChartSpanDialog(
                        onDismiss = { span ->
                            showSpanDialog = false
                            chartViewModel.updateSpan(span ?: ViewSpan.Week)
                        },
                        span = chartViewModel.displayConfig.value?.span ?: ViewSpan.Week
                    )
                }

                if (state.value.isAddingLog) {
                    UpsertLogDialog(state = state.value, onEvent = logDialogViewModel::onEvent)
                }
            }
        }
    }
}

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.75f), // Standard drawer width
        drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            "Menu",
            modifier = Modifier.padding(horizontal = 28.dp),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(16.dp))

        NavigationDrawerItem(
            label = { Text("Calendar") },
            selected = false,
            onClick = { scope.launch { drawerState.close() }; navController.navigate("calendar") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Calculator") },
            selected = false,
            onClick = { scope.launch { drawerState.close() }; navController.navigate("calculator") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Settings") },
            selected = false,
            onClick = { scope.launch { drawerState.close() }; navController.navigate("settings") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}