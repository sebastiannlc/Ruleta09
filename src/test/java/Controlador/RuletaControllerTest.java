package Controlador;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controlador.RuletaController;
import Controlador.ResultadoController;
import Controlador.SessionController;
import Interfaces.IRepositorioResultados;
import Modelo.ApuestaBase;
import Modelo.ApuestaRojo;
import Modelo.Usuario;
import Modelo.Resultado;
import Persistencia.RepositorioEnMemoria;

/**
 * Pruebas unitarias para el método jugarRonda en RuletaController.
 * Cubre los Casos de Prueba #3 (Apuesta nula), #4 (Saldo insuficiente), y el flujo normal.
 */
public class RuletaControllerTest {

    private RuletaController ruletaController;
    private Usuario usuarioPrueba;
    private double saldoInicial;

    @BeforeEach
    void setUp() {
        // 1. CONFIGURACIÓN DEL REPOSITORIO GLOBAL
        IRepositorioResultados repoPrueba = new RepositorioEnMemoria();
        new ResultadoController(repoPrueba, repoPrueba);

        ruletaController = new RuletaController();

        saldoInicial = 100.0;
        usuarioPrueba = new Usuario("testUser", "pass", "Test", saldoInicial);
        SessionController.getInstancia().iniciarSesion(usuarioPrueba);
    }

    /**
     * Apuesta nula es rechazada.
     * Resultado esperado: IllegalArgumentException con mensaje "Apuesta requerida.".
     */
    @Test
    void jugarRonda_ApuestaNula_LanzaIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ruletaController.jugarRonda(null);
        }, "Debe lanzar IllegalArgumentException si la apuesta es null.");

        assertEquals("Apuesta requerida.", exception.getMessage(), "El mensaje de error debe ser específico.");

        assertEquals(0, usuarioPrueba.getHistorialPersonal().size(), "No se debe registrar ninguna actividad.");
    }

    /**
     * Corresponde al Caso de Prueba #4: Apuesta con monto mayor al saldo.
     * Resultado esperado: IllegalStateException con mensaje "Saldo insuficiente.".
     */
    @Test
    void jugarRonda_SaldoInsuficiente_LanzaIllegalStateException() {
        ApuestaBase apuestaInvalida = new ApuestaRojo(100.01);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ruletaController.jugarRonda(apuestaInvalida);
        }, "Debe lanzar IllegalStateException al apostar más del saldo.");

        assertEquals("Saldo insuficiente.", exception.getMessage(), "El mensaje de error debe ser específico.");

        assertEquals(saldoInicial, usuarioPrueba.getSaldo(), 0.001, "El saldo no debe alterarse tras la excepción.");
    }

    /**
     * El saldo se actualiza y el resultado se registra.
     * CUMPLIMIENTO: Verifica la ejecución del juego sin errores.
     */
    @Test
    void jugarRonda_FlujoExitoso_ActualizaSaldoYRegistra() {
        // Arrange
        double montoApuesta = 10.0;
        ApuestaBase apuestaValida = new ApuestaRojo(montoApuesta);

        Resultado resultado = ruletaController.jugarRonda(apuestaValida);

        double saldoFinal = usuarioPrueba.getSaldo();

        assertTrue(saldoFinal >= saldoInicial - montoApuesta, "El saldo final debe ser mayor o igual al saldo inicial menos la apuesta.");
        assertTrue(saldoFinal <= saldoInicial + montoApuesta, "El saldo final debe ser menor o igual al saldo inicial más la ganancia máxima (1:1).");

        assertEquals(1, usuarioPrueba.getHistorialPersonal().size(), "Debe haber un resultado registrado.");
    }
}