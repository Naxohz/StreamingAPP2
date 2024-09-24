package com.example.streamingapp

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class CategoryAdapter(
    private val categories: List<String>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(category: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.textView.text = category
        holder.itemView.setOnClickListener {
            listener.onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.text1)
    }
}
