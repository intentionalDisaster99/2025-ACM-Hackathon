package com.hacksolotls.tracker.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.hacksolotls.tracker.ui.theme.TrackerTheme
import androidx.navigation.NavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hacksolotls.tracker.ui.composables.PreferencesManager

@Composable
fun SettingsScreen(navController: NavController) {

    // Get the Context using LocalContext
    val context = LocalContext.current

    // Initialize PreferencesManager with the current Context
    val preferencesManager = PreferencesManager(context)

    // Retrieve saved values from SharedPreferences
    var name by remember { mutableStateOf(preferencesManager.getName() ?: "") }
    var isDarkMode by remember { mutableStateOf(preferencesManager.isDarkMode()) }

    TrackerTheme(darkTheme = isDarkMode) {

        // Function to simulate the "Go Home" button behavior
        fun onGoHomeClick() {
            navController.navigate("home")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dark Mode Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Space between elements
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dark Mode",
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .wrapContentWidth(Alignment.End) // Right-aligned text
                )

                // Dark Mode Switch
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { newValue ->
                        isDarkMode = newValue
                        preferencesManager.saveDarkMode(newValue) // Save when changed
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name Input Field
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Space between elements
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name",
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .wrapContentWidth(Alignment.Start)
                )

                // TODO This needs to be updated to make the text show in landscape mode on all devices
                OutlinedTextField(
                    value = name,
                    onValueChange = { newName ->
                        name = newName
                        preferencesManager.saveName(newName)
                    },
                    textStyle = TextStyle(color = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .fillMaxHeight(0.075f)
                        .height(56.dp),
                    placeholder = { Text(text = "Enter your name") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    isError = name.isEmpty(),
                    label = { Text(text = "Name") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate Home
            Button(
                onClick = { onGoHomeClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Go Home")
            }
        }
    }
}
