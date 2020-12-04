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


import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;

import org.junit.Test;

import java.util.Collections;

import javax.annotation.Nonnull;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UniversalAdapterTest {

    private class Data implements BaseAdapterItem {

        private final long id;
        @Nonnull
        private final String name;

        Data(long id, @Nonnull String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public long adapterId() {
            return id;
        }

        @Override
        public boolean matches(@Nonnull BaseAdapterItem item) {
            return item instanceof Data && Objects.equal(((Data) item).id, id);
        }

        @Override
        public boolean same(@Nonnull BaseAdapterItem item) {
            return equals(item);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Data)) return false;
            final Data data = (Data) o;
            return id == data.id &&
                    Objects.equal(name, data.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id, name);
        }
    }

    @Test
    public void testWhenCallIsInvoked_notifyObserver() throws Exception {
        final ViewHolderManager viewHolderManager = mock(ViewHolderManager.class);
        final UniversalAdapter adapter = new UniversalAdapter(Collections.singletonList(viewHolderManager));
        final RecyclerView.AdapterDataObserver observer = mock(RecyclerView.AdapterDataObserver.class);
        adapter.registerAdapterDataObserver(observer);

        adapter.submitList(Collections.<BaseAdapterItem>singletonList(new Data(1L, "krowa")));

        verify(observer).onItemRangeInserted(0, 1);
    }
}