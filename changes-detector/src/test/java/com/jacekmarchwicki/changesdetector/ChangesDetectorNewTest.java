package com.jacekmarchwicki.changesdetector;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import static com.google.common.truth.Truth.assert_;

public class ChangesDetectorNewTest {

    private class TestAdapter {
        private final java.util.List<Cat> items = new ArrayList<>();
        private ChangesDetector<Cat, Cat> detector = new ChangesDetector<>(new SimpleDetector<Cat>());

        void call(final List<Cat> newData) {
            detector.newData(new ChangesDetector.ChangesAdapter() {
                @Override
                public void notifyItemRangeInserted(int start, int count) {
                    for (int i = 0; i < count; ++i) {
                        items.add(start + i, newData.get(start + i));
                    }
                }

                @Override
                public void notifyItemRangeChanged(int start, int count) {
                    for (int i = 0; i < count; ++i) {
                        items.set(start + i, newData.get(start + i));
                    }
                }

                @Override
                public void notifyItemRangeRemoved(int start, int count) {
                    for (int i = 0; i < count; ++i) {
                        items.remove(start);
                    }
                }

                @Override
                public void notifyItemMoved(int fromPosition, int toPosition) {
                    items.add(toPosition, items.remove(fromPosition));
//                    Collections.swap(items, fromPosition, toPosition);
                }
            }, newData, false);
        }

        @Nonnull
        List<Cat> items() {
            return items;
        }
    }

    @Test
    public void testAddFirstItem_dataEquals() throws Exception {
        final TestAdapter testAdapter = new TestAdapter();

        final ImmutableList<Cat> data = ImmutableList.of(new Cat(1));
        testAdapter.call(data);

        assert_().that(testAdapter.items()).isEqualTo(data);
    }

    @Test
    public void testSwapItemsWithNoChange_dataEquals() throws Exception {
        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)),
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)));
    }
    @Test
    public void testSwapItemsInTheMiddle_dataEquals() throws Exception {

        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)),
                ImmutableList.of(new Cat(1), new Cat(3), new Cat(2), new Cat(4)));
    }
    @Test
    public void testSwapItemsAndRemoveLast_dataEquals() throws Exception {

        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)),
                ImmutableList.of(new Cat(1), new Cat(3), new Cat(2)));
    }
    @Test
    public void testSwapItemsAtTheEndAndRemoveFirst_dataEquals() throws Exception {

        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3)),
                ImmutableList.of(new Cat(3), new Cat(2)));
    }
    @Test
    public void testSwapItemsAndRemoveFirst_dataEquals() throws Exception {
        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)),
                ImmutableList.of(new Cat(3), new Cat(2), new Cat(4)));
    }
    @Test
    public void testSwapItemsAndChangeLast_dataEquals() throws Exception {

        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)),
                ImmutableList.of(new Cat(1), new Cat(3), new Cat(2), new Cat(4).withName("krowa")));
    }

    @Test
    public void testSwapItemsAndChangeMiddle_dataEquals() throws Exception {
        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)),
                ImmutableList.of(new Cat(1), new Cat(3), new Cat(2).withName("krowa"), new Cat(4)));

        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)),
                ImmutableList.of(new Cat(1), new Cat(3).withName("krowa"), new Cat(2), new Cat(4)));
    }

    @Test
    public void testSwapItemsAndChangeFirst_dataEquals() throws Exception {
        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(1), new Cat(2), new Cat(3), new Cat(4)),
                ImmutableList.of(new Cat(1).withName("krowa"), new Cat(3), new Cat(2), new Cat(4)));
    }



    @Test
    public void testAfterChangeOrderAndDeleteSomeItem_orderAndDeleteAreNotified3() throws Exception {
        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(0), new Cat(1), new Cat(2)),
                ImmutableList.of(new Cat(2), new Cat(0)));
    }

    @Test
    public void testAfterChangeOrderAndDeleteSomeItem_orderAndDeleteAreNotified4() throws Exception {
        swapDataAndCheckIfEquals(
                ImmutableList.of(new Cat(0), new Cat(1), new Cat(2)),
                ImmutableList.of(new Cat(2), new Cat(1), new Cat(0)));
    }

    private void swapDataAndCheckIfEquals(@Nonnull ImmutableList<Cat> startData, @Nonnull ImmutableList<Cat> newData) {
        final TestAdapter testAdapter = new TestAdapter();
        System.out.println("Start data: " + startData);
        testAdapter.call(startData);

        testAdapter.call(newData);

        assert_().that(testAdapter.items()).isEqualTo(newData);
    }

    @Test
    public void testRandomElements_dataEquals() throws Exception {
        for (int i = 0; i < 100; ++i) {
            final TestAdapter testAdapter = new TestAdapter();
            for (int j = 0; j < 1000; ++j) {
                final List<Cat> previousData = new ArrayList<>(testAdapter.items());
                System.out.println("Previous data: " + previousData);
                final List<Cat> newItems = ListGenerator.generateItems();
                testAdapter.call(newItems);
                assert_().that(testAdapter.items()).isEqualTo(newItems);
            }
        }
    }

    private static class Cat implements SimpleDetector.Detectable<Cat> {
        private final int id;
        private final String name;

        private Cat(int id, String name) {
            this.id = id;
            this.name = name;
        }

        Cat(int id) {
            this.id = id;
            name = generateName(id);
        }

        Cat withName(String name) {
            return new Cat(id, name);
        }

        @Nonnull
        private String generateName(int id) {
            switch (id) {
                case 0:
                    return "zero";
                case 1:
                    return "one";
                case 2:
                    return "two";
                case 3:
                    return "tree";
                case 4:
                    return "four";
                case 5:
                    return "five";
                default:
                    throw new RuntimeException("Unknown id: " + id);
            }
        }

        @Override
        public String toString() {
            return "Cat{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean matches(@Nonnull Cat item) {
            return item.id == id;
        }

        @Override
        public boolean same(@Nonnull Cat item) {
            return equals(item);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cat)) return false;
            final Cat cat = (Cat) o;
            return id == cat.id &&
                    Objects.equal(name, cat.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id, name);
        }
    }

    private static class ListGenerator {
        final static Random random = new Random();

        @Nonnull
        private static List<Cat> generateItems() {
            final int itemsCount = randomBetween(random, 10, 30);
            final ArrayList<Cat> items = new ArrayList<>(itemsCount);
            for (int i = 0; i < itemsCount; ++i) {
                if (!randomWithGaussianBoolean(random, 2.0)) {
                    continue;
                }
                final String name = randomWithGaussianBoolean(random, 2.0) ? ("item" + i) : ("item" + i + " - changed");
                items.add(new Cat(i, name));
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
}
