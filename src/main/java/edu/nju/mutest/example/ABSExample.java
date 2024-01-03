package edu.nju.mutest.example;

public class ABSExample {
    public static int a;
    public static int b;
    public static int c;

    // 计算a乘以b的结果
    public static int test1() {
        return a * b;
    }

    // 计算a乘以b，再加上c的结果
    public static int test2() {
        return a * b + c;
    }

    // 计算a除以b的结果，如果b为0则返回0
    public static int test3() {
        if (b != 0) {
            return a / b;
        } else {
            System.out.println("Error: Division by zero.");
            return 0;
        }
    }

    // 计算a的平方
    public static int test4() {
        return a * a;
    }

    // 新增方法：计算a的绝对值
    public static int test5() {
        return Math.abs(a);
    }

}
