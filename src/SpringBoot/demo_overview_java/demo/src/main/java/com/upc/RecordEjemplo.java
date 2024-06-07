package com.upc;

public class RecordEjemplo {
    public static void main(String[] args) {
        Persona person = new Persona("Henry", 43);
        System.out.println(person.nombre());
        System.out.println(person.edad());
    }

    public static void soyUnMetodo(Persona... p) {

    }

}
