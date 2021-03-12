# Recycler View Changes Detector

[V1 documentation](https://github.com/jacek-marchwicki/recyclerview-changes-detector/blob/1.0.2/README.md)

[![Build Status](https://travis-ci.org/jacek-marchwicki/recyclerview-changes-detector.svg?branch=master)](https://travis-ci.org/jacek-marchwicki/recyclerview-changes-detector)
[![Jitpack Status](https://jitpack.io/v/jacek-marchwicki/recyclerview-changes-detector.svg)](https://jitpack.io/#jacek-marchwicki/recyclerview-changes-detector)

Lightweight library that simplifies creation of RecyclerView's Adapter:
- Less boilerplate. No need to implement `onCreateViewHolder`, `getItemViewType`, `onBindViewHolder`, `getItemId`
- Out of the box DiffUtil support. You don't have to implement the `DiffUtil.ItemCallback()` anymore.
- Plug and play data models and view holders
- Cleaner tests
- RX support (optional)

## How it looks

![Screencast of the sample app](data/screencast.gif)

## How to add to your project

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.jacek-marchwicki.recyclerview-changes-detector:universal-adapter:<look-on-release-tab>'
    // RX support
    implementation 'com.github.jacek-marchwicki.recyclerview-changes-detector:universal-adapter-rx:<look-on-release-tab>'
}
```

## How to use

Let's assume that your list consits of headers, songs and a footer.

- Implement data models for all list elements

```kotlin
data class HeaderItem(val text: String, val songsCount: Int, override val itemId: Any = text) : DefaultAdapterItem()

data class SongItem(val id: String, val title: String, val imageUrl: String, override val itemId: Any = id, val onSongClick: (id: String) -> Unit) : DefaultAdapterItem()

data class FooterItem(override val itemId: Any = NO_ID) : DefaultAdapterItem()

```

- Implement a view holder for each data model to bind its data to the view. You have to specify item layout id and data model class.

```kotlin
class HeaderViewHolder : LayoutViewHolderManager<HeaderItem>(
    R.layout.item_header, HeaderItem::class, { HeaderViewHolder(it) }
) {
    class HeaderViewHolder(itemView: View) : ViewHolderManager.BaseViewHolder<HeaderItem>(itemView) {
        override fun bind(item: HeaderItem) {
            itemView.item_header_tv.text = "${item.text} - ${item.songsCount}" 
        }
    }
}

class SongViewHolder(val imageLoader: ImageLoader) : LayoutViewHolderManager<SongItem>(
    R.layout.song_item, SongItem::class, { ViewHolder(it) }
) {
    class ViewHolder(itemView: View) : ViewHolderManager.BaseViewHolder<SongItem>(itemView) {
        override fun bind(item: SongItem) {
            itemView.song_item.text = item.title
            itemView.setOnClickListener { item.onSongClick(item.id) }
            imageLoader.load(item.imageUrl).into(itemView.song_cover_iv)
        }
    }
}

class FooterViewHolder : LayoutViewHolderManager<FooterItem>(
    R.layout.item_footer, FooterItem::class, { ViewHolder(it) }
) {
    class ViewHolder(itemView: View) : ViewHolderManager.BaseViewHolder<FooterItem>(itemView) {
        override fun bind(item: FooterItem) {}
    }
}

```

- Setup the adapter and bind data:

```kotlin
val adapter = UniversalAdapter(listOf(headerViewHolder, songViewHolder, footerViewHolder))
recyclerView.adapter = adapter

// You'd rather create the items in a ViewModel/Presenter
adapter.submitList(listOf(
  HeaderItem(text="Album1"),
  Song(id="1", title="Song1"),
  Song(id="2", title="Song2"),
  HeaderItem(text="Album2"),
  Song(id="3", title="Song1"),
  FooterItem(),
));
```

## DiffUtil support
DiffUtil is an androidx tool that calculates the difference between two lists submitted to adapter.
Thanks to this, only modified elements are updated and not the whole list. It also applies a very nice animation that you can see on the GIF above.
Normally you have to implement the `DiffUtil.ItemCallback` on your own and decide when your adapter elements has changed. 
Thanks to the data models you create by extending `DefaultAdapterItem` class, you don't have to do this any more. There are two conditions though
that you have to satisfy:
- you have to override `val itemId: Any`
- your data models have to be either Kotlin data classes or override `equals()` and `hashCode()` methods.

Let's have a look at the example below:
```kotlin
data class SongItem(val id: String, val title: String, override val itemId: Any = id) : DefaultAdapterItem()
```
- `itemId` is required to identify specific element in the list. In this case it is the song id as it is unique to the song. This is being used in the `DiffUtil.ItemCallback.areItemsTheSame()` method.
- `SongItem` is a Kotlin data class so it overrides `equals` and `hashCode` by default. This is being used in the `DiffUtil.ItemCallback.areContentsTheSame()` method to identify whether element's content changed and need to be updated. If you'd like to alter this behaviour you can override `equals` and `hashCode` methods on your own.

## Plug and play data models and view holders
As your models and view holders are not bound to any adapter, you can reuse them in every adapter.
You don't have to specify the `itemViewType` in each adapter, just pass you view holder to adapter's constructor.

## Cleaner tests
As your data models are being created in a ViewModel/Presenter, your test logic is very clean and simple

```kotlin
@Test
fun `when 2 of 4 registered students are attendees then student items correctly divided into sections`() {
    every { classDao.registeredStudents } returns Observable.just(
        listOf(
            ClassStudent("name1", attended = true),
            ClassStudent("name3", attended = false),
            ClassStudent("name2", attended = true),
            ClassStudent("name4", attended = false)
        )
    )
    viewModel = PastClassStudentsPresenter("fake_id", classDaos)

    viewModel.adapterItems
        .test()
        .assertValue(
            listOf(
                SectionNameItem("ATTENDED"),
                PastClassStudentItem("name1", true),
                PastClassStudentItem("name2", true),
                SectionNameItem("REGISTERED"),
                PastClassStudentItem("name3", false),
                PastClassStudentItem("name4", false)
            )
        )
}
```

## RX support
`universal-adapter-rx` module lets you subscribe directly to adapter like this:
```kotlin
viewModel.adapterItems.subscribe(adapter)
```

## Known issues
There is an issue in androidx.recyclerview version <= 1.1.0. If you use such version then you have to override
`LayoutManager.supportsPredictiveItemAnimations` and return false to avoid crash that may happen during list animation.
```kotlin
layoutManager = object : LinearLayoutManager(context, RecyclerView.VERTICAL, false) {
    override fun supportsPredictiveItemAnimations(): Boolean {
            return false
        }
    }
}
```

### More

For more look at the sample app at [app/](app/) directory.


## License

    Copyright [2016] [Jacek Marchwicki <jacek.marchwicki@gmail.com>]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    	http://www.apache.org/licenses/LICENSE-2.0


    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
