package Modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import Modelo.ApuestaBase;
// Nota: Necesitas importar clases de Apuesta (si las usas para inicializar)
import Modelo.ApuestaRojo; // Asumiendo que existe
import Modelo.ApuestaNegro; // Asumiendo que existe
import Modelo.Resultado;
import Modelo.Estadisticas; // Asumimos esta clase existe

/**
 * Pruebas unitarias para la clase Estadisticas (Caso #5).
 */
public class EstadisticasTest {

    // Método auxiliar para crear un Resultado con los 5 argumentos requeridos por tu constructor
    private Resultado crearResultado(char etiqueta, boolean acierto, double monto) {
        // Valores simulados:
        int numeroGanador = 0; // No importa para las estadísticas
        double gananciaNeta = (acierto ? monto * 2 : -monto); // Solo para llenar el campo

        // El constructor requiere 5 argumentos: int, double, boolean, char, double
        return new Resultado(numeroGanador, gananciaNeta, acierto, etiqueta, monto);
    }

    // Método auxiliar para crear un Resultado Nulo/Rechazado (Apuesta Nula)
    private Resultado crearResultadoNulo() {
        // Para apuestas nulas o rechazadas (que deben ser ignoradas),
        // necesitamos valores que no sesguen la estadística.
        // Suponemos que la lógica de Estadísticas ignora los resultados donde la etiqueta es un valor por defecto (e.g., 'X', ' ')
        // o si los demás campos son cero/falsos.
        // Asumiremos que el constructor real permite pasar datos por defecto cuando es nulo:
        return new Resultado(0, 0.0, false, 'X', 0.0); // O utiliza los valores que tu clase Resultado maneje para una apuesta nula/rechazada
    }


    @Test
    void calcularEstadisticas_HistorialMixto_CalculaCorrectamente() {
        // ARRANGE: Crear un historial de resultados mixto:
        // Racha Máxima: 2 (A-A)
        // Tipo más frecuente: R (4 apuestas válidas)
        // Total de apuestas válidas: 6
        // Total de victorias: 2

        List<Resultado> historial = Arrays.asList(
                // 1. Apuesta nula (DEBE IGNORARSE)
                crearResultadoNulo(),

                // 2. Apuesta R - Fallo (Racha de Fallos: 1)
                crearResultado('R', false, 10),

                // 3. Apuesta R - Fallo (Racha de Fallos: 2)
                crearResultado('R', false, 10),

                // 4. Apuesta N - Fallo (Racha de Fallos: 3)
                crearResultado('N', false, 10),

                // 5. Apuesta N - Acierto (Racha de Aciertos: 1)
                crearResultado('N', true, 10),

                // 6. Apuesta R - Acierto (Racha de Aciertos: 2) -> Máxima racha es 2
                crearResultado('R', true, 10),

                // 7. Apuesta R - Fallo (Racha de Fallos: 1)
                crearResultado('R', false, 10)
        );

        // ACT
        Estadisticas estadisticas = new Estadisticas(historial);

        // ASSERT: Total, Victorias, Porcentaje
        assertEquals(6, estadisticas.getTotalJugadas(), "Debe contar solo las jugadas válidas (no nulas).");
        assertEquals(2, estadisticas.getVictorias(), "Debe contar solo los aciertos.");
        assertEquals(0.333333, estadisticas.getPorcentajeVictorias(), 0.000001, "El porcentaje de victorias debe ser correcto.");

        // ASSERT: Racha Máxima
        assertEquals(2, estadisticas.getRachaMaxima(), "La racha máxima de aciertos debe calcularse correctamente.");

        // ASSERT: Tipo más jugado
        assertEquals('R', estadisticas.getTipoMasJugado(), "El tipo de apuesta más frecuente debe ser 'R'.");
    }
}