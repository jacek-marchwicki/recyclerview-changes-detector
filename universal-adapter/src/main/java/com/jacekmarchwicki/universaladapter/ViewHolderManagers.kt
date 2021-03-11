/*
 * Copyright 2021 Jacek Marchwicki <jacek.marchwicki@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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