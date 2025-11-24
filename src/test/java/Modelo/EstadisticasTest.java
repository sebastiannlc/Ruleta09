package Modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import Modelo.ApuestaBase;
import Modelo.ApuestaRojo;
import Modelo.ApuestaNegro;
import Modelo.Resultado;
import Modelo.Estadisticas;


public class EstadisticasTest {

    private Resultado crearResultado(char etiqueta, boolean acierto, double monto) {
        int numeroGanador = 0;
        double gananciaNeta = (acierto ? monto * 2 : -monto); // Solo para llenar el campo

        return new Resultado(numeroGanador, gananciaNeta, acierto, etiqueta, monto);
    }

    private Resultado crearResultadoNulo() {
        return new Resultado(0, 0.0, false, 'X', 0.0); // O utiliza los valores que tu clase Resultado maneje para una apuesta nula/rechazada
    }


    @Test
    void calcularEstadisticas_HistorialMixto_CalculaCorrectamente() {

        List<Resultado> historial = Arrays.asList(
                crearResultadoNulo(),

                crearResultado('R', false, 10),

                crearResultado('R', false, 10),

                crearResultado('N', false, 10),

                crearResultado('N', true, 10),

                crearResultado('R', true, 10),

                crearResultado('R', false, 10)
        );

        Estadisticas estadisticas = new Estadisticas(historial);

        assertEquals(6, estadisticas.getTotalJugadas(), "Debe contar solo las jugadas válidas (no nulas).");
        assertEquals(2, estadisticas.getVictorias(), "Debe contar solo los aciertos.");
        assertEquals(0.333333, estadisticas.getPorcentajeVictorias(), 0.000001, "El porcentaje de victorias debe ser correcto.");

        assertEquals(2, estadisticas.getRachaMaxima(), "La racha máxima de aciertos debe calcularse correctamente.");

        assertEquals('R', estadisticas.getTipoMasJugado(), "El tipo de apuesta más frecuente debe ser 'R'.");
    }
}