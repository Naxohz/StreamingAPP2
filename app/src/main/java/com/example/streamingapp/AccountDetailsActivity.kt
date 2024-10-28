package com.example.streamingapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.streamingapp.data.UserEntity
import com.example.streamingapp.data.UserViewModel

class AccountDetailsActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recupera el nombre de usuario y la contraseña desde SharedPreferences
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("logged_in_username", null)
        val password = sharedPref.getString("logged_in_password", null)

        // Si el usuario está logeado, muestra los detalles
        setContent {
            if (username != null && password != null) {
                AccountDetailsScreen(username, password)
            } else {
                Text("No se pudo obtener el usuario logeado.")
            }
        }
    }

    @Composable
    fun AccountDetailsScreen(username: String, password: String) {
        var user by remember { mutableStateOf<UserEntity?>(null) }

        // Recupera los detalles del usuario desde la base de datos
        LaunchedEffect(username, password) {
            userViewModel.getUserByUsernameAndPassword(username, password) { result ->
                user = result
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF111A22)),
                contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Detalles De La Cuenta",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(66.dp))

                if (user != null) {
                    AccountDetailItem(label = "Nombre de usuario:", value = user!!.username)
                    AccountDetailItem(label = "Nombre:", value = user!!.name)
                    AccountDetailItem(label = "Apellido:", value = user!!.lastName)
                    AccountDetailItem(label = "Email:", value = user!!.email)
                    AccountDetailItem(label = "Ubicación:", value = user!!.location)
                } else {
                    Text("Cargando información del usuario...", color = Color.White)
                }
            }
        }
    }

    @Composable
    fun AccountDetailItem(label: String, value: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF243647)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "$label $value",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewAccountDetailsScreen() {
        val simulatedUser = UserEntity(1, "jdoe", "John", "Doe", "jdoe@example.com", "1234", "Arica y Parinacota")

        AccountDetailsScreenPreview(simulatedUser)
    }

    @Composable
    fun AccountDetailsScreenPreview(user: UserEntity) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF111A22)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Detalles De La Cuenta",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(66.dp))

                AccountDetailItem(label = "Nombre de usuario:", value = user.username)
                AccountDetailItem(label = "Nombre:", value = user.name)
                AccountDetailItem(label = "Apellido:", value = user.lastName)
                AccountDetailItem(label = "Email:", value = user.email)
                AccountDetailItem(label = "Ubicación:", value = user.location)
            }
        }
    }
}







