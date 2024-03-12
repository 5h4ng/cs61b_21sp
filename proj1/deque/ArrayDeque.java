package deque;

import afu.org.checkerframework.checker.igj.qual.I;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private int size;
    private int nextFirst;
    private int nextLast;

    private T[] items;

     /*Creates an empty ArrayDeque*/
    public ArrayDeque() {
        size = 0;
        items = (T []) new Object[8];
        nextFirst = 4;
        nextLast = 5;
    }
    public void resize() {
        T[] newItems = (T[]) new Object[items.length * 2];
        int start = (nextFirst + 1) % items.length;
        for (int i = 0; i < size; i++) {
            newItems[i] = items[(start + i) % items.length];
        }
        items = newItems;
        nextFirst = items.length - 1;
        nextLast = size;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            resize();
        }
        items[nextFirst] = item;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            resize();
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int i = 1;
        int p = nextFirst + 1;
        while (i <= size) {
            i += 1;
            if (p == items.length) {
                p = 0;
            }
            System.out.print(items[p] + " ");
            p += 1;
        }
        System.out.println();
    }
    @Override
    public T removeFirst() {
        if (size == 0) return null;
        nextFirst = (nextFirst + 1) % items.length;
        T item = items[nextFirst];
        items[nextFirst] = null;
        size--;
        return item;
    }
    @Override
    public T removeLast() {
        if (size == 0) return null;
        nextLast = (nextLast - 1 + items.length) % items.length;
        T item = items[nextLast];
        items[nextLast] = null;
        size--;
        return item;
    }
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[(nextFirst + 1 + index) % items.length];
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int pos;
        public ArrayDequeIterator() {
            pos = 0;
        }
        @Override
        public boolean hasNext() {
            return pos < size;
        }
        @Override
        public T next() {
            T item = get(pos);
            pos += 1;
            return item;
        }
    }
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof  ArrayDeque)) {
            return false;
        }
        if (((ArrayDeque<?>) o).size() != this.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i ++) {
            if (this.get(i) != ((ArrayDeque<?>) o).get(i)) {
                return false;
            }
        }
        return true;
    }
}
