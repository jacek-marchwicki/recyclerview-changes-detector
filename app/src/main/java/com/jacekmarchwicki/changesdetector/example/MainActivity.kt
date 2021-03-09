/*
 * Copyright 2016 Jacek Marchwicki <jacek.marchwicki@gmail.com>
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
package com.jacekmarchwicki.changesdetector.example

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacekmarchwicki.universaladapter.DefaultAdapterItem
import com.jacekmarchwicki.universaladapter.LayoutViewHolderManager
import com.jacekmarchwicki.universaladapter.UniversalAdapter
import com.jacekmarchwicki.universaladapter.ViewHolderManager.BaseViewHolder

class MainActivity : AppCompatActivity() {

    private val presenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val recyclerView = (findViewById<View>(R.id.main_activity_recycler) as RecyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false).apply {
                recycleChildrenOnDetach = true
            }
        }

        val adapter = UniversalAdapter(listOf(DataViewHolder(), HeaderViewHolder()))
        recyclerView.adapter = adapter

        findViewById<View>(R.id.main_activity_fab)
                .setOnClickListener { adapter.submitList(presenter.getItems()) }
    }
}

data class DataItem(val id: Long, val name: String, val color: Int) : DefaultAdapterItem() {
    override val itemId: Any = id
}

class DataViewHolder : LayoutViewHolderManager<DataItem>(
        R.layout.data_item, DataItem::class, { ViewHolder(it) }
) {
    class ViewHolder(itemView: View) : BaseViewHolder<DataItem>(itemView) {
        override fun bind(item: DataItem) {
            itemView.findViewById<TextView>(R.id.data_item_text).apply {
                text = item.name
                setOnClickListener { println("Click: $item") }
            }
            itemView.findViewById<CardView>(R.id.data_item_cardview)
                    .setCardBackgroundColor(item.color)
        }
    }
}

data class HeaderItem(val text: String, override val itemId: Any = text) : DefaultAdapterItem()

class HeaderViewHolder : LayoutViewHolderManager<HeaderItem>(
        R.layout.item_header, HeaderItem::class, { HeaderViewHolder(it) }
) {
    class HeaderViewHolder(itemView: View) : BaseViewHolder<HeaderItem>(itemView) {
        override fun bind(item: HeaderItem) {
            itemView.findViewById<TextView>(R.id.item_header_tv).text = item.text
        }
    }
}