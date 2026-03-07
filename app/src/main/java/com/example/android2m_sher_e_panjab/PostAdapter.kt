package com.example.android2m_sher_e_panjab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.android2m_sher_e_panjab.PostAdapter.*
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.ItemAdapter.OnItemClick

private var Any.text: String?
    get() {
        TODO()
    }
    set(value) {}
private val ViewHolder.name: Any
    get() {
        TODO()
    }

class PostAdapter (
    var list : ArrayList<Post>,
    val onClick: OnItemClick
):RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    inner class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    }

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
        var Post =list[position]

        holder.name.text = Post.categoryName

    }

    override fun getItemCount(): Int {
        return list.size

    }
}