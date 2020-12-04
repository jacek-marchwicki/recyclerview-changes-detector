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

package com.jacekmarchwicki.changesdetector.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jacekmarchwicki.universaladapter.BaseAdapterItem;
import com.jacekmarchwicki.universaladapter.UniversalAdapter;
import com.jacekmarchwicki.universaladapter.ViewHolderManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;

public class MainActivity extends AppCompatActivity {

    private static class Data implements BaseAdapterItem {

        private final long id;
        @Nonnull
        private final String name;
        private final int color;

        Data(long id, @Nonnull String name, int color) {
            this.id = id;
            this.name = name;
            this.color = color;
        }

        @Override
        public long adapterId() {
            return id;
        }

        @Override
        public boolean matches(@Nonnull BaseAdapterItem item) {
            return item instanceof Data && (((Data) item).id == id);
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
                    name.equals(data.name) &&
                    color == data.color;
        }
    }

    static class DataViewHolder implements ViewHolderManager {

        @Override
        public boolean matches(@Nonnull BaseAdapterItem baseAdapterItem) {
            return baseAdapterItem instanceof Data;
        }

        @Nonnull
        @Override
        public BaseViewHolder createViewHolder(@Nonnull ViewGroup parent, @Nonnull LayoutInflater inflater) {
            return new ViewHolder(inflater.inflate(R.layout.data_item, parent, false));
        }

        private class ViewHolder extends BaseViewHolder<Data> {

            @Nonnull
            private final TextView text;
            @Nonnull
            private final CardView cardView;

            ViewHolder(@Nonnull View itemView) {
                super(itemView);
                text = (TextView) itemView.findViewById(R.id.data_item_text);
                cardView = (CardView) itemView.findViewById(R.id.data_item_cardview);
            }

            @Override
            public void bind(@Nonnull final Data item) {
                text.setText(item.name);
                cardView.setCardBackgroundColor(item.color);
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("Click: " + item);
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_activity_recycler);

        // Setup layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setRecycleChildrenOnDetach(true);
        recyclerView.setLayoutManager(layoutManager);

        // Create universal adapter
        final UniversalAdapter adapter = new UniversalAdapter(Collections.singletonList(new DataViewHolder()));
        recyclerView.setAdapter(adapter);

        findViewById(R.id.main_activity_fab)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.submitList(generateItems());
                    }
                });
    }

    @Nonnull
    private static List<BaseAdapterItem> generateItems() {
        final Random random = new Random();

        float hue = 0.f;
        final int itemsCount = randomBetween(random, 10, 30);
        final ArrayList<BaseAdapterItem> items = new ArrayList<>(itemsCount);
        for (int i = 0; i < itemsCount; ++i) {
            hue += 0.12345;
            if (!randomWithGaussianBoolean(random, 2.0)) {
                continue;
            }
            float realHue = hue % 1.0f;
            final int color = Color.HSVToColor(255, new float[]{realHue * 360, 1.f, 0.5f});
            final String name = randomWithGaussianBoolean(random, 2.0) ? ("item" + i) : ("item" + i + " - changed");
            items.add(new Data(i, name, color));
        }

        final int swapStart = randomBetween(random, 0, items.size() - 2);
        final int swapEnd = Math.min(items.size() - 1, swapStart + 2);
        Collections.swap(items, swapStart, swapEnd);
        return Collections.unmodifiableList(items);
    }

    private static boolean randomWithGaussianBoolean(@Nonnull Random random, double proablity) {
        return Math.abs(random.nextGaussian()) < proablity;
    }

    private static int randomBetween(@Nonnull Random random, int start, int end) {
        return random.nextInt(end - start) + start;
    }
}
