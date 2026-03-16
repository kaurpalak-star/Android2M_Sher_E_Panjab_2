package com.example.android2m_sher_e_panjab.BottomNavigation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android2m_sher_e_panjab.Property
import com.example.android2m_sher_e_panjab.R

class FavGridAdapter(
    private val list: List<Property>,
    private val listener: OnItemClick
) : RecyclerView.Adapter<FavGridAdapter.ViewHolder>() {

    interface OnItemClick {
        fun onPropertyClick(property: Property)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.ivProperty)
        val price: TextView = view.findViewById(R.id.tvPrice)
        val name: TextView = view.findViewById(R.id.tvName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val property = list[position]
        holder.name.text = property.name
        holder.price.text = property.price

        if (property.imageUrls.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(property.imageUrls[0])
                .placeholder(R.drawable.img_7)
                .into(holder.image)
        }

        holder.itemView.setOnClickListener { listener.onPropertyClick(property) }
    }

    override fun getItemCount(): Int = list.size
}