package com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.android2m_sher_e_panjab.R

class ItemAdapter (
    var list : ArrayList<Item>,
    val onClick: OnItemClick
): RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        var item =list[position]

        holder.name.text = item.categoryName
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        val name = view.findViewById<Button>(R.id.item_button)
    }

    interface OnItemClick {

    }


}