package com.jacekmarchwicki.universaladapter

/**
 * Version of BaseAdapterItem with default implementation of its methods.
 * It's highly recommended that classes implementing this interface are either data classes
 * or override equals() and hashCode() methods.
 */
interface DefaultAdapterItem : BaseAdapterItem {
    val itemId: Any

    override fun adapterId(): Long = itemId.hashCode().toLong()

    override fun matches(item: BaseAdapterItem): Boolean = when (item) {
        is DefaultAdapterItem -> itemId == item.itemId && item.javaClass == javaClass
        else -> false
    }

    override fun same(item: BaseAdapterItem): Boolean =
            this == item
}