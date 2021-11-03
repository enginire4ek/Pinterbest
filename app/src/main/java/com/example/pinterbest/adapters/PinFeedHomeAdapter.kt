package com.example.pinterbest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pinterbest.data.PinObject
import com.example.pinterbest.databinding.ViewHolderHomeFeedBinding

class PinFeedHomeAdapter(private val pinObjects: List<PinObject>) :
    RecyclerView.Adapter<PinFeedHomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewHolderHomeFeedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pinObjects[position])
    }

    override fun getItemCount(): Int {
        return pinObjects.size
    }

    class ViewHolder(private val binding: ViewHolderHomeFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pin: PinObject) {
            binding.pinImage.setImageResource(pin.imageLink)
            binding.pinTitle.text = pin.title
        }
    }
}
