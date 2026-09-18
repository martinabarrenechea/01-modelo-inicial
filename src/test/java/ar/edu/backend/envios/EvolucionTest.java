package ar.edu.backend.envios;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.*;
import java.io.IOException;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
class EvolucionTest {
    @TempDir Path dir;
    private ResultadoParseo leer(String filas) throws IOException {
        Path p=dir.resolve("nuevo.csv");
        Files.writeString(p,"id,zona,brutoGramos,embalajeGramos,estado,modalidad\n"+filas);
        return new ParserEnvios().leer(p);
    }
    @Test void nuevasTarifasYColector() throws IOException {
        var r=leer("A,N,1200,200,LISTO,NORMAL\nB,N,1200,200,LISTO,PRIORITARIO\nC,S,5200,200,LISTO,PRIORITARIO\n");
        assertEquals(3,r.getProcesadas()); assertEquals(0,r.getInvalidas());
        assertEquals(700,r.getEnvios().get(0).costo()); assertEquals(1100,r.getEnvios().get(1).costo());
        assertEquals(1900,r.getEnvios().get(2).costo());
        var c=new CentralEnvios(r.getEnvios());
        assertEquals(3700,c.total()); assertEquals(Map.of("N",1800,"S",1900),c.totalesPorZona());
        assertEquals(2,c.filtrar(e->e.costo()>1000).size());
    }
    @Test void limitesYClasificacion() throws IOException {
        var r=leer("A,N,5201,200,LISTO,PRIORITARIO\nB,N,1,0,LISTO,PRIORITARIO\nC,N,1,0,LISTO,\nD,N,1,0,LISTO,URGENTE\nE,N,0,0,CANCELADO,URGENTE\n");
        assertEquals(5,r.getLeidas()); assertEquals(1,r.getProcesadas()); assertEquals(3,r.getInvalidas());
        assertEquals(1,r.getDescartadas()); assertEquals(1100,r.getEnvios().get(0).costo());
    }
    @Test void formatoAnteriorSigueVigente() throws IOException {
        Path p=dir.resolve("viejo.csv");
        Files.writeString(p,"id,zona,brutoGramos,embalajeGramos,estado\nA,N,1200,200,LISTO\n");
        var r=new ParserEnvios().leer(p); assertEquals(1,r.getProcesadas()); assertEquals(700,r.getEnvios().get(0).costo());
    }
    @Test void anchoDebeCoincidirConCabecera() throws IOException {
        var r=leer("A,N,100,0,LISTO\nB,N,100,0,LISTO,NORMAL,extra\n");
        assertEquals(2,r.getInvalidas()); assertEquals(0,r.getProcesadas());
    }
}
