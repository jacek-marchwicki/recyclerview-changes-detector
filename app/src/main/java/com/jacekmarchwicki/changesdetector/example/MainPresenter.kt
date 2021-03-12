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

package com.jacekmarchwicki.changesdetector.example

import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import kotlin.random.Random

class MainPresenter(private val view: MainView) {

    interface MainView {
        fun onSongClicked(id: String)
    }

    fun getItems(): List<BaseAdapterItem> {
        val albumsCount = Random.nextInt(2, 4)
        return mutableListOf<BaseAdapterItem>().apply {
            repeat(albumsCount) { index ->
                val songs = getSongsForAlbum()
                add(HeaderItem("Album $index", songsCount = songs.size, itemId = index))
                addAll(songs)
            }
            add(FooterItem())
        }.toList()
    }

    private fun getSongsForAlbum(): List<BaseAdapterItem> {
        val songsCount = Random.nextInt(1, 4)
        return mutableListOf<BaseAdapterItem>().apply {
            repeat(songsCount) { index ->
                add(SongItem(index.toString(), "Song $index",
                    itemId = index, onSongClick = ::onSongClick, imageUrl = "url"))
            }
        }.toList()
    }

    private fun onSongClick(id: String) {
        view.onSongClicked(id)
    }
}