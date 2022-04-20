package com.example.pinterbest.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pinterbest.presentation.R
import com.example.pinterbest.presentation.databinding.ViewHolderCreatorBinding
import com.example.pinterbest.presentation.models.ProfileViewData

class CreatorsAdapter :
    RecyclerView.Adapter<CreatorsAdapter.ViewHolder>() {
    private var profiles = listOf<ProfileViewData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewHolderCreatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(profiles[position])
    }

    override fun getItemCount() = profiles.size

    fun updateList(profilesList: List<ProfileViewData>) {
        val profilesDiffUtilCallback = CreatorsDiffUtilCallback(profiles, profilesList)
        val profilesDiffResult = DiffUtil.calculateDiff(profilesDiffUtilCallback)
        profiles = profilesList
        profilesDiffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(private val binding: ViewHolderCreatorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: ProfileViewData) {
            binding.name.text = profile.username
            binding.pinsCount.text = profile.pinsCount.toString()
            binding.followers.text = profile.followers.toString()
            binding.followings.text = profile.following.toString()
            Glide.with(binding.imageView.context)
                .load(profile.avatarLink)
                .centerCrop()
                .error(R.drawable.ic_error)
                .into(binding.imageView)
        }
    }
}
