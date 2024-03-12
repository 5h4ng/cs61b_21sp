package deque;
import deque.ArrayDeque;

import org.junit.Test;

import static org.junit.Assert.*;

public  class ArrayDequeTest {
    @Test
    public void addIsEmptySizeTest() {

        ArrayDeque<String> lld1 = new ArrayDeque<String>();

        assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
        lld1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

        lld1.addLast("middle");
        assertEquals(2, lld1.size());

        lld1.addLast("back");
        assertEquals(3, lld1.size());

        System.out.println("Printing out deque: ");
        lld1.printDeque();
    }
    @Test
    public void addFirstTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 20; i ++) {
            lld1.addFirst(i);
            assertEquals(Integer.valueOf(i), lld1.get(0));
        }
    }


    @Test
    public void resizeTest() {
        ArrayDeque<Integer> lld1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i ++){
            lld1.addLast(i);
        }
        assertEquals(10, lld1.size());
        for(int i = 0; i < 100; i ++){
            lld1.addFirst(i);
        }
        assertEquals(110, lld1.size());
    }

    @Test
    public void isEmptyTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertTrue(deque.isEmpty());

        deque.addFirst(1);
        assertFalse(deque.isEmpty());

        deque.removeFirst();
        assertTrue(deque.isEmpty());
    }

    @Test
    public void getTest() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);

        assertEquals(Integer.valueOf(1), deque.get(0));
        assertEquals(Integer.valueOf(2), deque.get(1));
        assertEquals(Integer.valueOf(3), deque.get(2));
    }
    @Test
    public void testAddAndGet() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < 8; i++) {
            deque.addLast(i);
        }

    }

    @Test
    public void testRemoveFromEmptyDeque() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        assertNull(deque.removeFirst());
        assertNull(deque.removeLast());
        deque.addLast(1);
        assertEquals(Integer.valueOf(1), deque.removeFirst());

    }

    @Test
    public void testFillUpEmptyFillUpAgain() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        // 填满队列
        for(int i = 0; i < 8; i++) {
            deque.addLast(i);
        }

        // 清空队列
        for(int i = 0; i < 8; i++) {
            assertEquals(Integer.valueOf(i), deque.removeFirst());
        }

        // 再次填满队列
        for(int i = 0; i < 8; i++) {
            deque.addLast(i + 10); // 使用不同的值以便区分
        }

        // 再次清空队列并检查值
        for(int i = 0; i < 8; i++) {
            Integer removed = deque.removeFirst();
            assertEquals(Integer.valueOf(i + 10), removed);
        }
    }

    @Test
    public void testNegativeSize() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        deque.removeFirst();
        deque.removeLast();
        assertTrue(deque.size() >= 0);
    }

    @Test
    public void testIterator() {
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < 20; i ++) {
            deque.addLast(i);
        }
        int pos = 0;
        for (int elem : deque) {
            assertEquals((long) elem, (long) deque.get(pos));
            pos += 1;
        }
    }

    @Test
    public void testEqual() {
        ArrayDeque<Integer> s1 = new ArrayDeque<>();
        ArrayDeque<Integer> s2 = new ArrayDeque<>();
        LinkedListDeque<Integer> l1 = new LinkedListDeque<>();
        l1.addLast(1);
        l1.addFirst(2);
        s1.addLast(1);
        s1.addFirst(2);
        s2.addLast(1);
        s2.addFirst(2);
        assertTrue(s1.equals(s2));
        s2.removeLast();
        assertFalse(s2.equals(s1));
        assertTrue(l1.equals(s1));
        assertTrue(s1.equals(l1));

    }
}