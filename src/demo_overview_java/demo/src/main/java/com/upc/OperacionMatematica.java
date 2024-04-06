package com.upc;

//Lambda = (parametros) -> {cuerpo de lambda}

@FunctionalInterface
public interface OperacionMatematica {
    double aplicar(double a, double b);
}
