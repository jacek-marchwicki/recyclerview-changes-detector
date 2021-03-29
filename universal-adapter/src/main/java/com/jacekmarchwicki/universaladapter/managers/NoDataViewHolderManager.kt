package com.jacekmarchwicki.universaladapter.managers

import android.view.View
import androidx.annotation.LayoutRes
import com.jacekmarchwicki.universaladapter.LayoutViewHolderManager
import com.jacekmarchwicki.universaladapter.ViewHolderManager
import com.jacekmarchwicki.universaladapter.items.NoDataAdapterItem

class NoDataViewHolderManager(
    @LayoutRes private val layoutRes: Int
) : LayoutViewHolderManager<NoDataAdapterItem>(
    layoutRes, NoDataAdapterItem::class, { NoDataViewHolder(it) }
) {

    class NoDataViewHolder(itemView: View) : ViewHolderManager.BaseViewHolder<NoDataAdapterItem>(itemView) {
        override fun bind(item: NoDataAdapterItem) {
            // Nothing to bind
        }
    }
}