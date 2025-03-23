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

@Composable
fun CalculatorScreen() {

    TrackerTheme(darkTheme = false) {
        // Variables to hold the input values
        var input1 by remember { mutableStateOf("") }
        var input2 by remember { mutableStateOf("") }
        var input3 by remember { mutableStateOf("") }

        // Function to simulate the "Go Home" button behavior
        fun onGoHomeClick() {
            // TODO
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
                onValueChange = { input1 = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Second Input Field
            InputFieldWithLabel(
                label = "Amount (mL)",
                value = input2,
                onValueChange = { input2 = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Third Input Field
            InputFieldWithLabel(
                label = "Dosage (mg)",
                value = input3,
                onValueChange = { input3 = it }
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
    onValueChange: (String) -> Unit
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
            placeholder = { Text(text = "Enter number") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorScreenPreview() {
    CalculatorScreen()
}
