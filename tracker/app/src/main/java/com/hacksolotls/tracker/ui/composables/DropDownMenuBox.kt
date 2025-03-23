package com.hacksolotls.tracker.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDownMenuBox(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    label: String = "Select an Item"
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val selectedText = selectedItem?.let { itemLabel(it) } ?: label

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {/* No op */ },
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Medication form drop-down menu"
                )
            },
            modifier = Modifier
                .menuAnchor()
//                .fillMaxWidth()
                .clickable { expanded = !expanded })

        DropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier
        ) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text(text = itemLabel(item)) }, onClick = {
                    onItemSelected(item)
                    expanded = false
                })
            }
        }
    }
}