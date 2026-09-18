package ar.edu.backend.envios;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserEnvios {
    private static final String CABECERA = "id,zona,brutoGramos,embalajeGramos,estado";
    private static final String CABECERA_NUEVA = "id,zona,brutoGramos,embalajeGramos,estado,modalidad";

    public ResultadoParseo leer(Path archivo) throws IOException {
        List<Envio> envios = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        List<String> descartes = new ArrayList<>();
        int leidas = 0;
        try (BufferedReader reader = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            String encabezado = reader.readLine();
            if (!CABECERA.equals(encabezado) && !CABECERA_NUEVA.equals(encabezado)) {
                throw new IllegalArgumentException("encabezado incorrecto");
            }
            String linea;
            while ((linea = reader.readLine()) != null) {
                leidas++;
                int numero = leidas + 1;
                try {
                    String[] c = Arrays.stream(linea.split(",", -1)).map(String::strip).toArray(String[]::new);
                    if (c.length != 5 && c.length != 6) throw new IllegalArgumentException("cantidad de columnas incorrecta");
                    if (c[4].equals("CANCELADO")) {
                        descartes.add("Línea " + numero + ": CANCELADO");
                        continue;
                    }
                    if (!c[4].equals("LISTO")) throw new IllegalArgumentException("estado desconocido: " + c[4]);
                    if (c.length == 6 && (!c[5].equals("NORMAL") && !c[5].equals("PRIORITARIO"))) throw new IllegalArgumentException("modalidad desconocida: " + c[5]);
                    envios.add(Envio.desdeCampos(c));
                } catch (IllegalArgumentException e) {
                    errores.add("Línea " + numero + ": " + e.getMessage());
                }
            }
        }
        return new ResultadoParseo(envios, leidas, descartes.size(), errores, descartes);
    }
}
