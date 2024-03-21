package com.nom.graalnom.test.java;

public class BranchTestJavaImpl {
    public boolean BranchTestMainJava() {
        int a = 10;
        int b = 256;
        int c = 1024;
        return Branch1TestJava(a, b, c) && Branch2TestJava(a, b, c);
    }

    private boolean Branch1TestJava(int a, int b, int c) {
        if (a > b) {
            if (a > c) {
                return true;
            } else {
                return false;
            }
        } else {
            if (b > c) {
                return false;
            }
        }
        return true;
    }

    private boolean Branch2TestJava(int a, int b, int c) {
        if (a > b) {
            return !Branch1TestJava(a, b, c);
        }
        return Branch1TestJava(a, b, c);
    }
}
