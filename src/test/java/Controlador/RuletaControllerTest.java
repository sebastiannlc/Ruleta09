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
 * Pruebas unitarias para el metodo jugarRonda en RuletaController.
 */
public class RuletaControllerTest {

    private RuletaController ruletaController;
    private Usuario usuarioPrueba;
    private ApuestaBase apuestaBase;
    private double saldoInicial;

    @BeforeEach
    void setUp() {
        // 1. CONFIGURACIÓN DEL REPOSITORIO GLOBAL
        IRepositorioResultados repoPrueba = new RepositorioEnMemoria();
        new ResultadoController(repoPrueba, repoPrueba); // Inicializa el SessionController

        ruletaController = new RuletaController();

        saldoInicial = 100.0;
        usuarioPrueba = new Usuario("testUser", "pass", "Test", saldoInicial);
        SessionController.getInstancia().iniciarSesion(usuarioPrueba);

        apuestaBase = new ApuestaRojo(10.0);
    }

    /**
     * Prueba el flujo exitoso
     */
    @Test
    void jugarRonda_FlujoExitoso_ActualizaSaldoYRegistra() {
        double montoApuesta = apuestaBase.getMonto(); // 10.0

        Resultado resultado = ruletaController.jugarRonda(apuestaBase);

        double saldoFinal = usuarioPrueba.getSaldo();

        assertTrue(saldoFinal >= saldoInicial - montoApuesta, "El saldo final debe ser mayor o igual al saldo inicial menos la apuesta.");
        assertTrue(saldoFinal <= saldoInicial + montoApuesta, "El saldo final debe ser menor o igual al saldo inicial más la ganancia (1:1).");

        // Verifica que se registró el resultado en el historial personal
        assertEquals(1, usuarioPrueba.getHistorialPersonal().size(), "Debe haber un resultado registrado en el historial personal.");

        // Verifica que se registró el resultado en el historial global
    }

    /**
     * Apuesta con monto mayor al saldo.
     * Resultado esperado: IllegalStateException con mensaje específico.
     */
    @Test
    void jugarRonda_SaldoInsuficiente_LanzaIllegalStateException() {
        // Apuesta de $500.0 contra un saldo de $100.0
        ApuestaBase apuestaInvalida = new ApuestaRojo(500.0);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            ruletaController.jugarRonda(apuestaInvalida);
        }, "Debe lanzar IllegalStateException al apostar más del saldo.");

        assertEquals("Saldo insuficiente.", exception.getMessage(), "El mensaje de error debe ser específico.");

        // Verificamos que el saldo no haya cambiado
        assertEquals(saldoInicial, usuarioPrueba.getSaldo(), 0.001, "El saldo no debe alterarse tras la excepción.");
    }

    /**
     * Apuesta nula es rechazada.
     * Resultado esperado: IllegalArgumentException con mensaje específico.
     */
    @Test
    void jugarRonda_ApuestaNula_LanzaIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ruletaController.jugarRonda(null);
        }, "Debe lanzar IllegalArgumentException si la apuesta es null.");

        assertEquals("Apuesta requerida.", exception.getMessage(), "El mensaje de error debe ser específico.");
    }
}