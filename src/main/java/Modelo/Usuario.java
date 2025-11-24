package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String password;
    private final String nombre;
    private double saldo;

    private final List<Resultado> historialPersonal = new ArrayList<>();

    /**
     * Constructor principal con validación de saldo inicial.
     * Implementa el Caso de Prueba #1: Constructor rechaza saldo inicial negativo.
     */
    public Usuario(String username, String password, String nombre, double saldoInicial) {
        // CUMPLIMIENTO CASO #1: El constructor no debe permitir saldo < 0.
        // El mensaje debe ser "Saldo inicial inválido".
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Saldo inicial inválido");
        }

        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.saldo = saldoInicial;
    }

    /**
     * Constructor secundario con saldo por defecto.
     */
    public Usuario(String username, String password, String nombre) {
        // Llama al constructor principal con un saldo por defecto positivo (e.g., 1000.0)
        this(username, password, nombre, 1000.0);
    }

    public boolean validarCredenciales(String u, String p) {
        return this.username.equals(u) && this.password.equals(p);
    }

    // --- Métodos de Saldo y Operación (Cumplimiento Caso #2) ---

    public double getSaldo() {
        return saldo;
    }

    /**
     * Actualiza el saldo (depósito si es positivo, pérdida/retiro si es negativo).
     * Implementa el Caso de Prueba #2: Depósito válido incrementa el saldo.
     */
    public void actualizarSaldo(double cambio) {
        this.saldo += cambio;
    }

    // --- Métodos de Historial y Getters ---

    public void agregarResultado(Resultado resultado) {
        this.historialPersonal.add(resultado);
    }

    public List<Resultado> getHistorialPersonal() {
        return historialPersonal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUsername() {
        return username;
    }
}