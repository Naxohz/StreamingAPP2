package com.example.streamingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen { category ->
                Toast.makeText(this, "Seleccionaste: $category", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun HomeScreen(onCategorySelected: (String) -> Unit) {
    var showSportsDialog by remember { mutableStateOf(false) }
    var showNewsDialog by remember { mutableStateOf(false) }

    val sportsCategories = listOf("Fútbol", "Atletismo", "Basketball", "Handball", "Tenis")
    val newsCategories = listOf("Política", "Economía", "Cultura", "Tecnología")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenidos a la Aplicación Regional", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showSportsDialog = true }) {
            Text("Deportes")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { showNewsDialog = true }) {
            Text("Noticias")
        }
    }

    if (showSportsDialog) {
        CategoryDialog(
            title = "Deportes en la región",
            categories = sportsCategories,
            onDismiss = { showSportsDialog = false },
            onCategorySelected = onCategorySelected
        )
    }

    if (showNewsDialog) {
        CategoryDialog(
            title = "Noticias de la Región",
            categories = newsCategories,
            onDismiss = { showNewsDialog = false },
            onCategorySelected = onCategorySelected
        )
    }
}

@Composable
fun CategoryDialog(
    title: String,
    categories: List<String>,
    onDismiss: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = {
            Column {
                categories.forEach { category ->
                    TextButton(onClick = {
                        onCategorySelected(category)
                        onDismiss()
                    }) {
                        Text(text = category)
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen { category ->
    }
}
