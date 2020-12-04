package com.jacekmarchwicki.universaladapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

abstract class BaseViewHolderManager<T : BaseAdapterItem>(
        private val clazz: KClass<out T>,
        private val viewHolderCreator: (view: View) -> ViewHolderManager.BaseViewHolder<T>,
) : ViewHolderManager {

    abstract fun createView(parent: ViewGroup, inflater: LayoutInflater): View

    override fun createViewHolder(
            parent: ViewGroup,
            inflater: LayoutInflater
    ): ViewHolderManager.BaseViewHolder<out BaseAdapterItem> {
        return viewHolderCreator(createView(parent, inflater))
    }

    override fun matches(baseAdapterItem: BaseAdapterItem): Boolean =
            clazz.isInstance(baseAdapterItem)
}

open class LayoutViewHolderManager<T : BaseAdapterItem>(
        @LayoutRes private val layoutRes: Int,
        clazz: KClass<out T>,
        viewHolderCreator: (view: View) -> ViewHolderManager.BaseViewHolder<T>
) : BaseViewHolderManager<T>(clazz, viewHolderCreator) {

    override fun createView(parent: ViewGroup, inflater: LayoutInflater): View {
        return inflater.inflate(layoutRes, parent, false)
    }
}

object ViewHolderManagerFactory {

    inline fun <reified T : BaseAdapterItem> create(
            @LayoutRes layoutRes: Int,
            noinline viewHolderCreator: (View) -> ViewHolderManager.BaseViewHolder<T>
    ): ViewHolderManager {
        return LayoutViewHolderManager(layoutRes, T::class, viewHolderCreator)
    }

    inline fun <reified T : BaseAdapterItem> create(
            crossinline createView: (parent: ViewGroup, inflater: LayoutInflater) -> View,
            noinline viewHolderCreator: (View) -> ViewHolderManager.BaseViewHolder<T>
    ): ViewHolderManager {
        return object : BaseViewHolderManager<T>(T::class, viewHolderCreator) {
            override fun createView(parent: ViewGroup, inflater: LayoutInflater): View {
                return createView(parent, inflater)
            }
        }
    }
}