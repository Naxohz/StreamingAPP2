package com.example.streamingapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


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
    var showAccountMenu by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sportsCategories = listOf("Fútbol", "Atletismo", "Basketball", "Handball", "Tenis")
    val newsCategories = listOf("Política", "Economía", "Cultura", "Tecnología")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bienvenidos a la Aplicación Regional") },
                actions = {
                    IconButton(onClick = { showAccountMenu = !showAccountMenu }) {
                        Icon(Icons.Filled.Person, contentDescription = "Menú de cuenta")
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

                // Botones para mostrar categorías
                Button(
                    onClick = { showSportsDialog = true },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(200.dp, 50.dp) // Ajusta el tamaño del botón
                ) {
                    Text("Deportes")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showNewsDialog = true },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(200.dp, 50.dp) // Ajusta el tamaño del botón
                ) {
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
                setLoggedIn(context, false)
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        )
    }

    if (showAccountMenu) {
        AlertDialog(
            onDismissRequest = { showAccountMenu = false },
            title = { Text("Menú de Cuenta") },
            text = {
                Column {
                    TextButton(onClick = {
                        context.startActivity(Intent(context, AccountDetailsActivity::class.java))
                        showAccountMenu = false
                    }) {
                        Text("Detalles de la cuenta")
                    }
                    TextButton(onClick = {
                        showLogoutDialog = true
                        showAccountMenu = false
                    }) {
                        Text("Cerrar sesión")
                    }
                }
            },
            confirmButton = {}
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
            LazyColumn {
                items(categories) { category ->
                    CategoryCard(category = category, onClick = {
                        onCategorySelected(category)
                        onDismiss()
                    })
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun CategoryCard(category: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3B334D))
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = category, color = Color.White)
        }
    }
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

fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
    val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putBoolean("isLoggedIn", isLoggedIn)
        apply()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen { category -> }
}


