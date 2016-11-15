# FAQ

`ChangesDetector` detects changes based on methods `matches` and `same` that you need to implement.
If you implement them wrongly this can lead to omitting your changes in recycler view or flooding
changed to recycler view that was not necessary.

# Bugs (aka: your bugs)

## Bug #1

```java
public class Data implements SimpleDetector.Detectable<Data> {

    private final long id;
    @Nonnull
    private final String name;

    Data(long id, @Nonnull String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean matches(@Nonnull Data item) {
        return Objects.equals(item.id, id);
    }

    @Override
    public boolean same(@Nonnull Data item) {
        return equals(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        final Data data = (Data) o;
        return Objects.equals(id, data.id);
    }
}
```

Because your `equals` method doesn't take care of `name`, changing name will not be informed to recycler view.

You need to change your `equals` method.

```java
public class Data implements SimpleDetector.Detectable<Data> {
    // ...
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        final Data data = (Data) o;
        return Objects.equals(id, data.id) &&
          Objects.equals(name, data.name); // This was missing
    }
    // ...
}
```

## Bug #2

```java
public class Data implements SimpleDetector.Detectable<Data> {

    private final long id;
    @Nonnull
    private final Object o;

    Data(long id, @Nonnull Object o) {
        this.id = id;
        this.o = o;
    }

    @Override
    public boolean matches(@Nonnull Data item) {
        return Objects.equals(item.id, id);
    }

    @Override
    public boolean same(@Nonnull Data item) {
        return equals(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        final Data data = (Data) o;
        return Objects.equals(id, data.id) &&
           Objects.equals(o, data.o);
    }
}
```

This code is not buggy but probably you will invoke bug invoking changes detector.

If you execute:

```java
adapter.call(Arrays.toList(Data(1, new Object())), new Data(2, new Object()));
adapter.call(Arrays.toList(Data(1, new Object())), new Data(2, new Object()));
```

You expect that changes detector will detect that nothing was changed, but actually change was made.
Changes detector will detect that `Object o` was changed according to method `equals` where
`Objects.equal(new Object(), new Object()) == false`.

There are two solutions:

- use objects that implement `equals` method
- use the same instance of objects

```java
final Object instance = new Object()
adapter.call(Arrays.toList(Data(1, instance)), new Data(2, instance));
adapter.call(Arrays.toList(Data(1, instance)), new Data(2, instance));
```

## Bug #2.1

Similar bug can be done with RxJava

```java
public class Data implements SimpleDetector.Detectable<Data> {

    private final long id;
    @Nonnull
    private final Observable<String> observable;

    Data(long id, @Nonnull Observable<String> observable) {
        this.id = id;
        this.observable = observable;
    }

    @Override
    public boolean matches(@Nonnull Data item) {
        return Objects.equals(item.id, id);
    }

    @Override
    public boolean same(@Nonnull Data item) {
        return equals(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        final Data data = (Data) o;
        return Objects.equals(id, data.id) &&
           Objects.equals(observable, data.observable);
    }
}
```

So wrong is:

```java
adapter.call(Arrays.toList(Data(1, Observable.just(""))), new Data(2, Observable.just("")));
adapter.call(Arrays.toList(Data(1, Observable.just(""))), new Data(2, Observable.just("")));
```

should be:

```java
final Observable<String> instance = Observable.just("")
adapter.call(Arrays.toList(Data(1, instance)), new Data(2, instance));
adapter.call(Arrays.toList(Data(1, instance)), new Data(2, instance));
```

So wrong is:

```java
final Observable<String> instance = Observable.just("")
adapter.call(Arrays.toList(Data(1, instance.observeOn(uiScheduler))), new Data(2, instance.observeOn(uiScheduler)));
adapter.call(Arrays.toList(Data(1, instance.observeOn(uiScheduler))), new Data(2, instance.observeOn(uiScheduler)));
```

should be:

```java
final Observable<String> instance = Observable.just("").observeOn(uiScheduler)
adapter.call(Arrays.toList(Data(1, instance)), new Data(2, instance));
adapter.call(Arrays.toList(Data(1, instance)), new Data(2, instance));
```

## Bug 3

This usually can happen when using RxJava

```java
public class Data implements SimpleDetector.Detectable<Data> {

    private final long id;
    @Nonnull
    private final Observable<String> observable;

    Data(long id, @Nonnull Observable<String> observable, Scheduler uiScheduler) {
        this.id = id;
        this.observable = observable.observeOn(uiScheduler);
    }

    @Override
    public boolean matches(@Nonnull Data item) {
        return Objects.equals(item.id, id);
    }

    public Observable<String> observable() {
        return observable;
    }

    @Override
    public boolean same(@Nonnull Data item) {
        return equals(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        final Data data = (Data) o;
        return Objects.equals(id, data.id) &&
           Objects.equals(observable, data.observable);
    }
}
```

In this case, calling:

```java
final Observable<String> instance = Observable.just("")
adapter.call(Arrays.toList(Data(1, instance)), new Data(2, instance));
adapter.call(Arrays.toList(Data(1, instance)), new Data(2, instance));
```

`new Data()` will always generate new objects so equals will never be `true`.

You should write your `Data`:

```java
public class Data implements SimpleDetector.Detectable<Data> {

    private final long id;
    @Nonnull
    private final Observable<String> observable;
    @Nonnull
    private final Scheduler uiScheduler;

    Data(long id, @Nonnull Observable<String> observable, Scheduler uiScheduler) {
        this.id = id;
        this.observable = observable;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public boolean matches(@Nonnull Data item) {
        return Objects.equals(item.id, id);
    }

    public Observable<String> observable() {
        return observable.observeOn(uiScheduler);
    }

    @Override
    public boolean same(@Nonnull Data item) {
        return equals(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        final Data data = (Data) o;
        return Objects.equals(id, data.id) &&
           Objects.equals(observable, data.observable) &&
           Objects.equals(uiScheduler, data.uiScheduler);
    }
}
```

## Bug 4

The same issue can happen without using RxJava:

```java
public class Data implements SimpleDetector.Detectable<Data> {

    private final long id;
    @Nonnull
    private final int value;

    Data(long id, @Nonnull java.util.Random random) {
        this.id = id;
        this.value = random.nextInt();
    }

    @Override
    public boolean matches(@Nonnull Data item) {
        return Objects.equals(item.id, id);
    }

    @Override
    public boolean same(@Nonnull Data item) {
        return equals(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        final Data data = (Data) o;
        return Objects.equals(id, data.id) &&
           Objects.equals(value, data.value);
    }
}
```

This is definitely what you shouldn't do.


## Bug 5

When using RxJava with adapter (this is not strictly related to `ChangesDetector`) there is one more thing to remember.

```java
public class Data implements SimpleDetector.Detectable<Data> {

    private final long id;
    @Nonnull
    private final Observable<String> observable;
    @Nonnull
    private final Scheduler uiScheduler;

    Data(long id, @Nonnull Observable<String> observable, Scheduler uiScheduler) {
        this.id = id;
        this.observable = observable;
        this.uiScheduler = uiScheduler;
    }

    @Override
    public boolean matches(@Nonnull Data item) {
        return Objects.equals(item.id, id);
    }

    public Observable<String> observable() {
        return observable.observeOn(uiScheduler);
    }

    @Override
    public boolean same(@Nonnull Data item) {
        return equals(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        final Data data = (Data) o;
        return Objects.equals(id, data.id) &&
           Objects.equals(observable, data.observable) &&
           Objects.equals(uiScheduler, data.uiScheduler);
    }
}
```

This code actually have some bug. Because RecyclerView recycle it's old items. So in case when we have
some items:

```java
adapter.call(Arrays.toList(
    new Data(1, Observable.just("1").delay(1, TimeUnite.SECONDS)),
    new Data(2, Observable.just("2").delay(1, TimeUnite.SECONDS)),
    new Data(3, Observable.just("3").delay(1, TimeUnite.SECONDS)),
    new Data(4, Observable.just("4").delay(1, TimeUnite.SECONDS)),
    new Data(5, Observable.just("5").delay(1, TimeUnite.SECONDS))
    // ...
))
```

List will be populated correctly and values `["1", "2", "3", "4"]` will be populated after second.
But after user will scroll something weird happens. We will see `["2", "3", "4", "1"]` and then, after
a second, it will change to `["2", "3", "4", "5"]`, because `Observable<String> observable()` does not
have start value and first item was recycled to populate fifth element.

So you need to change your code to:

```java
public class Data {
    // ...
    public Observable<String> observable() {
        return observable.observeOn(uiScheduler).startWith("");
    }
    // ...
}
```

# TIPS

Using AutoValue plugin usually your code will be less potential to bugs.
