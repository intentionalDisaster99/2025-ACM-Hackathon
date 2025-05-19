package com.hacksolotls.tracker.ui.composables

import android.icu.util.Calendar
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hacksolotls.tracker.ui.theme.TrackerTheme
import java.time.LocalDate
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun CalendarScreen(modifier: Modifier = Modifier, navController: NavController) {

    // Get the Context using LocalContext
    val context = LocalContext.current

    // Initialize PreferencesManager with the current Context
    val preferencesManager = PreferencesManager(context)

    // Retrieve saved values from SharedPreferences
    val isDarkMode by remember { mutableStateOf(preferencesManager.isDarkMode()) }
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }
    val dateFormatter = remember { SimpleDateFormat("MMMM yyyy", Locale.getDefault()) }


    TrackerTheme(darkTheme = isDarkMode) {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Display the current month name and year
            Text(
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp),
                text = dateFormatter.format(currentMonth.time),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp, color = MaterialTheme.colorScheme.secondary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Row to show month navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth().padding(5.dp, 0.dp, 5.dp, 0.dp),

                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    // Go to previous month
                    val newMonth = currentMonth.clone() as Calendar
                    newMonth.add(Calendar.MONTH, -1)
                    currentMonth = newMonth
                }) {
                    Text(text = "Previous")
                }

                Button(onClick = {
                    // Go to next month
                    val newMonth = currentMonth.clone() as Calendar
                    newMonth.add(Calendar.MONTH, 1)
                    currentMonth = newMonth
                }) {
                    Text(text = "Next")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Grid for displaying the days of the month
            CalendarGrid(currentMonth = currentMonth, navController = navController)

        }
    }
}

@Composable
fun CalendarGrid(currentMonth: Calendar, navController: NavController) {
    val currentDate = LocalDate.now() // Get the current date
    val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfMonth = currentMonth.get(Calendar.DAY_OF_WEEK) - 1 // Adjust so that Sunday is 0

    Column {
        // Days of the week header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (day in arrayOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")) {
                Text(text = day, modifier = Modifier.weight(1f), style = TextStyle(fontWeight = FontWeight.Bold))
            }
        }

        // todo record when meds have been taken

        val takenDays: List<Int> = listOf(1, 3, 5, 7, 10)
        val plannedDays: List<Int> = listOf(1, 5, 7, 3, 10, 12, 23)

        // Empty spaces before the first day of the month
        var dayCounter = 1
        for (i in 0..5) { // Max 6 rows of days (because there can be up to 6 rows in a month)
            Row(modifier = Modifier.fillMaxWidth().height(75.dp)) {
                for (j in 0..6) {

                    // Figuring out if meds have been taken this day
                    val isMedicationTaken = dayCounter in takenDays
                    val isMedicationNotTaken = dayCounter !in takenDays && dayCounter in plannedDays


                    if (i == 0 && j < firstDayOfMonth || dayCounter > daysInMonth) {
                        Spacer(modifier = Modifier.weight(1f)) // Empty space for previous month
                    } else {
                        Text(
                            text = dayCounter.toString(),
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp)
                                .border(
                                    width = 2.dp,
                                    color = if (dayCounter == currentDate.dayOfMonth) Color.Blue else Color.Transparent,
                                    shape = RoundedCornerShape(0.dp)
                                )
                                .background(
                                    if (isMedicationTaken) Color.Green else {
                                        if (isMedicationNotTaken) {
                                            Color.Red
                                        } else {
                                            Color.Transparent
                                        }
                                    }
                                )
                                ,
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                        dayCounter++
                    }
                }
            }

        }

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Card(
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 16.dp / 2),
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
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // A button to take them back to the homepage
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp, 16.dp, 16.dp, 8.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Go Home")
            }
        }
    }
}


//@Preview
//@Composable
//private fun PrevMainScreen() {
//    CalendarScreen()
//}
