package com.appunite.universal_adapter_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import kotlin.reflect.KClass

abstract class BaseViewHolderManager<T : BaseAdapterItem>(
        private val viewHolderCreator: (view: View) -> BaseViewHolder<T>,
        private val clazz: KClass<out T>
) : ViewHolderManager {

    abstract fun createView(parent: ViewGroup, inflater: LayoutInflater): View

    override fun createViewHolder(
            parent: ViewGroup,
            inflater: LayoutInflater
    ): BaseViewHolder<BaseAdapterItem> {
        return viewHolderCreator(createView(parent, inflater)) as BaseViewHolder<BaseAdapterItem>
    }

    override fun matches(baseAdapterItem: BaseAdapterItem): Boolean =
            clazz.isInstance(baseAdapterItem)
}

open class LayoutViewHolderManager<T : BaseAdapterItem>(
        @LayoutRes private val layoutRes: Int,
        viewHolderCreator: (view: View) -> BaseViewHolder<T>,
        clazz: KClass<out T>
) : BaseViewHolderManager<T>(viewHolderCreator, clazz) {

    override fun createView(parent: ViewGroup, inflater: LayoutInflater): View {
        return inflater.inflate(layoutRes, parent, false)
    }
}

object ViewHolderManagerFactory {

    inline fun <reified T : BaseAdapterItem> create(
            @LayoutRes layoutRes: Int,
            noinline viewHolderFactory: (View) -> BaseViewHolder<T>
    ): ViewHolderManager {
        return LayoutViewHolderManager(
                layoutRes,
                viewHolderFactory,
                T::class
        )
    }
}