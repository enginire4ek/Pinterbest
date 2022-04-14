package com.example.pinterbest.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.pinterbest.presentation.models.PinsListViewData

class PinsDiffUtilCallback(
    private val oldList: PinsListViewData,
    private val newList: PinsListViewData
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.pins.size

    override fun getNewListSize() = newList.pins.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGroup = oldList.pins[oldItemPosition]
        val newGroup = newList.pins[newItemPosition]
        return oldGroup.id == newGroup.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldGroup = oldList.pins[oldItemPosition]
        val newGroup = newList.pins[newItemPosition]
        return (
            oldGroup.userID == newGroup.userID &&
                oldGroup.description == newGroup.description &&
                oldGroup.imageLink == newGroup.imageLink &&
                oldGroup.description == newGroup.description &&
                oldGroup.title == newGroup.title
            )
    }
}
