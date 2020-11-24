package com.appunite.universal_adapter_kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import com.appunite.universal_adapter_kotlin.BaseViewHolder
import com.jacekmarchwicki.universaladapter.BaseAdapterItem


/**
 * Manager that managing of creation [ViewHolderManager.BaseViewHolder]
 */
interface ViewHolderManager {
    /**
     * Return if this manager can handle that kind of type
     * @param baseAdapterItem adapter item
     * @return true if can handle this item
     */
    fun matches(baseAdapterItem: BaseAdapterItem): Boolean

    /**
     * Create [ViewHolderManager.BaseViewHolder] for this item
     * @param parent parent view
     * @param inflater layout inflater
     * @return new [ViewHolderManager.BaseViewHolder]
     */
    fun createViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): BaseViewHolder<BaseAdapterItem>

}