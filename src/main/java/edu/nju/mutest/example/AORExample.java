package edu.nju.mutest.example;

public class AORExample {
    public static int a;
    public static int b;
    public static int c;
    // 计算a和b的和
    public static int test6() {
        return a + b;
    }

    // 计算a减去b的结果
    public static int test7() {
        return a - b;
    }

    // 判断a是否为正数，是则返回1，否则返回0
    public static int test8() {
        return (a > 0) ? 1 : 0;
    }

    // 计算a除以b的余数
    public static int test9() {
        if (b != 0) {
            return a % b;
        } else {
            System.out.println("Error: Modulo by zero.");
            return 0;
        }
    }

    // 判断a是否为偶数，是则返回1，否则返回0
    public static int test10() {
        return (a % 2 == 0) ? 1 : 0;
    }
}
