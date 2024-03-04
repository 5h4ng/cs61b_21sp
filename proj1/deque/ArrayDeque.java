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
        T[] newItems = (T []) new Object[items.length * 2];
        int p = nextFirst + 1;
        for (int i = 0 ; i < size; i ++) {
            if(p == items.length) {
                p = 0;
            }
            newItems[i] = items[p];
            p += 1;
        }
        items = newItems;
        nextFirst = newItems.length - 1;
        nextLast = size;
    }
    public void addFirst(T item) {
        if (items[nextFirst] != null) {
            resize();
        }
        items[nextFirst] = item;
        nextFirst -= 1;
        size += 1;
        if (nextFirst < 0) {
            nextFirst = items.length - 1;
        }
    }

    public void addLast(T item) {
        if (items[nextLast] != null) {
            resize();
        }
        items[nextLast] = item;
        nextLast += 1;
        size += 1;
        if (nextLast >= items.length) {
            nextLast = 0;
        }
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
        int p = nextFirst + 1;
        T item = items[p];
        if (p == items.length) {
            p = 0;
        }
        items[p] = null;
        nextFirst = p;
        size -= 1;
        return item;
    }

    public T removeLast() {
        int p = nextLast - 1;
        T item = items[p];
        if (p < 0) {
            p = items.length - 1;
        }
        items[p] = null;
        nextLast = p;
        size -= 1;
        return item;
    }

    public T get(int index) {
        int p = nextFirst + 1;
        for (int i = 0; i < index; i ++) {
            if (p == items.length) {
                p = 0;
            }
            p ++;
        }
        return items[p];
    }

/*    public Iterator<T> iterator() {

    }

    public boolean equals(Object o) {

    }*/
}
