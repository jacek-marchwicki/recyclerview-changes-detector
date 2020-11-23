# Recycler View Changes Detector

[![Build Status](https://travis-ci.org/jacek-marchwicki/recyclerview-changes-detector.svg?branch=master)](https://travis-ci.org/jacek-marchwicki/recyclerview-changes-detector)
[![Jitpack Status](https://jitpack.io/v/jacek-marchwicki/recyclerview-changes-detector.svg)](https://jitpack.io/#jacek-marchwicki/recyclerview-changes-detector)

Library allow to automatically detect changes in your data and call methods:
- notifyItemRangeInserted()
- notifyItemRangeChanged()
- notifyItemRangeRemoved()
- notifyItemMoved()

## How it looks

![Screencast of the sample app](data/screencast.gif)

## How to add to your project

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {

    // UniversalAdapter with changes detector with RxJava
    implementation 'com.github.jacek-marchwicki.recyclerview-changes-detector:universal-adapter-rx:<look-on-release-tab>'

    // UniversalAdapter with changes detector without RxJava
    implementation 'com.github.jacek-marchwicki.recyclerview-changes-detector:universal-adapter:<look-on-release-tab>'

    // Changes Detector and Adapter items (without Android dependencies)
    implementation 'com.github.jacek-marchwicki.recyclerview-changes-detector:universal-adapter-java:<look-on-release-tab>'

    // Changes Detector (without Android dependencies)
    implementation 'com.github.jacek-marchwicki.recyclerview-changes-detector:changes-detector:<look-on-release-tab>'
}
```

## How to use

### With Universal Adapter

Implement some Pojo with your data:

```java
private class Data implements BaseAdapterItem {

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

    /**
     * Return true if id matches
     */
    @Override
    public boolean matches(@Nonnull BaseAdapterItem item) {
        return item instanceof Data && (((Data) item).id == id);
    }

    /**
     * Return true if items are equal
     */
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
```

Implement your holder:

```java
public class DataViewHolder implements ViewHolderManager {

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
        public void bind(@Nonnull Data item) {
            text.setText(item.name);
            cardView.setCardBackgroundColor(item.color);
        }
    }
}
```

Setup recycler view:

```java
final UniversalAdapter adapter = new UniversalAdapter(Collections.<ViewHolderManager>singletonList(new DataViewHolder()));
recyclerView.setAdapter(adapter);
```

Give new data to adapter:

```java
adapter.call(Arrays.toList(new Data(1, "Cow"), new Data(2, "Dg"), new Data(3, "Cat"));
```

And another data so recycler view will be nice animated:

```java
adapter.call(Arrays.toList(Data(2, "Dog"), new Data(3, "Cat"), new Data(4, "Elephant"));
```

### With auto-value (recommended)

Usage like above with small improvement:

```java
@AutoValue
private class Data implements BaseAdapterItem {

    @Nonnull
    @AdapterId
    public abstract String id();
    @Nonnull
    public abstract String name();
    public abstract int color();

    public Data create(@Nonull String id, @Nonnull String name, int color) {
        return AutoValue_Data(id, name, color);
    }

    // methods equal, adapterId, matches and same will be generated for you
}
```


For more info look: [AutoValue: BaseAdapterItem Extension](https://github.com/m-zagorski/auto-value-base-adapter-item)


### Without Universal Adapter

Implement some Pojo with your data:

```java
private class Data implements SimpleDetector.Detectable<Data> {

    private final long id;
    @Nonnull
    private final String name;
    private final int color;

    Data(long id, @Nonnull String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    /**
     * Return true if id matches
     */
    @Override
    public boolean matches(@Nonnull Data item) {
        return ((Data) item).id == id;
    }

    /**
     * Return true if items are equal
     */
    @Override
    public boolean same(@Nonnull Data item) {
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
```

Setup changes detector for your adapter

```
@Nonnull
private final ChangesDetector<Data, Data> changesDetector =
        new ChangesDetector<>(new SimpleDetector<Data>());
```

Give new data to your adapter:

```java
List<Data> data = Arrays.toList(new Data(1, "Cow"), new Data(2, "Dg"), new Data(3, "Cat"));
yourAdapter.swapData(data)
changesDetector.newData(yourAdapter, data, false);
```

And another data so recycler view will be nice animated:

```java
List<Data> data = Arrays.toList(Data(2, "Dog"), new Data(3, "Cat"), new Data(4, "Elephant"));
yourAdapter.swapData(data)
changesDetector.newData(yourAdapter, data, false);
```

### More

For more:
- look on sample app at [app/](app/) directory.
- look on [AutoValue: BaseAdapterItem Extension](https://github.com/m-zagorski/auto-value-base-adapter-item)


### Frequently asked questions

[FAQ](FAQ.md)


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
