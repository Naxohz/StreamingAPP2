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
                title = { Text("Bienvenidos a la Aplicación Regional", color = Color.White, fontSize = 20.sp) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF111A22)),
                actions = {
                    IconButton(onClick = { showAccountMenu = !showAccountMenu }) {
                        Icon(Icons.Filled.Person, contentDescription = "Menú de cuenta", tint = Color.White)
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF111A22))
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showSportsDialog = true },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(200.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF243647))
                ) {
                    Text("Deportes", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showNewsDialog = true },
                    modifier = Modifier
                        .padding(8.dp)
                        .size(200.dp, 50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF243647))
                ) {
                    Text("Noticias", color = Color.White)
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
            title = { Text("Menú de Cuenta", color = Color.White) },
            text = {
                Column {
                    TextButton(onClick = {
                        context.startActivity(Intent(context, AccountDetailsActivity::class.java))
                        showAccountMenu = false
                    }) {
                        Text("Detalles de la cuenta", color = Color.White)
                    }
                    TextButton(onClick = {
                        showLogoutDialog = true
                        showAccountMenu = false
                    }) {
                        Text("Cerrar sesión", color = Color.White)
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
        title = { Text(text = title, color = Color.White) },
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
        title = { Text(text = "Cerrar Sesión", color = Color.White) },
        text = { Text("¿Quieres cerrar sesión?", color = Color.White) },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("Sí", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("No", color = Color.White)
            }
        },
        containerColor = Color(0xFF111A22)
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