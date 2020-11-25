package com.appunite.universal_adapter_kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jacekmarchwicki.universaladapter.BaseAdapterItem

class UniversalAdapter(
    private val managers: List<ViewHolderManager>
) : ListAdapter<BaseAdapterItem, BaseViewHolder<BaseAdapterItem>>(
    itemDiffCallback
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BaseAdapterItem> {
        val manager: ViewHolderManager = managers[viewType]
        return manager.createViewHolder(parent, LayoutInflater.from(parent.context))
    }

    override fun getItemViewType(position: Int): Int {
        val baseAdapterItem: BaseAdapterItem = currentList[position]
        val first = managers.indexOfFirst { manager -> manager.matches(baseAdapterItem) }
        if (first == -1) throw IllegalStateException("No matching manager")
        return first
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            getItem(position).adapterId()
        } else {
            return RecyclerView.NO_ID
        }
    }

    override fun onBindViewHolder(
            holder: BaseViewHolder<BaseAdapterItem>,
            position: Int
    ) = holder.bind(currentList[position])

    override fun onFailedToRecycleView(holder: BaseViewHolder<BaseAdapterItem>): Boolean {
        return holder.onFailedToRecycleView()
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder<BaseAdapterItem>) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<BaseAdapterItem>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onViewRecycled(holder: BaseViewHolder<BaseAdapterItem>) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }

    companion object {
        val itemDiffCallback = object : DiffUtil.ItemCallback<BaseAdapterItem>() {

            override fun areItemsTheSame(
                oldItem: BaseAdapterItem,
                newItem: BaseAdapterItem
            ): Boolean =
                oldItem.matches(newItem)

            override fun areContentsTheSame(
                oldItem: BaseAdapterItem,
                newItem: BaseAdapterItem
            ): Boolean =
                oldItem.same(newItem)
        }
    }
}