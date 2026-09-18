package ar.edu.backend.envios;

import java.util.List;

public final class ResultadoParseo {
    private final List<Envio> envios;
    private final int leidas;
    private final int descartadas;
    private final List<String> errores;
    private final List<String> descartes;

    public ResultadoParseo(List<Envio> envios, int leidas, int descartadas,
                           List<String> errores, List<String> descartes) {
        this.envios = List.copyOf(envios);
        this.leidas = leidas;
        this.descartadas = descartadas;
        this.errores = List.copyOf(errores);
        this.descartes = List.copyOf(descartes);
    }
    public List<Envio> getEnvios() { return envios; }
    public int getLeidas() { return leidas; }
    public int getDescartadas() { return descartadas; }
    public List<String> getErrores() { return errores; }
    public List<String> getDescartes() { return descartes; }
    public int getProcesadas() { return envios.size(); }
    public int getInvalidas() { return errores.size(); }
    public String resumen() {
        return "Leídas: %d | procesadas: %d | descartadas: %d | inválidas: %d | objetos: %d"
                .formatted(leidas, getProcesadas(), descartadas, getInvalidas(), envios.size());
    }
}
