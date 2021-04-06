package com.jacekmarchwicki.universaladapter.items

import com.jacekmarchwicki.universaladapter.BaseAdapterItem

open class NoDataAdapterItem: BaseAdapterItem {
    override fun adapterId(): Long = BaseAdapterItem.NO_ID

    override fun matches(item: BaseAdapterItem): Boolean {
        return item.javaClass == javaClass
    }

    override fun same(item: BaseAdapterItem): Boolean = true
}