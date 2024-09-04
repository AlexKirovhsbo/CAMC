package com.example.camc.view.selections

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.camc.view.main_screen.MainViewModel

class SelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // Create NavController
            SelectionScreen(viewModel = viewModel(), navController = navController)
        }
    }
}

@Composable
fun SelectionScreen(viewModel: MainViewModel, navController: NavController) {
    val options = listOf("Stehen", "Gehen", "Laufen", "Auto", "Zug")
    val selectedOptions = viewModel.selectedOptions
    var mobileNumber by viewModel.mobileNumber

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 80.dp) // Adjust padding as needed
    ) {
        // Container for the checkboxes
        Column(
            modifier = Modifier
                .weight(1f) // Take up available space
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Padding between checkboxes and input field
        ) {
            options.forEach { option ->
                val isSelected = selectedOptions.contains(option)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = isSelected,
                            onValueChange = { checked ->
                                if (checked) {
                                    viewModel.addSelection(option)
                                } else {
                                    viewModel.removeSelection(option)
                                }
                            }
                        )
                        .padding(8.dp)
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = null // We handle toggleable
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option)
                }
            }
        }

        // OutlinedTextField directly beneath the checkboxes
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            label = { Text("Handynummer") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Padding before the button
        )

        // Button to save the mobile number and navigate back
        Button(onClick = {
            viewModel.saveMobileNumber(mobileNumber)
            navController.navigate("mainscreen") {
                popUpTo("mainscreen") { inclusive = true } // This will clear the back stack to avoid multiple instances
            }
        }) {
            Text("Weiter")
        }
    }
}
