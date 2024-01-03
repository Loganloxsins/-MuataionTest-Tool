package edu.nju.mutest.example;

import java.util.Arrays;

public class SortingTestSuite {

    public static void main(String[] args) {
        testInsertionSort();
        testIsSorted();
        testQuicksort();
    }

    private static void testInsertionSort() {
        int[] inputArray = {4, 2, 7, 1, 9, 3};
        int[] expectedArray = {1, 2, 3, 4, 7, 9};

        Sorting sorting = new Sorting();
        sorting.insertionSort(inputArray);

        assertArrayEquals(expectedArray, inputArray, "testInsertionSort");
    }

    private static void testIsSorted() {
        int[] sortedArray = {1, 2, 3, 4, 5};
        int[] unsortedArray = {4, 2, 7, 1, 9, 3};

        Sorting sorting = new Sorting();

        assertTrue(sorting.isSorted(sortedArray), "testIsSorted (sorted)");
        assertFalse(sorting.isSorted(unsortedArray), "testIsSorted (unsorted)");
    }

    private static void testQuicksort() {
        int[] unsortedArray = {4, 2, 7, 1, 9, 3};
        int[] expectedArray = {1, 2, 3, 4, 7, 9};

        Sorting.quicksort(unsortedArray);

        assertArrayEquals(expectedArray, unsortedArray, "testQuicksort");
    }

    private static void assertArrayEquals(int[] expected, int[] actual, String testName) {
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                throw new RuntimeException(String.format("[%s] failed! Expected: %s, Actual: %s",
                        testName, Arrays.toString(expected), Arrays.toString(actual)));
            }
        }
        System.out.println(String.format("[%s] pass!", testName));
    }

    private static void assertTrue(boolean condition, String testName) {
        if (!condition) {
            throw new RuntimeException(String.format("[%s] failed! Expected: true, Actual: false", testName));
        }
        System.out.println(String.format("[%s] pass!", testName));
    }

    private static void assertFalse(boolean condition, String testName) {
        if (condition) {
            throw new RuntimeException(String.format("[%s] failed! Expected: false, Actual: true", testName));
        }
        System.out.println(String.format("[%s] pass!", testName));
    }
}
