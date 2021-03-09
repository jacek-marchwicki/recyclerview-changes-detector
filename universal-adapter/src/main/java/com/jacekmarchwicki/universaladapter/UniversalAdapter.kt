/*
 * Copyright 2021 Jacek Marchwicki <jacek.marchwicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.jacekmarchwicki.universaladapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

open class UniversalAdapter(
        private val managers: List<ViewHolderManager>
) : ListAdapter<BaseAdapterItem, ViewHolderManager.BaseViewHolder<BaseAdapterItem>>(
        itemDiffCallback
) {

    // Added for 1.0 backward compatibility
    fun call(baseAdapterItems: List<BaseAdapterItem>) {
        submitList(baseAdapterItems)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ViewHolderManager.BaseViewHolder<BaseAdapterItem> {
        val manager: ViewHolderManager = managers[viewType]
        return manager.createViewHolder(parent, LayoutInflater.from(parent.context))
                as ViewHolderManager.BaseViewHolder<BaseAdapterItem>
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
            holder: ViewHolderManager.BaseViewHolder<BaseAdapterItem>,
            position: Int
    ) = holder.bind(currentList[position])

    override fun onFailedToRecycleView(holder: ViewHolderManager.BaseViewHolder<BaseAdapterItem>): Boolean {
        return holder.onFailedToRecycleView()
    }

    override fun onViewAttachedToWindow(holder: ViewHolderManager.BaseViewHolder<BaseAdapterItem>) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderManager.BaseViewHolder<BaseAdapterItem>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onViewRecycled(holder: ViewHolderManager.BaseViewHolder<BaseAdapterItem>) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }

    companion object {
        val itemDiffCallback = object : DiffUtil.ItemCallback<BaseAdapterItem>() {

            override fun areItemsTheSame(
                    oldItem: BaseAdapterItem,
                    newItem: BaseAdapterItem
            ): Boolean = oldItem.matches(newItem)

            override fun areContentsTheSame(
                    oldItem: BaseAdapterItem,
                    newItem: BaseAdapterItem
            ): Boolean = oldItem.same(newItem)
        }
    }
}