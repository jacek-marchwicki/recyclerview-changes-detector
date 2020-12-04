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

package com.jacekmarchwicki.universaladapter;


public interface BaseAdapterItem {

    /**
     * return this id if you don't have any id's
     */
    long NO_ID = -1;

    /**
     * Unique adapter id or {@link #NO_ID}
     *
     * @return adapter id or {@link #NO_ID}
     */
    long adapterId();

    /**
     * If both items are the same but can have different content
     * <p>Usually it means booth items has same id</p>
     *
     * @param item to compare
     * @return true if booth items matches
     */
    boolean matches(BaseAdapterItem item);

    /**
     * If both items has exactly same content
     * <p>Usually it means both items has same id, name and other fields</p>
     * <p>If you implemented {@link Object#equals(Object)} you can call
     * {@code this.equals(item)}</p>
     *
     * @param item to compare
     * @return true if booth items are the same
     */
    boolean same(BaseAdapterItem item);
}
