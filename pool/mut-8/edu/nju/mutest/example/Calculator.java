package edu.nju.mutest.example;

public class Calculator {

    private Calculator() {
    }

    public static int add(int a, int b) {
        return a + b;
    }

    public static int subtract(int a, int b) {
        return a - b;
    }

    public static int multiply(int a, int b) {
        return a * b;
    }

    public static int bitWise(int a) {
        int b = 3;
        // Unary operator applied: BITWISE_COMPLEMENT
        a = ~a + b;
        return a;
    }

    public static int minus(int a) {
        int b = 9;
        // Unary operator applied: MINUS
        a = a-- + b;
        return a;
    }

    public static boolean equal(int a) {
        int b = 10;
        // Binary operator applied: EQUAL
        return a == b;
    }
}
