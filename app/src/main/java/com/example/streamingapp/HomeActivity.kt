package com.example.streamingapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar


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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(onCategorySelected: (String) -> Unit) {
    var showSportsDialog by remember { mutableStateOf(false) }
    var showNewsDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sportsCategories = listOf("Fútbol", "Atletismo", "Basketball", "Handball", "Tenis")
    val newsCategories = listOf("Política", "Economía", "Cultura", "Tecnología")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bienvenidos a la Aplicación Regional") },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showSportsDialog = true }) {
                    Text("Deportes")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showNewsDialog = true }) {
                    Text("Noticias")
                }
            }
        }
    )

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

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                // Cerrar sesión y redirigir al MainActivity
                setLoggedIn(context, false)
                context.startActivity(Intent(context, MainActivity::class.java))
            }
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

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Cerrar Sesión") },
        text = { Text("¿Quieres cerrar sesión?") },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("Sí")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("No")
            }
        }
    )
}

private fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
    val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putBoolean("isLoggedIn", isLoggedIn)
        apply()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen { category ->
    }
}

