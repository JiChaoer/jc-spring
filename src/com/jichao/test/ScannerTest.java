package com.jichao.test;

import com.jichao.spring.framework.scanner.PackageScanner;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ScannerTest {

    public static void main(String args[]) {
        Map<String, Class<?>> classes = PackageScanner.scanner("com.jichao");
        Set<Map.Entry<String, Class<?>>> set = classes.entrySet();
        Iterator<Map.Entry<String, Class<?>>> iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Class<?>> entry = iterator.next();
            System.out.println("key: " + entry.getKey());
            System.out.println("value: " + entry.getValue());
        }
    }
}
