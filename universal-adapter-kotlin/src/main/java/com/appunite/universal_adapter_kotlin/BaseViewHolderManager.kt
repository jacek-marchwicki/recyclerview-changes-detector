package com.appunite.universal_adapter_kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import kotlin.reflect.KClass

open class BaseViewHolderManager<T : BaseAdapterItem>(
        @LayoutRes private val layoutRes: Int,
        private val viewHolderCreator: (view: View) -> BaseViewHolder<T>,
        private val clazz: KClass<out T>
) : ViewHolderManager {

    override fun matches(baseAdapterItem: BaseAdapterItem): Boolean =
        clazz.isInstance(baseAdapterItem)

    override fun createViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): BaseViewHolder<BaseAdapterItem> {
        val itemView = inflater.inflate(layoutRes, parent, false)
        return viewHolderCreator(itemView) as BaseViewHolder<BaseAdapterItem>
    }
}

object ViewHolderManagerFactory {

    inline fun <reified T : BaseAdapterItem> create(
        @LayoutRes layoutRes: Int,
        noinline viewHolderFactory: (View) -> BaseViewHolder<T>
    ): ViewHolderManager {
        return BaseViewHolderManager(
            layoutRes,
            viewHolderFactory,
            T::class
        )
    }
}