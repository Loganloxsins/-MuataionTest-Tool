package edu.nju.mutest.example;

/**
 * Test suite for {@link ABSExample}
 */
public class ABSExampleTestSuite {

    public static void main(String[] args) {
        testTest1();
        testTest2();
        testTest3();
        testTest4();
        testTest5();
    }

    private static void testTest1() {
        ABSExample.a = 2;
        ABSExample.b = 2;
        int oracle = 4;
        int res = ABSExample.test1();
        assertEqual("test1", oracle, res);
    }

    private static void testTest2() {
        ABSExample.a = 2;
        ABSExample.b = 3;
        ABSExample.c = 1;
        int oracle = 7;
        int res = ABSExample.test2();
        assertEqual("test2", oracle, res);
    }

    private static void testTest3() {
        ABSExample.a = 6;
        ABSExample.b = 2;
        int oracle = 3;
        int res = ABSExample.test3();
        assertEqual("test3", oracle, res);
    }

    private static void testTest4() {
        ABSExample.a = 3;
        int oracle = 9;
        int res = ABSExample.test4();
        assertEqual("test4", oracle, res);
    }

    private static void testTest5() {
        ABSExample.a = -5;
        int oracle = 5;
        int res = ABSExample.test5();
        assertEqual("test5", oracle, res);
    }

    private static void assertEqual(String testName, int expected, int actual) {
        if (expected == actual)
            System.out.println("[TEST] " + testName + "() pass!");
        else
            throw new RuntimeException("[TEST] " + testName + "() fail (" + expected + ", " + actual + ")!");
    }
}
