package ar.edu.backend.envios;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class EnvioTest {
    @Test void construccionYCalculo() {
        Envio e = Envio.desdeCampos(new String[]{"E", "Norte", "1200", "200", "LISTO"});
        assertEquals("E", e.getId()); assertEquals("Norte", e.getZona());
        assertEquals(1000, e.getGramos()); assertEquals(700, e.costo());
    }
    @Test void limites() {
        assertEquals(700, new Envio("E", "N", 1).costo());
        assertEquals(900, new Envio("E", "N", 1001).costo());
        assertEquals(6500, new Envio("E", "N", 30000).costo());
    }
    @Test void invalidos() {
        for (int peso : new int[]{0, -1, 30001}) assertThrows(IllegalArgumentException.class, () -> new Envio("E", "N", peso));
        assertThrows(IllegalArgumentException.class, () -> new Envio(" ", "N", 1));
        assertThrows(IllegalArgumentException.class, () -> new Envio("E", null, 1));
        assertThrows(IllegalArgumentException.class, () -> Envio.desdeCampos(new String[]{"E","N","100","100","LISTO"}));
        assertThrows(IllegalArgumentException.class, () -> Envio.desdeCampos(new String[]{"E","N","100","-1","LISTO"}));
    }
}
