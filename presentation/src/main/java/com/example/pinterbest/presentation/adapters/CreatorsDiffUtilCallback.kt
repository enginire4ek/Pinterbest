package com.example.pinterbest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.pinterbest.presentation.models.ProfileViewData

class CreatorsDiffUtilCallback(
    private val oldList: List<ProfileViewData>,
    private val newList: List<ProfileViewData>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGroup = oldList[oldItemPosition]
        val newGroup = newList[newItemPosition]
        return oldGroup.id == newGroup.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGroup = oldList[oldItemPosition]
        val newGroup = newList[newItemPosition]
        return (
            oldGroup.username == newGroup.username &&
                oldGroup.avatarLink == newGroup.avatarLink &&
                oldGroup.following == newGroup.following &&
                oldGroup.followers == newGroup.followers &&
                oldGroup.boardsCount == newGroup.boardsCount &&
                oldGroup.pinsCount == newGroup.pinsCount
            )
    }
}
