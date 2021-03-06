/*
 * Copyright 2021 Marcin Adamczewski
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
package com.jacekmarchwicki.universaladapter.rx

import android.view.View
import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import com.jacekmarchwicki.universaladapter.ViewHolderManager
import rx.Subscription
import rx.subscriptions.SerialSubscription
import rx.subscriptions.Subscriptions

abstract class BaseRxViewHolder<T : BaseAdapterItem>(view: View) :
    ViewHolderManager.BaseViewHolder<T>(view) {

    private val subscription = SerialSubscription()

    open fun bindStreams(item: T): Subscription = Subscriptions.empty()

    open fun bindData(item: T) = Unit

    final override fun bind(item: T) {
        subscription.set(bindStreams(item))
        bindData(item)
    }

    override fun onViewRecycled() {
        subscription.set(Subscriptions.empty())
    }
}