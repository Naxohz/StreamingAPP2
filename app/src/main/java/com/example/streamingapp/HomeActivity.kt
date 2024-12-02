package com.example.streamingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class HomeActivity : ComponentActivity() {
    private val firestoreRepository = FirestoreRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavHost(navController)
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("details/{category}/{subCategory}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Sin categoría"
            val subCategory = backStackEntry.arguments?.getString("subCategory") ?: "Sin subcategoría"
            CategoryDetailsScreen(navController, category, subCategory)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val firestoreRepository = FirestoreRepository()
    val categoriesState = remember { mutableStateOf<List<Category>>(emptyList()) }

    // Cargar categorías desde Firestore
    LaunchedEffect(true) {
        categoriesState.value = firestoreRepository.getCategories()
    }

    var showAccountMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Selecciona alguna categoría",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF264653)),
                actions = {
                    IconButton(onClick = { showAccountMenu = !showAccountMenu }) {
                        Icon(Icons.Filled.Person, contentDescription = "Menú de cuenta", tint = Color.White)
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF101A23))
                    .padding(paddingValues)
            ) {
                // Recorrer las categorías
                categoriesState.value.forEach { category ->
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = category.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    LazyRow(
                        modifier = Modifier.padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(category.items.zip(category.icons)) { (item, iconName) ->
                            val iconResId = getDrawableResourceId(iconName)
                            ChannelCard(
                                channelName = item,
                                imageRes = iconResId,
                                onClick = { navController.navigate("details/${category.title}/$item") }
                            )
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
fun ChannelCard(channelName: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF182834))
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Icono de $channelName",
                modifier = Modifier.fillMaxSize()
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
fun getDrawableResourceId(iconName: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(iconName, "drawable", context.packageName).takeIf { it != 0 } ?: 0
}

@Composable
fun AccountMenuDialog(onDismiss: () -> Unit, onLogoutRequest: () -> Unit) {
    val context = LocalContext.current

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

fun setLoggedIn(context: Context, isLoggedIn: Boolean)
{
    val sharedPref = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putBoolean("isLoggedIn", isLoggedIn)
        apply()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Simular los datos que obtendrás de Firestore
    val categoriesState = listOf(
        Category(
            title = "Deportes",
            items = listOf("Fútbol", "Baloncesto", "Tenis"),
            icons = listOf("futbol_icon", "basketball_icon", "tenis_icon")
        ),
        Category(
            title = "Noticias",
            items = listOf("Política", "Cultura", "Economía"),
            icons = listOf("politica_icon", "cultura_icon", "economia_icon")
        ),
        Category(
            title = "Entretenimiento",
            items = listOf("Películas", "Series", "Música"),
            icons = listOf("peliculas_icon", "series_icon", "musica_icon")
        )
    )

    // Simulamos la carga de categorías
    HomeScreenPreviewContent(categoriesState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenPreviewContent(categoriesState: List<Category>) {
    var showAccountMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Selecciona alguna categoría",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF264653)),
                actions = {
                    IconButton(onClick = { showAccountMenu = !showAccountMenu }) {
                        Icon(Icons.Filled.Person, contentDescription = "Menú de cuenta", tint = Color.White)
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF101A23))
                    .padding(paddingValues)
            ) {
                categoriesState.forEach { category ->
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = category.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                    )
                    LazyRow(
                        modifier = Modifier.padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(category.items.zip(category.icons)) { (item, iconName) ->
                            val iconResId = getDrawableResourceId(iconName)
                            ChannelCard(
                                channelName = item,
                                imageRes = iconResId,
                                onClick = { /* Simula la navegación */ }
                            )
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
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = { /* Simula el cierre de sesión */ }
        )
    }
}
