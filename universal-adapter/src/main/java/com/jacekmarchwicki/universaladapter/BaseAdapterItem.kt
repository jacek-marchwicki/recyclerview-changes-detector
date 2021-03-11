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
package com.jacekmarchwicki.universaladapter

interface BaseAdapterItem {
    /**
     * Unique adapter id or [BaseAdapterItem.NO_ID]
     *
     * @return adapter id or [BaseAdapterItem.NO_ID]
     */
    fun adapterId(): Long

    /**
     * If both items are the same but can have different content
     *
     * Usually it means that both items have the same id
     *
     * @param item to compare
     * @return true if both items matches
     */
    fun matches(item: BaseAdapterItem): Boolean

    /**
     * If both items have exactly the same content
     *
     * Usually it means both items have the same id, name and other fields
     *
     * If you implemented [Any.equals] you can call
     * `this.equals(item)`
     *
     * @param item to compare
     * @return true if booth items are the same
     */
    fun same(item: BaseAdapterItem): Boolean

    companion object {
        /**
         * return this id if you don't have any id's
         */
        const val NO_ID: Long = -1
    }
}