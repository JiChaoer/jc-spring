package com.jichao.test;

import com.jichao.spring.framework.scanner.PackageScanner;

import java.util.Map;

public class ScannerTest {

    public static void main(String args[]) {
        Map<String, Class<?>> classes = PackageScanner.scanner("com.jichao");
        classes.forEach((k, v) -> {
            System.out.println("key: " + k);
            System.out.println("value: " + v);
        });
    }
}
