package com.example.streamingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.PasswordVisualTransformation
import kotlinx.coroutines.launch

class RegisterActivity : ComponentActivity() {
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = AppDatabase.getDatabase(this)

        setContent {
            RegisterScreen()
        }
    }

    @Composable
    fun RegisterScreen() {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        Column {
            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Usuario") }
            )
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(onClick = {
                coroutineScope.launch {
                    db.userDao().insert(User(username = username.value, password = password.value))
                }
            }) {
                Text("Registrar")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewRegisterScreen() {
        RegisterScreen()
    }
}


