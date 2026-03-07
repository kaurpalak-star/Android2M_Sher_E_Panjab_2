package com.example.android2m_sher_e_panjab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.Item
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.ItemAdapter.OnItemClick
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeAdapter (
    var list : ArrayList<Item>,
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

        var propertyImage = view.findViewById<ImageView>(R.id.ImageProperty)
        var favouriteButton = view.findViewById<FloatingActionButton>(R.id.addFav)
        var location = view.findViewById<TextView>(R.id.location)
        var price = view.findViewById<TextView>(R.id.Price)
        var area =view.findViewById<TextView>(R.id.area)
        var category = view.findViewById<TextView>(R.id.category)
        var checkButton = view.findViewById<Button>(R.id.checkButton)

    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}