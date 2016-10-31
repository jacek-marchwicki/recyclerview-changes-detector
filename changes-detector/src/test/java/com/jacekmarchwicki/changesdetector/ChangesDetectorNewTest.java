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

        public void call(final List<Cat> newData) {
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
                        items.remove(start + i);
                        items.add(start + i, newData.get(start + i));
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
                    Collections.swap(items, fromPosition, toPosition);
                }
            }, newData, false);
        }

        @Nonnull
        public List<Cat> items() {
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

    static class ListGenerator {

        @Nonnull
        private static List<Cat> generateItems() {
            final Random random = new Random();

            final int itemsCount = randomBetween(random, 10, 30);
            final ArrayList<Cat> items = new ArrayList<>(itemsCount);
            for (int i = 0; i < itemsCount; ++i) {
                if (!randomWithGaussianBoolean(random, 2.0)) {
                    continue;
                }
                final String name = randomWithGaussianBoolean(random, 2.0) ? ("item" + i) : ("item" + i + " - changed");
                items.add(new Cat(i, name));
            }

            //        final int swapStart = randomBetween(random, 0, items.size() - 2);
            //        final int swapEnd = Math.min(items.size() - 1, swapStart + 2);
            //        Collections.swap(items, swapStart, swapEnd);
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
