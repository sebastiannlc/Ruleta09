package Modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import Modelo.Usuario;

public class UsuarioTest {

    @Test
    void actualizarSaldo_MontoPositivo_IncrementaSaldoExactamente() {
        Usuario usuario = new Usuario("deposito", "123", "Depositante", 150.0);
        double deposito = 49.99;
        double saldoEsperado = 150.0 + 49.99; // 199.99

        usuario.actualizarSaldo(deposito);
        double saldoActual = usuario.getSaldo();

        assertEquals(saldoEsperado, saldoActual, 0.001, "El saldo debe incrementarse exactamente por el monto depositado.");
    }

    @Test
    void actualizarSaldo_MontoNegativo_ReduceSaldoCorrectamente() {
        Usuario usuario = new Usuario("retirador", "pass", "Retirador", 100.0);
        double perdida = -30.50;
        double saldoEsperado = 100.0 - 30.50; // 69.50

        usuario.actualizarSaldo(perdida);
        double saldoActual = usuario.getSaldo();

        assertEquals(saldoEsperado, saldoActual, 0.001, "El saldo debe reducirse por el monto negativo aplicado.");
    }

    @Test
    void constructor_SaldoNegativo_LanzaIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("fail", "pass", "Test", -0.01);
        }, "El constructor debe lanzar IllegalArgumentException.");

        assertEquals("Saldo inicial inválido", exception.getMessage(), "El mensaje de error debe ser 'Saldo inicial inválido'.");
    }
}