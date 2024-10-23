package com.example.streamingapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    private val userDao by lazy { AppDatabase.getDatabase(applicationContext).userDao() }
    private val loggedInUsername = "USERNAME_DE_TU_SESION" // Reemplaza esto con el nombre de usuario real

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShowUserData()
        }
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun ShowUserData() {
        var user by remember { mutableStateOf<User?>(null) }

        CoroutineScope(Dispatchers.IO).launch {
            user = userDao.getUserByUsername(loggedInUsername)
            // Asegúrate de manejar la navegación a la UI principal desde el hilo correcto
        }

        // Muestra los datos del usuario si están disponibles
        if (user != null) {
            Text("Bienvenido, ${user!!.name} ${user!!.lastName}")
        } else {
            Text("Cargando datos...")
        }
    }
}


