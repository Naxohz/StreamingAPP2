package com.example.streamingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("MainActivity", "onCreate called")

        if (isLoggedIn()) {
            Log.d("MainActivity", "User is logged in")
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        } else {
            Log.d("MainActivity", "User is not logged in")
            findViewById<Button>(R.id.btnLogin).setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            findViewById<Button>(R.id.btnRegister).setOnClickListener {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }
}