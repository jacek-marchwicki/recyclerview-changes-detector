package com.appunite.universal_adapter_kotlin

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jacekmarchwicki.universaladapter.BaseAdapterItem

abstract class BaseViewHolder<T : BaseAdapterItem>(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    /**
     * Called when a view created by this view holder has been recycled.
     *
     * @see RecyclerView.Adapter.onViewRecycled
     */
    open fun onViewRecycled() {}

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of that view holder to reflect the item.
     *
     * @param item adapter item to bind
     *
     * @see RecyclerView.Adapter.onBindViewHolder
     */
    abstract fun bind(item: T)

    /**
     * Called by the RecyclerView if a view holder cannot be recycled due to its transient
     * state. Upon receiving this callback, view holder can clear the animation(s) that effect
     * the View's transient state and return `true` so that the View can be recycled.
     * Keep in mind that the View in question is already removed from the RecyclerView.
     *
     * @see RecyclerView.Adapter.onFailedToRecycleView
     * @return True if the View should be recycled, false otherwise. Note that if this method
     * returns `true`, RecyclerView *will ignore* the transient state of
     * the View and recycle it regardless. If this method returns `false`,
     * RecyclerView will check the View's transient state again before giving a final decision.
     * Default implementation returns false.
     */
    fun onFailedToRecycleView(): Boolean {
        return false
    }

    /**
     * Called when a view created by this view holder has been attached to a window.
     *
     * @see RecyclerView.Adapter.onViewAttachedToWindow
     */
    fun onViewAttachedToWindow() {}

    /**
     * Called when a view created by this view holder has been detached from its window.
     *
     * @see RecyclerView.Adapter.onViewDetachedFromWindow
     */
    fun onViewDetachedFromWindow() {}
}