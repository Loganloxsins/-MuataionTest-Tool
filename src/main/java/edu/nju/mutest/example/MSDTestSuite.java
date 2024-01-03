package edu.nju.mutest.example;

import java.util.Arrays;

public class MSDTestSuite {

    public static void main(String[] args) {
        testStringSort();
        testIntSort();
    }

    private static void testStringSort() {
        String[] inputArray = {"abc", "def", "xyz", "mno", "ghi", "jkl"};
        String[] expectedArray = {"abc", "def", "ghi", "jkl", "mno", "xyz"};

        MSD.sort(inputArray);

        assertArrayEquals(expectedArray, inputArray, "testStringSort");
    }

    private static void testIntSort() {
        int[] inputArray = {432, 123, 876, 234, 567, 789};
        int[] expectedArray = {123, 234, 432, 567, 789, 876};

        MSD.sort(inputArray);

        assertArrayEquals(expectedArray, inputArray, "testIntSort");
    }

    private static void assertArrayEquals(String[] expected, String[] actual, String testName) {
        if (!Arrays.equals(expected, actual)) {
            throw new RuntimeException(String.format("[%s] failed! Expected: %s, Actual: %s",
                    testName, Arrays.toString(expected), Arrays.toString(actual)));
        }
        System.out.println(String.format("[%s] pass!", testName));
    }

    private static void assertArrayEquals(int[] expected, int[] actual, String testName) {
        if (!Arrays.equals(expected, actual)) {
            throw new RuntimeException(String.format("[%s] failed! Expected: %s, Actual: %s",
                    testName, Arrays.toString(expected), Arrays.toString(actual)));
        }
        System.out.println(String.format("[%s] pass!", testName));
    }
}
