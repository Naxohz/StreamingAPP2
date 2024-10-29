package com.example.streamingapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.streamingapp.ui.theme.*
import com.example.streamingapp.R

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
    var showAccountMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val categories = listOf(
        Category("Deportes", listOf("Fútbol", "Atletismo", "Basketball", "Handball", "Tenis")),
        Category("Noticias", listOf("Política", "Economía", "Cultura", "Tecnología")),
        Category("Entretenimiento", listOf("Música", "Películas", "Series"))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecciona alguna categoría", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF264653)),
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
                    .background(Color(0xFF101A23))
                    .padding(16.dp)
            ) {
                categories.forEach { category ->
                    Text(
                        text = category.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    LazyRow(
                        modifier = Modifier.padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(category.items) { item ->
                            ChannelCard(item, onClick = { onCategorySelected(item) })
                        }
                    }
                }
            }
        }
    )

    if (showAccountMenu) {
        AccountMenuDialog(
            onDismiss = { showAccountMenu = false },
            onLogoutRequest = { showLogoutDialog = true }
        )
    }

    if (showLogoutDialog) {
        val context = LocalContext.current
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                setLoggedIn(context, false)
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        )
    }
}

@Composable
fun ChannelCard(channelName: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF182834))
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .background(Color.Gray), // Placeholder for image
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Imagen",
                color = Color.White
            )
        }
        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = channelName,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AccountMenuDialog(onDismiss: () -> Unit, onLogoutRequest: () -> Unit) {
    @Composable
    fun rememberLocalContext(): Context {
        return LocalContext.current
    }

    val context = rememberLocalContext()

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Menú de Cuenta", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                TextButton(onClick = {
                    // Navegar a detalles de cuenta
                    val intent = Intent(context, AccountDetailsActivity::class.java)
                    context.startActivity(intent)
                    onDismiss()
                }) {
                    Text("Detalles de la cuenta", color = Color.White)
                }
                TextButton(onClick = {
                    // Solicitar cierre de sesión
                    onLogoutRequest()
                    onDismiss()
                }) {
                    Text("Cerrar sesión", color = Color.White)
                }
            }
        },
        confirmButton = {},
        containerColor = Color(0xFF264653)
    )
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Cerrar Sesión", color = Color.White) },
        text = { Text("¿Quieres cerrar sesión?", color = Color.White) },
        confirmButton = {
            Button(onClick = { onConfirm() }) {
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

data class Category(val title: String, val items: List<String>)
