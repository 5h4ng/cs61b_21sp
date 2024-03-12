package deque;

import afu.org.checkerframework.checker.oigj.qual.O;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    public class Node {
        public T item;
        public Node prev;
        public Node next;
        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }
    private int size = 0;
    private Node sentinel = new Node(null, null, null);
    /*Create an empty linked list deque.*/
    public LinkedListDeque() {
       sentinel.next = sentinel;
       sentinel.prev = sentinel;
    }

    /*Adds an item of type T to the front of the deque.
     You can assume that item is never null*/
    @Override
    public void addFirst(T item) {
        Node n = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = n;
        sentinel.next = n;
        size += 1;
    }
    /*Adds an item of type T to the back of the deque.*/
    @Override
    public void addLast(T item) {
        Node n = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = n;
        sentinel.prev = n;
        size += 1;
    }

    /*Returns the number of items in the deque.*/
    @Override
    public int size() {
        return size;
    }
    /*Prints the items in the deque from first to last, separated by a space.
    * Once all the items have been printed, print out a new line.*/
    @Override
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }
    /*Removes and returns the item at the front of the deque.
    * If no such item exists, return null*/
    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node first = sentinel.next;
        sentinel.next = first.next;
        first.next.prev = sentinel;
        size -= 1;
        return first.item;
    }
    /*Removes and returns the item at the back of the deque.
     * If no such item exists, return null*/
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node last = sentinel.prev;
        sentinel.prev = last.prev;
        last.prev.next = sentinel;
        size -= 1;
        return last.item;
    }
    /*Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
    * If no such item exists, returns null. Must not alter the deque!*/
    @Override
    public T get(int index) {
        int cnt = 0;
        Node p = sentinel.next;
        while (cnt < index) {
            cnt += 1;
            p = p.next;
        }
        return p.item;
    }
    private class LinkedListDequeIterator implements Iterator<T> {
        private int pos;
        public LinkedListDequeIterator() {
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
        return new LinkedListDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof  LinkedListDeque)) {
            return false;
        }
        if (((LinkedListDeque<?>) o).size() != this.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i ++) {
            if (this.get(i) != ((LinkedListDeque<?>) o).get(i)) {
                return false;
            }
        }
        return true;
    }
    private T getRecursiveHelper(int index, Node p) {
        if (index == 0) {
            return p.item;
        }
        return getRecursiveHelper(index - 1, p.next);
    }
    public T getRecursive(int index) {
        return getRecursiveHelper(index, sentinel.next);
    }

}
