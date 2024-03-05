package deque;

import java.util.Iterator;

public class ArrayDeque<T> {
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
    public void addFirst(T item) {
        if (size == items.length) {
            resize();
        }
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        items[nextFirst] = item;
        size++;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize();
        }
        items[nextLast] = item;
        nextLast = (nextLast + 1) % items.length;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

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

    public T removeFirst() {
        if (size == 0) return null;
        nextFirst = (nextFirst + 1) % items.length;
        T item = items[nextFirst];
        items[nextFirst] = null;
        size--;
        return item;
    }

    public T removeLast() {
        if (size == 0) return null;
        nextLast = (nextLast - 1 + items.length) % items.length;
        T item = items[nextLast];
        items[nextLast] = null;
        size--;
        return item;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[(nextFirst + 1 + index) % items.length];
    }

/*    public Iterator<T> iterator() {

    }

    public boolean equals(Object o) {

    }*/
}
