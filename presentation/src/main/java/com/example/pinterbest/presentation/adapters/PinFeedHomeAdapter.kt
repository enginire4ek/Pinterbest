package com.example.pinterbest.presentation.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinterbest.presentation.HomeFragmentDirections
import com.example.pinterbest.presentation.ProfileFragmentDirections
import com.example.pinterbest.presentation.R
import com.example.pinterbest.presentation.databinding.ViewHolderHomeFeedBinding
import com.example.pinterbest.presentation.models.PinObjectViewData
import com.example.pinterbest.presentation.models.PinsListViewData
import kotlin.math.roundToInt

class PinFeedHomeAdapter :
    RecyclerView.Adapter<PinFeedHomeAdapter.ViewHolder>() {
    private var pinObjects = PinsListViewData(listOf())

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
        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections
                .actionHomeFragmentToActualPinFragment(pinObjects.pins[position])
            try {
                it.findNavController().navigate(direction)
            } catch (e: IllegalArgumentException) {
                val dir = ProfileFragmentDirections
                    .actionProfileFragmentToActualPinFragment(pinObjects.pins[position])
                it.findNavController().navigate(dir)
            }
        }
    }

    override fun getItemCount() = pinObjects.pins.size

    fun updateList(pinList: PinsListViewData) {
        val pinsDiffUtilCallback = PinsDiffUtilCallback(pinObjects, pinList)
        val pinsDiffResult = DiffUtil.calculateDiff(pinsDiffUtilCallback)
        pinObjects = pinList
        pinsDiffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: ViewHolderHomeFeedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setImageResource(imageLink: String, imageAvgColor: String) {
            val url = BASE_URL_IMAGES + imageLink
            Glide.with(binding.pinImage.context)
                .load(url)
                .centerCrop()
                .placeholder(ColorDrawable(Color.parseColor("#$imageAvgColor")))
                .error(R.drawable.ic_error)
                .into(binding.pinImage)
        }

        fun bind(pin: PinObjectViewData) {
            val ratioVar = pin.imageHeight / pin.imageWidth.toDouble()
            binding.pinImage.layoutParams.height =
                (binding.pinImage.layoutParams.width * ratioVar).roundToInt()
            setImageResource(pin.imageLink, pin.imageAvgColor)
            binding.pinTitle.text = pin.title
        }
    }

    companion object {
        const val BASE_URL_IMAGES = "https://pinterbest-bucket.s3.eu-central-1.amazonaws.com/"
    }
}
