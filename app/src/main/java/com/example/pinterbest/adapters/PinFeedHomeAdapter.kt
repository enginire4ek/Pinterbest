package com.example.pinterbest.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinterbest.R
import com.example.pinterbest.api.ApiEndpoints.BASE_URL_IMAGES
import com.example.pinterbest.databinding.ViewHolderHomeFeedBinding

class PinFeedHomeAdapter :
    RecyclerView.Adapter<PinFeedHomeAdapter.ViewHolder>() {
    private var pinObjects = PinsFeedViewData(listOf())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewHolderHomeFeedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pinObjects.pins[position])
    }

    override fun getItemCount() = pinObjects.pins.size

    fun updateList(pinList: PinsFeedViewData) {
        pinObjects = pinList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ViewHolderHomeFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun getImageResource(imageLink: String, imageAvgColor: String) {
            val url = BASE_URL_IMAGES + imageLink
            Glide.with(binding.pinImage)
                .load(url)
                .centerCrop()
                .placeholder(ColorDrawable(Color.parseColor("#$imageAvgColor")))
                .error(R.drawable.ic_error)
                .into(binding.pinImage)
        }

        fun bind(pin: PinObjectViewData) {
            getImageResource(pin.imageLink, pin.imageAvgColor)
            binding.pinTitle.text = pin.title
        }
    }
}
