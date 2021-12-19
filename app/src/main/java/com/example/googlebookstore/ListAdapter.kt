package com.example.googlebookstore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.googlebookstore.api.pojos.VolumeInfo


class ListAdapter(
    private val context: Context,
    private val data: ArrayList<VolumeInfo>,
    private val onButtonShowPopupWindowClick: (VolumeInfo) -> Unit
): RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(myItem: VolumeInfo, cb: (VolumeInfo) -> Unit){
            itemView.setOnClickListener {
                cb(myItem)
            }
           // itemView.book_title.text= myItem
            //itemView.book_desc.text= myItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val myListItem = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        return ViewHolder(myListItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(data[position].imageLinks.thumbnail).fitCenter()
            .into(holder.itemView.findViewById(R.id.image));

        holder.bind(data[position],onButtonShowPopupWindowClick)
    }

    override fun getItemCount(): Int =data.count()
}