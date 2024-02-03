
import com.example.luckysix.utill.Combination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
public class CombinationTest {
    private Combination combination;

    @BeforeEach
    public void setUp() {
        combination = new Combination();
    }

    @Test
    public void testSort() {
        try {
            combination.add(5);
            combination.add(3);
            combination.add(7);
        } catch (Combination.ValueNotUniqueException e) {
            throw new RuntimeException(e);
        }
        combination.sort();
        assertEquals(Arrays.asList(3, 5, 7), combination.getNumbers());
    }

    @Test
    public void testGetNumbers() {
        try {
            combination.add(5);
            combination.add(3);
        } catch (Combination.ValueNotUniqueException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Integer> expected = new ArrayList<>(Arrays.asList(3, 5));
        assertEquals(expected, combination.getNumbers());
    }

    @Test
    public void testContains() {
        try {
            combination.add(5);
        } catch (Combination.ValueNotUniqueException e) {
            throw new RuntimeException(e);
        }
        assertTrue(combination.contains(5));
        assertFalse(combination.contains(3));
    }

    @Test
    public void testAdd() throws Combination.ValueNotUniqueException {
        assertTrue(combination.add(5));
        assertFalse(combination.add(5));
    }

    @Test
    public void testRemove() throws Combination.ValueNotUniqueException {
        combination.add(5);
        assertTrue(combination.remove(5));
        assertFalse(combination.remove(3));
    }

    @Test
    public void testIsDone() throws Combination.ValueNotUniqueException {
        combination.add(5);
        assertFalse(combination.isDone());
        combination.add(3);
        combination.add(7);
        combination.add(9);
        combination.add(11);
        combination.add(13);
        assertTrue(combination.isDone());
    }

    @Test
    public void testClear() throws Combination.ValueNotUniqueException {
        combination.add(5);
        combination.clear();
        assertTrue(combination.getNumbers().isEmpty());
    }

    @Test
    public void testRandom() {
        combination.random();
        assertTrue(combination.isDone());
    }

    @Test
    public void testCheck() throws Combination.ValueNotUniqueException {
        combination.add(3);
        combination.add(7);
        combination.add(11);
        combination.add(15);
        combination.add(19);
        combination.add(23);
        assertTrue(combination.check(new int[]{3, 7, 11, 15, 19, 23}));
        assertFalse(combination.check(new int[]{3, 7, 11, 15, 19, 25}));
    }
}
