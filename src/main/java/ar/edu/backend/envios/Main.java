package ar.edu.backend.envios;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path archivo = Path.of(args.length == 0 ? "datos/datos-parcial.csv" : args[0]);
        ResultadoParseo resultado = new ParserEnvios().leer(archivo);
        CentralEnvios central = new CentralEnvios(resultado.getEnvios());
        System.out.println(resultado.resumen());
        resultado.getDescartes().forEach(System.out::println);
        resultado.getErrores().forEach(System.out::println);
        System.out.println(central.panorama());
        System.out.println(central.contarModalidad());

    }
}
