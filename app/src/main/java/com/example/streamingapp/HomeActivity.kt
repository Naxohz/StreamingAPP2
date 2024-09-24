package com.example.streamingapp

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<TextView>(R.id.tvWelcome).text = "Bienvenidos a la Aplicación Regional"

        findViewById<Button>(R.id.btnSports).setOnClickListener { showSportsCategories() }
        findViewById<Button>(R.id.btnNews).setOnClickListener { showNewsCategories() }
    }

    private fun showSportsCategories() {
        val sportsCategories = arrayOf("Fútbol", "Atletismo", "Basketball", "Handball", "Tenis")
        AlertDialog.Builder(this)
            .setTitle("Deportes en la región")
            .setItems(sportsCategories) { _, which ->
                val selectedCategory = sportsCategories[which]
                // Aquí deberías iniciar una nueva actividad o fragmento para mostrar el contenido de la categoría seleccionada
                Toast.makeText(this, "Seleccionaste: $selectedCategory", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showNewsCategories() {
        val newsCategories = arrayOf("Política", "Economía", "Cultura", "Tecnología")
        AlertDialog.Builder(this)
            .setTitle("Noticias de la Región")
            .setItems(newsCategories) { _, which ->
                val selectedCategory = newsCategories[which]
                // Aquí deberías iniciar una nueva actividad o fragmento para mostrar el contenido de la categoría seleccionada
                Toast.makeText(this, "Seleccionaste: $selectedCategory", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}