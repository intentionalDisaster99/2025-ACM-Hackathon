package com.hacksolotls.tracker.ui.composables

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

@Composable
fun CalculatorScreen(navController: NavController) {

    // Get the Context using LocalContext
    val context = LocalContext.current

    // Initialize PreferencesManager with the current Context
    val preferencesManager = PreferencesManager(context)

    // Retrieve saved values from SharedPreferences
    val isDarkMode by remember { mutableStateOf(preferencesManager.isDarkMode()) }

    // Setting up dark mode or light mode
    TrackerTheme(darkTheme = isDarkMode) {

        // Variables to hold the input values
        var input1 by remember { mutableStateOf("") }
        var input2 by remember { mutableStateOf("") }
        var input3 by remember { mutableStateOf("") }

        // Function to simulate the "Go Home" button behavior
        fun onGoHomeClick() {
            navController.navigate("home")
        }

        // This is where we do the math
        val calculatedValue = remember(input1, input2, input3) {
            // Try to parse the input values as floats
            val amount = input2.toFloatOrNull() ?: -1f
            val dosage = input3.toFloatOrNull() ?: -1f
            val concentration = input1.toFloatOrNull() ?: -1f


            // Making sure that the concentration has been put in
            if (concentration == -1f || (dosage == -1f && amount == -1f)) {
                0f  // Returning 0 if concentration is not entered
            } else if (amount == -1f) {
                // To calculate the amount, we divide the dosage by the concentration
                if (concentration != 0f) {
                    dosage / concentration
                } else {
                    0f  // Prevent division by zero
                }

            } else if (dosage == -1f) {
                // To calculate the dosage, we multiply the concentration by the amount
                if (amount != 0f) {
                    concentration * amount
                } else {
                    0f  // Prevent division by zero
                }
            } else {
                // An error value if neither amount nor dosage is missing
                -2f
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // First Input Field
            InputFieldWithLabel(
                label = "Concentration (mg/mL)",
                value = input1,
                onValueChange = { input1 = it },
                placeholder = { Text(text = "Enter a number") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Second Input Field
            InputFieldWithLabel(
                label = "Amount (mL)",
                value = input2,
                onValueChange = { input2 = it },
                placeholder = { Text(text = "Result: $calculatedValue") } // Dynamically update placeholder
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Third Input Field
            InputFieldWithLabel(
                label = "Dosage (mg)",
                value = input3,
                onValueChange = { input3 = it },
                placeholder = { Text(text = "Result: $calculatedValue") } // Dynamically update placeholder
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Go Home Button
            Button(
                onClick = { onGoHomeClick() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Go Home")
            }
        }
    }
}

@Composable
fun InputFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit // Make the placeholder dynamic
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            placeholder = placeholder, // Use dynamic placeholder here
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun CalculatorScreenPreview() {
//    val navController = rememberNavController()
//
//    // Simple NavHost for preview, this doesn't need to do anything complex
//    NavHost(navController = navController, startDestination = "calculator") {
//        composable("calculator") {
//            CalculatorScreen(navController)  // Pass NavController to your screen
//        }
//        composable("home") {
//            MainScreen(navController=navController)
//        }
//    }
//}
