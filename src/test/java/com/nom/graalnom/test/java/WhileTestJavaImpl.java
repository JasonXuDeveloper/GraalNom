package com.nom.graalnom.test.java;

public class WhileTestJavaImpl {
    public boolean WhileTestMainJava() {
        int i = 3;
        int a = 10;
        while (i > 0) {
            i = i - 1;
            while (a > i) {
                a *= i;
            }
        }
        return a > i;
    }
}
