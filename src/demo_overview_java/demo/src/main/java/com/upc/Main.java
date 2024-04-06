package com.upc;

public class Main {
    public static void main(String[] args) {
        OperacionMatematica suma = (a, b) -> a + b;
        OperacionMatematica resta = (a, b) -> a - b;

        System.out.println("Suma:" +
                suma.aplicar(5, 3));
        System.out.println("Resta:" +
                resta.aplicar(5, 3));

    }
}