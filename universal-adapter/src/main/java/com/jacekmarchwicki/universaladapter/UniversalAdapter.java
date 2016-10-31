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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jacekmarchwicki.changesdetector.ChangesDetector;
import com.jacekmarchwicki.changesdetector.SimpleDetector;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Universal adapter for {@link RecyclerView} that will automatically detect changes
 */
public class UniversalAdapter extends RecyclerView.Adapter<ViewHolderManager.BaseViewHolder> implements ChangesDetector.ChangesAdapter {
    @Nonnull
    private final ChangesDetector<BaseAdapterItem, BaseAdapterItem> changesDetector =
            new ChangesDetector<>(new SimpleDetector<BaseAdapterItem>());
    @Nonnull
    private final List<ViewHolderManager> managers;
    @Nonnull
    private List<BaseAdapterItem> items = Collections.emptyList();

    /**
     * Usage:
     * <pre><code>
     *   public static class Item implements BaseAdapterItem {
     *
     *     &#64;Nonnull
     *     private final String id;
     *     &#64;Nullable
     *     private final String lastMessage;
     *
     *     public ChatsItem(@Nonnull String id,
     *     &#64;Nullable String lastMessage) {
     *       this.id = id;
     *       this.lastMessage = lastMessage;
     *     }
     *
     *     &#64;Override
     *     public boolean matches(@Nonnull BaseAdapterItem item) {
     *       return item instanceof Item &amp;&amp; Objects.equal(id, ((Item)item).id);
     *     }
     *
     *     &#64;Override
     *     public boolean same(@Nonnull BaseAdapterItem item) {
     *       return equals(item);
     *     }
     *
     *     &#64;Nonnull
     *     public String id() {
     *       return id;
     *     }
     *
     *     &#64;Nullable
     *     public String lastMessage() {
     *       return lastMessage;
     *     }
     *
     *     &#64;Override
     *     public boolean equals(Object o) {
     *       if (this == o) return true;
     *       if (!(o instanceof Item)) return false;
     *       final Item chatsItem = (ChatsItem) o;
     *       return Objects.equal(id, chatsItem.id) &amp;&amp;
     *       Objects.equal(lastMessage, chatsItem.lastMessage);
     *     }
     *
     *     &#64;Override
     *     public int hashCode() {
     *       return Objects.hashCode(id, lastMessage);
     *     }
     *
     *     &#64;Override
     *     public long adapterId() {
     *       return id.hashCode();
     *     }
     *   }
     *
     *   private static class MyViewHolderManager implements ViewHolderManager {
     *     &#64;Override
     *     public boolean matches(BaseAdapterItem baseAdapterItem) {
     *       return baseAdapterItem instanceof Item;
     *     }
     *
     *     &#64;Override
     *     public BaseViewHolder createViewHolder(ViewGroup parent, LayoutInflater from) {
     *       return new Holder(from.inflate(R.layout.activity_chats_item, parent, false));
     *     }
     *
     *     public static class Holder extends BaseViewHolder&lt;Item&gt; {
     *
     *       private final TextView textView;
     *
     *       public Holder(View itemView) {
     *         super(itemView);
     *         textView = (TextView) itemView;
     *       }
     *
     *       &#64;Override
     *       public void bind(@Nonnull Item item) {
     *         textView.setText(item.lastMessage());
     *       }
     *
     *     }
     *   }
     *
     *   final UniversalAdapter adapter = new UniversalAdapter(
     *     ImmutableList.&lt;ViewHolderManager&gt;of(new MyViewHolderManager()));
     *
     *   recyclerView.setAdapter(adapter);
     * </code></pre>
     *
     * @param managers for inflating views
     */
    public UniversalAdapter(@Nonnull List<ViewHolderManager> managers) {
        this.managers = managers;
    }

    public void call(@Nonnull List<BaseAdapterItem> baseAdapterItems) {
        items = baseAdapterItems;
        changesDetector.newData(this, items, false);
    }

    @Override
    public ViewHolderManager.BaseViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {
        final ViewHolderManager manager = managers.get(viewType);
        return manager.createViewHolder(parent, LayoutInflater.from(parent.getContext()));
    }

    @Override
    public int getItemViewType(int position) {
        final BaseAdapterItem baseAdapterItem = items.get(position);
        for (int i = 0; i < managers.size(); i++) {
            final ViewHolderManager manager = managers.get(i);
            if (manager.matches(baseAdapterItem)) {
                return i;
            }
        }
        throw new RuntimeException("Unsupported item type: " + baseAdapterItem);
    }

    @Override
    public void onBindViewHolder(@Nonnull ViewHolderManager.BaseViewHolder holder, int position) {
        //noinspection unchecked
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).adapterId();
    }

    /**
     * Return item at position
     * <p>
     * Tip: Should not be used in reactive code because it's not a function
     * Tip: Need to be called from UIThread - because it can change
     *
     * @param position of item on the list
     * @return item at position
     * @throws IndexOutOfBoundsException if the position is out of range
     *    (<tt>position &lt; 0 || index &gt;= getItemCount()</tt>)
     */
    @Nonnull
    public BaseAdapterItem getItemAtPosition(int position) {
        return items.get(position);
    }

    @Override
    public boolean onFailedToRecycleView(@Nonnull ViewHolderManager.BaseViewHolder holder) {
        return holder.onFailedToRecycleView();
    }

    @Override
    public void onViewAttachedToWindow(@Nonnull ViewHolderManager.BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@Nonnull ViewHolderManager.BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    @Override
    public void onViewRecycled(@Nonnull ViewHolderManager.BaseViewHolder holder) {
        holder.onViewRecycled();
        super.onViewRecycled(holder);
    }
}
