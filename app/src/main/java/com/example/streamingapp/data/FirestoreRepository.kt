package com.example.streamingapp

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Category(
    val title: String = "",
    val items: List<String> = listOf(),
    val icons: List<String> = listOf()
)

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()

    // Obtener categorías desde Firestore
    suspend fun getCategories(): List<Category> {
        return try {
            val categoriesRef = db.collection("categories")
            val snapshot = categoriesRef.get().await()
            snapshot.documents.map { document ->
                val title = document.getString("title") ?: ""
                val items = document.get("items") as? List<String> ?: listOf()
                val icons = document.get("iconos") as? List<String> ?: listOf()
                Category(title, items, icons)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // En caso de error, retorna lista vacía
        }
    }
}
