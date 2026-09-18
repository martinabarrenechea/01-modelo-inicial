package ar.edu.backend.envios;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
class ParserEnviosTest {
    @TempDir Path dir;
    @Test void archivoProvisto() throws IOException {
        var r = new ParserEnvios().leer(Path.of("datos/datos.csv"));
        assertEquals(60, r.getLeidas()); assertEquals(39, r.getProcesadas());
        assertEquals(6, r.getDescartadas()); assertEquals(15, r.getInvalidas());
        assertEquals(39, r.getEnvios().size()); assertEquals(1000, r.getEnvios().get(0).getGramos());
        assertEquals("Leídas: 60 | procesadas: 39 | descartadas: 6 | inválidas: 15 | objetos: 39", r.resumen());
        assertTrue(r.getErrores().get(0).startsWith("Línea 10:"));
        assertEquals(6, r.getDescartes().size());
        assertEquals(r.getLeidas(), r.getProcesadas()+r.getDescartadas()+r.getInvalidas());
    }
    @Test void agregadosYTestigos() throws IOException {
        var r = new ParserEnvios().leer(Path.of("datos/datos.csv"));
        var c = new CentralEnvios(r.getEnvios());
        assertEquals(86100, c.total());
        assertEquals(java.util.Map.of("Centro",30700,"Norte",28500,"Sur",26900), c.totalesPorZona());
        assertEquals(8, c.filtrar(e -> e.getGramos() <= 1000).size());
        assertEquals(30000, c.filtrar(e -> e.getId().equals("E06")).get(0).getGramos());
        assertEquals(1, c.filtrar(e -> e.getId().equals("I013")).get(0).getGramos());
        assertTrue(c.filtrar(e -> e.getId().equals("I049")).isEmpty());
        assertTrue(r.getErrores().stream().anyMatch(e -> e.startsWith("Línea 50:")));
        assertTrue(r.getDescartes().contains("Línea 47: CANCELADO"));
    }
    @Test void resultadoConservaCopiasDefensivas() {
        var envios = new java.util.ArrayList<Envio>();
        var errores = new java.util.ArrayList<String>();
        var descartes = new java.util.ArrayList<String>();
        envios.add(new Envio("A", "N", 1)); errores.add("error"); descartes.add("descarte");
        var r = new ResultadoParseo(envios, 3, 1, errores, descartes);
        envios.clear(); errores.clear(); descartes.clear();
        assertEquals(1, r.getProcesadas()); assertEquals(1, r.getInvalidas());
        assertEquals(java.util.List.of("descarte"), r.getDescartes());
        assertThrows(UnsupportedOperationException.class, () -> r.getEnvios().clear());
        assertThrows(UnsupportedOperationException.class, () -> r.getErrores().clear());
        assertThrows(UnsupportedOperationException.class, () -> r.getDescartes().clear());
    }
    @Test void estructuraYContinuacion() throws IOException {
        Path p = dir.resolve("x.csv");
        Files.writeString(p, "id,zona,brutoGramos,embalajeGramos,estado\n\nE,N,100,0,LISTO,extra\nE,N,100,0,LISTO\n");
        var r = new ParserEnvios().leer(p);
        assertEquals(3,r.getLeidas()); assertEquals(2,r.getInvalidas()); assertEquals(1,r.getProcesadas());
    }
    @Test void soloCabeceraYErroresDeArchivo() throws IOException {
        Path p=dir.resolve("x.csv");
        Files.writeString(p,"id,zona,brutoGramos,embalajeGramos,estado\n");
        assertEquals(0,new ParserEnvios().leer(p).getLeidas());
        Files.writeString(p,"mal\n");
        assertThrows(IllegalArgumentException.class,()->new ParserEnvios().leer(p));
        assertThrows(IOException.class,()->new ParserEnvios().leer(dir.resolve("ausente")));
    }
}
