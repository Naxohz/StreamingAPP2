package com.example.streamingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.streamingapp.ui.theme.StreamingAPPTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar si el usuario está logueado
        if (isLoggedIn()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            // Si no está logueado, mostrar la pantalla de inicio de sesión y registro
            setContent {
                StreamingAPPTheme {
                    LoginRegisterScreen()
                }
            }
        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRegisterScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Aplicación Streaming Regional") }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp), // Padding general
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Iniciar Sesión")
                }

                Button(
                    onClick = {
                        context.startActivity(Intent(context, RegisterActivity::class.java))
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Registrar")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginRegisterScreen() {
    StreamingAPPTheme {
        LoginRegisterScreen()
    }
}


