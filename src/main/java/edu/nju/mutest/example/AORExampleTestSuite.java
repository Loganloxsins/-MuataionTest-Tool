package edu.nju.mutest.example;

/**
 * Test suite for {@link AORExample}
 */
public class AORExampleTestSuite {

    public static void main(String[] args) {
        testTest6();
        testTest7();
        testTest8();
        testTest9();
        testTest10();
    }

    private static void testTest6() {
        AORExample.a = 2;
        AORExample.b = 3;
        int oracle = 5;
        int res = AORExample.test6();
        assertEqual("test6", oracle, res);
    }

    private static void testTest7() {
        AORExample.a = 5;
        AORExample.b = 3;
        int oracle = 2;
        int res = AORExample.test7();
        assertEqual("test7", oracle, res);
    }

    private static void testTest8() {
        AORExample.a = 7;
        int oracle = 1;
        int res = AORExample.test8();
        assertEqual("test8", oracle, res);
    }

    private static void testTest9() {
        AORExample.a = 10;
        AORExample.b = 3;
        int oracle = 1;
        int res = AORExample.test9();
        assertEqual("test9", oracle, res);
    }

    private static void testTest10() {
        AORExample.a = 4;
        int oracle = 1;
        int res = AORExample.test10();
        assertEqual("test10", oracle, res);
    }

    private static void assertEqual(String testName, int expected, int actual) {
        if (expected == actual)
            System.out.println("[TEST] " + testName + "() pass!");
        else
            throw new RuntimeException("[TEST] " + testName + "() fail (" + expected + ", " + actual + ")!");
    }
}
