package com.upc;

public interface Interfaz {
    void sumar(int a, int b);

    default void agregar() {
        System.out.println("Metodo con implementacion");
    }

}
