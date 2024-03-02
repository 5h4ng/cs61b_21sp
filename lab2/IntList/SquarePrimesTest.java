package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesWithAllPrimes() {
        IntList lst = IntList.of(2, 3, 5, 7);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 9 -> 25 -> 49", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesWithNoPrimes() {
        IntList lst = IntList.of(4, 6, 8, 9, 10);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 6 -> 8 -> 9 -> 10", lst.toString());
        assertFalse(changed);
    }
    @Test
    public void testSquarePrimesWithLargePrime() {
        IntList lst = IntList.of(10, 15, 997);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("10 -> 15 -> 994009", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesWithSingleElementPrime() {
        IntList primeList = IntList.of(13);
        boolean changed = IntListExercises.squarePrimes(primeList);
        assertEquals("169", primeList.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesWithSingleElementComposite() {
        IntList compositeList = IntList.of(4);
        boolean changed = IntListExercises.squarePrimes(compositeList);
        assertEquals("4", compositeList.toString());
        assertFalse(changed);
    }

}
