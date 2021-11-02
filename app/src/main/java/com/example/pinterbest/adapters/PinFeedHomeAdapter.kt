package com.example.pinterbest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pinterbest.R
import com.example.pinterbest.data.PinObject

class PinFeedHomeAdapter(private val pinObjects: List<PinObject>) :
    RecyclerView.Adapter<PinFeedHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_home_feed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pinObjects[position])
    }

    override fun getItemCount(): Int {
        return pinObjects.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pinImage: ImageView = itemView.findViewById(R.id.pin_image)
        private val pinTitle: TextView = itemView.findViewById(R.id.pin_title)

        fun bind(pin: PinObject) {
            pinImage.setImageResource(pin.imageLink)
            pinTitle.text = pin.title
        }
    }
}
