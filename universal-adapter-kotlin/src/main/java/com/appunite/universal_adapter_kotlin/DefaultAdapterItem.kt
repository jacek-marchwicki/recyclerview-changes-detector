package com.appunite.universal_adapter_kotlin

import com.jacekmarchwicki.universaladapter.BaseAdapterItem

interface DefaultAdapterItem<out T : Any> : BaseAdapterItem {
    val itemId: T

    override fun adapterId(): Long = itemId.hashCode().toLong()

    override fun matches(item: BaseAdapterItem): Boolean = when (item) {
        is DefaultAdapterItem<*> -> itemId == item.itemId && item.javaClass == javaClass
        else -> false
    }

    override fun same(item: BaseAdapterItem): Boolean =
            this == item
}