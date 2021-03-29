package com.jacekmarchwicki.universaladapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

object ViewHolderManagerFactory {

    inline fun <reified T : BaseAdapterItem> create(
        @LayoutRes layoutRes: Int,
        noinline viewHolderCreator: (View) -> BaseViewHolder<T>
    ): ViewHolderManager {
        return LayoutViewHolderManager(layoutRes, T::class, viewHolderCreator)
    }

    inline fun <reified T : BaseAdapterItem> create(
        crossinline createView: (parent: ViewGroup, inflater: LayoutInflater) -> View,
        noinline viewHolderCreator: (View) -> BaseViewHolder<T>
    ): ViewHolderManager {
        return object : BaseViewHolderManager<T>(T::class, viewHolderCreator) {
            override fun createView(parent: ViewGroup, inflater: LayoutInflater): View {
                return createView(parent, inflater)
            }
        }
    }
}