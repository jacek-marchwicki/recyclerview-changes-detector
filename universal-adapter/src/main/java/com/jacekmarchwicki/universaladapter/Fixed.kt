/*
 * Copyright 2020 Jacek Marchwicki <jacek.marchwicki@gmail.com>
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

/**
 * This class helps excluding property of [BaseAdapterItem] from comparison between other
 * [BaseAdapterItem] using [BaseAdapterItem.same()] method.
 * It may be useful when your data model contains a property that shouldn't impact
 * equality check. It could be a provided method or rx Observable.
 *
 * Following example demonstrates usecase:
 *```
 * data class FixedAdapterItem(
 *    val title: String,
 *    val isFavorite: Fixed<Observable<Boolean>,
 *    override val itemId: Any = title
 * ) : DefaultAdapterItem()
 *
 * class Presenter {
 *    fun createAdapterItems(): List<BaseAdapterItem> {
 *       return listOf(
 *          FixedAdapterItem("title1", favsDao.isFavorite(id).fixed()),
 *       )
 *    }
 * }
 *```
 * Assume that [favsDao.isFavorite() ]returns a new instance of observable
 * every time it is being called. Thus two following FixedAdapterItem with the same title
 * would be different for [BaseAdapterItem.same()] method if we didn't wrap it in the Fixed object.
 * That would cause RecyclerView to re-bind the element which content in fact didn't change.
 */
class Fixed<T>(val property: T?) {

    override fun equals(other: Any?): Boolean = true

    override fun hashCode(): Int = 0
}

fun <T> T.fixed() = Fixed(this)

