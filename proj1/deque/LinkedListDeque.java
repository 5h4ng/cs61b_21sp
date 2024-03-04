package deque;

import java.util.Iterator;

public class LinkedListDeque<T> {
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
    public void addFirst(T item) {
        Node n = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = n;
        sentinel.next = n;
        size += 1;
    }
    /*Adds an item of type T to the back of the deque.*/
    public void addLast(T item) {
        Node n = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = n;
        sentinel.prev = n;
        size += 1;
    }
    /*Returns true if deque is empty, false otherwise.*/
    public boolean isEmpty() {
        return size == 0;
    }
    /*Returns the number of items in the deque.*/
    public int size() {
        return size;
    }
    /*Prints the items in the deque from first to last, separated by a space.
    * Once all the items have been printed, print out a new line.*/
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
    public T get(int index) {
        int cnt = 0;
        Node p = sentinel.next;
        while (cnt < index) {
            cnt += 1;
            p = p.next;
        }
        return p.item;
    }

/*
    public Iterator<T> iterator() {

    }

    public boolean equals(Object o) {

    }
*/

}
