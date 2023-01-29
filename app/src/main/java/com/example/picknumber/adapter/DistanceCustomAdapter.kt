package com.example.picknumber.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.picknumber.R
import com.example.picknumber.model.SearchModel.Search

class DistanceCustomAdapter(var listData: ArrayList<Search>) : RecyclerView.Adapter<DistanceCustomAdapter.ViewHolder> () {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var distance: TextView = itemView.findViewById(R.id.distance)
        var bankName: TextView = itemView.findViewById(R.id.bankName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.activity_search_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("listData >> ", listData.toString())
        var item = listData[position]

        holder.distance.text = item.distance
        holder.bankName.text = item.bankName

    }

    override fun getItemCount(): Int {
        return listData.size
    }

}