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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jacekmarchwicki.universaladapter.*
import com.jacekmarchwicki.universaladapter.items.NoDataAdapterItem
import com.jacekmarchwicki.universaladapter.managers.NoDataViewHolderManager
import kotlinx.android.synthetic.main.item_header.view.*
import kotlinx.android.synthetic.main.item_song.view.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(), MainPresenter.MainView {

    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val recyclerView = main_activity_recycler.apply {
            layoutManager = object : LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false) {
                // If you use androidx.recyclerview version <= 1.1.0 then you have to override this method
                // as there is a bug that may cause a crash when the list animates
            /*    override fun supportsPredictiveItemAnimations(): Boolean {
                    return false
                }*/
            }.apply {
                recycleChildrenOnDetach = true
            }

        }

        val adapter = UniversalAdapter(listOf(
            SongViewHolder(ImageLoader()),
            HeaderViewHolder(),
            NoDataViewHolderManager(R.layout.item_footer))
        )
        recyclerView.adapter = adapter

        main_activity_fab.setOnClickListener { adapter.submitList(presenter.getItems()) }
    }

    override fun onSongClicked(id: String) {
        Toast.makeText(this, "Song $id clicked", Toast.LENGTH_SHORT).show()
    }
}

// Data models
data class HeaderItem(val text: String, val songsCount: Int, override val itemId: Any = text) : DefaultAdapterItem()

data class SongItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    override val itemId: Any = id,
    val onSongClick: (id: String) -> Unit
) : DefaultAdapterItem()

class FooterItem : NoDataAdapterItem()

// View Holders
class HeaderViewHolder : LayoutViewHolderManager<HeaderItem>(
    R.layout.item_header, HeaderItem::class, { HeaderViewHolder(it) }
) {
    class HeaderViewHolder(itemView: View) : BaseViewHolder<HeaderItem>(itemView) {
        override fun bind(item: HeaderItem) {
            itemView.item_header_tv.text = "${item.text}  (songs count: ${item.songsCount})"
        }
    }
}

class SongViewHolder(private val imageLoader: ImageLoader) : LayoutViewHolderManager<SongItem>(
    R.layout.item_song, SongItem::class, { ViewHolder(it, imageLoader) }
) {
    class ViewHolder(itemView: View, private val imageLoader: ImageLoader) : BaseViewHolder<SongItem>(itemView) {
        override fun bind(item: SongItem) {
            itemView.item_song_title.text = item.title
            itemView.setOnClickListener { item.onSongClick(item.id) }
            imageLoader.load(item.imageUrl).into(itemView.item_song_card)
        }
    }
}

class ImageLoader {
    fun load(url: String): ImageLoader {
        return this
    }

    fun into(view: View) {}
}