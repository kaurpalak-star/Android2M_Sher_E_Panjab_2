package com.example.android2m_sher_e_panjab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
// Ensure these imports match your actual project structure
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.ItemAdapter.OnItemClick
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeAdapter (
    var list : ArrayList<Property>,
    val onClick: OnItemClick
): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_home, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        var propertyImage: ImageView = view.findViewById(R.id.ImageProperty)
        var favouriteButton: FloatingActionButton = view.findViewById(R.id.addFav)
        var location: TextView = view.findViewById(R.id.location)
        var price: TextView = view.findViewById(R.id.Price)
        var area: TextView = view.findViewById(R.id.area)
        var category: TextView = view.findViewById(R.id.category)
        var checkButton: Button = view.findViewById(R.id.checkButton)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {
        val currentItem = list[position]

        // Binding data to views
        holder.location.text = currentItem.location
        holder.price.text = currentItem.price
        holder.area.text = currentItem.area
        holder.category.text = currentItem.propertyType

        // If using a library like Glide or Picasso for images:
         Glide.with(holder.view.context).load(currentItem.imageUrls[0]).into(holder.propertyImage)
        // Or setting a local resource:
//        holder.propertyImage.setImageResource(currentItem.imageRes)

        // Setting up click listeners

        holder.checkButton.setOnClickListener {
            // You can add specific logic for the button here or use the same interface
            onClick.onItemClick(currentItem)
        }

        holder.favouriteButton.setOnClickListener {
            // Handle favorite logic
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // Optional: Helper method to update the list
    fun updateData(newList: ArrayList<Property>) {
        this.list = newList
        notifyDataSetChanged()
    }


    interface OnItemClick {
        fun onItemClick(currentItem : Property)
    }
}