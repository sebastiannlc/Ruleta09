package Modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import Modelo.Usuario;

/**
 * Pruebas unitarias para la lógica de saldo de la clase Usuario.
 * Incluye la validación de depósito (Caso #2) y la validación de constructor (Caso #1).
 */
public class UsuarioTest {

    // Asumimos que el constructor de Usuario está:
    // public Usuario(String username, String password, String nombre, double saldoInicial) { ... }

    // --- Caso de Prueba #2: Depósito válido incrementa el saldo ---

    /**
     * Prueba que el método actualizarSaldo incremente correctamente el saldo
     * cuando se proporciona un monto positivo (simulando un depósito).
     * CUMPLIMIENTO: Caso #2.
     */
    @Test
    void actualizarSaldo_MontoPositivo_IncrementaSaldoExactamente() {
        // Arrange
        Usuario usuario = new Usuario("deposito", "123", "Depositante", 150.0);
        double deposito = 49.99;
        double saldoEsperado = 150.0 + 49.99; // 199.99

        // Act
        usuario.actualizarSaldo(deposito);
        double saldoActual = usuario.getSaldo();

        // Assert
        // Usamos delta de 0.001 para comparar doubles
        assertEquals(saldoEsperado, saldoActual, 0.001, "El saldo debe incrementarse exactamente por el monto depositado.");
    }

    // --- Caso Auxiliar (Verificación de Pérdida) ---

    /**
     * Prueba que el método actualizarSaldo reduzca correctamente el saldo
     * cuando se proporciona un monto negativo (simulando una pérdida de ruleta).
     */
    @Test
    void actualizarSaldo_MontoNegativo_ReduceSaldoCorrectamente() {
        // Arrange
        Usuario usuario = new Usuario("retirador", "pass", "Retirador", 100.0);
        double perdida = -30.50;
        double saldoEsperado = 100.0 - 30.50; // 69.50

        // Act
        usuario.actualizarSaldo(perdida);
        double saldoActual = usuario.getSaldo();

        // Assert
        assertEquals(saldoEsperado, saldoActual, 0.001, "El saldo debe reducirse por el monto negativo aplicado.");
    }

    // --- Caso de Prueba #1: Constructor rechaza saldo negativo (Reintroducido para completitud) ---

    /**
     * Prueba que el constructor de Usuario rechace un saldo inicial negativo.
     * CUMPLIMIENTO: Caso #1.
     * Requiere que el constructor de Usuario lance IllegalArgumentException si saldoInicial < 0.
     */
    @Test
    void constructor_SaldoNegativo_LanzaIllegalArgumentException() {
        // Asumiendo la modificación en el constructor de Usuario:
        // if (saldoInicial < 0) throw new IllegalArgumentException("Saldo inicial inválido");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("fail", "pass", "Test", -0.01);
        }, "El constructor debe lanzar IllegalArgumentException.");

        assertEquals("Saldo inicial inválido", exception.getMessage(), "El mensaje de error debe ser 'Saldo inicial inválido'.");
    }
}