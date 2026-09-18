package ar.edu.backend.envios;

public class EnvioPrioritario extends Envio{

        public EnvioPrioritario(String id, String zona, int gramos, String modalidad) {
        super(id, zona, gramos, modalidad);
    }

    @Override 
    public int costo() { 
        if (getModalidad().equals("PRIORITARIO")) {
            return 500 + 200 * ((getGramos() + 999) / 1000) + 400;
        }
        return 500 + 200 * ((getGramos() + 999) / 1000); }
}
