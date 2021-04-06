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
package com.jacekmarchwicki.universaladapter.rx3

import com.jacekmarchwicki.universaladapter.BaseAdapterItem
import com.jacekmarchwicki.universaladapter.UniversalAdapter
import com.jacekmarchwicki.universaladapter.ViewHolderManager
import io.reactivex.rxjava3.functions.Consumer

class RxUniversalAdapter(
    managers: List<ViewHolderManager>
) : UniversalAdapter(managers), Consumer<List<BaseAdapterItem>> {

    override fun accept(items: List<BaseAdapterItem>) {
        submitList(items)
    }
}