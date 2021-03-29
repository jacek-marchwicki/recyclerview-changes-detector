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