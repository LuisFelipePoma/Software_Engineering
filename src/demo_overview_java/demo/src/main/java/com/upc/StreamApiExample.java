package com.upc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StreamApiExample {
    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Operacion duplicar cada numero
        List<Integer> duplicados = numeros.stream()
                .filter(numero -> numero > 3)
                .map(numero -> numero * 2)
                .toList();
        System.out.println("Duplicados:" + duplicados);

        Optional<Integer> primerNumero = numeros
                .stream()
                .findFirst();

        primerNumero.ifPresent(numero -> System.out.println(numero));

        var primerNumero01 = numeros
                .stream()
                .findFirst()
                .orElse(0);
        System.out.println(primerNumero01);

        List<List<Integer>> listaDeListas = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9));

        List<Integer> listaPlana = listaDeListas
                .stream()
                .flatMap(List::stream)
                .toList();
        System.out.println(listaPlana);

    }
}
