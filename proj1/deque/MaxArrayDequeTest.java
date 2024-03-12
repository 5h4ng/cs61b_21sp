package deque;

import jh61b.junit.In;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;

public class MaxArrayDequeTest {
    private static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return Integer.compare(o1, o2);
        }
    }

    private static class StringSizeComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return  Integer.compare(s1.length(), s2.length());
        }
    }

    private static class StringDictionaryComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return  String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
        }
    }

    StringSizeComparator str_size_cmp = new StringSizeComparator();
    IntegerComparator int_cmp = new IntegerComparator();
    StringDictionaryComparator str_dic_cmp = new StringDictionaryComparator();
    @Test
    public void testEmpty() {
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(int_cmp);
        assertEquals(null, deque.max());
    }

    @Test
    public void testIntegerComparator() {
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<>(int_cmp);
        deque.addLast(10);
        deque.addLast(20);
        deque.addLast(5);
        deque.addLast(3);
        Integer expectedMax = 20;
        Integer actualMax = deque.max();
        assertEquals(expectedMax, actualMax);
    }

    @Test
    public void testStringComparator() {
        MaxArrayDeque<String> deque = new MaxArrayDeque<>(str_dic_cmp);
        deque.addLast("aaaaaa");
        deque.addLast("bbbbb");
        deque.addLast("cccccccccccccc");
        deque.addLast("d");
        assertEquals("d", deque.max());
        assertEquals("cccccccccccccc", deque.max(str_size_cmp));

    }
}
