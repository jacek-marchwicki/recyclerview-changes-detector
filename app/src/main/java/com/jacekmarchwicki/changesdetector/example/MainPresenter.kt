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

import android.graphics.Color
import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import java.util.*
import kotlin.math.min

class MainPresenter {

    fun getItems(): List<BaseAdapterItem> {
        val random = Random()
        var hue = 0f
        val itemsCount = randomBetween(random, 10, 30)
        val items = mutableListOf<BaseAdapterItem>()
        for (i in 0 until itemsCount) {
            hue += 0.12345f
            if (!randomWithGaussianBoolean(random, 2.0)) {
                continue
            }
            val realHue = hue % 1.0f
            val color = Color.HSVToColor(255, floatArrayOf(realHue * 360, 1f, 0.5f))
            val name = if (randomWithGaussianBoolean(random, 2.0)) "item$i" else "item$i - changed"
            items.add(DataItem(i.toLong(), name, color))
        }
        val swapStart = randomBetween(random, 0, items.size - 2)
        val swapEnd = min(items.size - 1, swapStart + 2)
        Collections.swap(items, swapStart, swapEnd)

        items.add(0, HeaderItem("Header 0"))
        val secondPosition = itemsCount / 3
        items.add(secondPosition, HeaderItem("Header $secondPosition"))
        val thirdPosition = itemsCount / 2
        items.add(thirdPosition, HeaderItem("Header $thirdPosition"))

        return items.toList()
    }

    private fun randomWithGaussianBoolean(random: Random, proablity: Double): Boolean {
        return Math.abs(random.nextGaussian()) < proablity
    }

    private fun randomBetween(random: Random, start: Int, end: Int): Int {
        return random.nextInt(end - start) + start
    }
}