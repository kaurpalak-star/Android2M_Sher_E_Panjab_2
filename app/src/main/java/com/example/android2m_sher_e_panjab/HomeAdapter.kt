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
import com.example.android2m_sher_e_panjab.databinding.LayoutHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
class HomeAdapter(
    private var list: List<Property>,
    private val onClick: OnItemClick
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    // Use ViewBinding for cleaner code and better performance
    inner class ViewHolder(val binding: LayoutHomeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutHomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        with(holder.binding) {
            // Binding data
            location.text = currentItem.location
            Price.text = currentItem.price
            area.text = currentItem.area
            category.text = currentItem.propertyType

            // Loading image (Added a placeholder/error for better UX)
            if (currentItem.imageUrls.isNotEmpty()) {
                Glide.with(root.context)
                    .load(currentItem.imageUrls[0])
                    .placeholder(R.drawable.home)
                    .centerCrop()
                    .into(ImageProperty)
            }

            // Click Listeners
            checkButton.setOnClickListener {
                onClick.onItemClick(currentItem)
            }

            addFav.setOnClickListener {
                onClick.onFavouriteClick(currentItem, position)
            }

            // Whole card click (Optional but recommended)
            root.setOnClickListener {
                onClick.onItemClick(currentItem)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateData(newList: List<Property>) {
        // For simple updates, this works, but consider DiffUtil for larger lists
        this.list = newList
        notifyDataSetChanged()
    }

    interface OnItemClick {
        fun onItemClick(property: Property)
        fun onFavouriteClick(property: Property, position: Int)
    }
}