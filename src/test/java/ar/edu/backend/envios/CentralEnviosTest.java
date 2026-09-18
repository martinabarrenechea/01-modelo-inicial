package ar.edu.backend.envios;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
class CentralEnviosTest {
    @Test void totalesYFiltro() {
        var c = new CentralEnvios(List.of(new Envio("A","N",1000),new Envio("B","N",1001),new Envio("C","S",1)));
        assertEquals(2300,c.total()); assertEquals(Map.of("N",1600,"S",700),c.totalesPorZona());
        assertEquals(2,c.filtrar(e->e.getGramos()<=1000).size());
        assertTrue(c.filtrar(e->e.getZona().equals("X")).isEmpty());
    }
    @Test void vacio() {
        var c=new CentralEnvios(List.of());
        assertEquals(0,c.total()); assertTrue(c.totalesPorZona().isEmpty()); assertTrue(c.filtrar(e->true).isEmpty());
    }
    @Test void copiaDefensiva() {
        var lista=new ArrayList<Envio>(); lista.add(new Envio("A","N",1));
        var c=new CentralEnvios(lista); lista.clear();
        assertEquals(700,c.total());
        assertThrows(UnsupportedOperationException.class,()->c.filtrar(e->true).clear());
    }
}
