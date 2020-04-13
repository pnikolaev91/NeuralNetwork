package ru.sample.XY;

import com.sun.org.apache.xpath.internal.operations.String;

public class Main {
    /*
        Frame taken from https://github.com/Elco-/SimpleNN
     */
    public static void main(String[] args) {
        FormDots f = new FormDots();
        new Thread(f).start();
    }
}