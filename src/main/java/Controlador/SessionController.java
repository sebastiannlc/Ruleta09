package Controlador;

import Vista.VentanaLogin;
import Modelo.Usuario;
import Modelo.Resultado;
import java.util.List;

public class SessionController {

    private static SessionController instancia;
    private Usuario usuarioActual;

    private SessionController() {
    }

    public static SessionController getInstancia() {
        if (instancia == null) {
            instancia = new SessionController();
        }
        return instancia;
    }

    // Gestión de Sesión
    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Usuario autenticarYIniciarSesion(String username, String password) {

        Usuario usuario = VentanaLogin.USUARIOS.stream()
                .filter(u -> u.validarCredenciales(username, password))
                .findFirst()
                .orElse(null);

        if (usuario != null) {
            this.iniciarSesion(usuario);
            return usuario;
        } else {
            throw new IllegalStateException("Credenciales invalida. Usuario o contraseña incorrectos. ");
        }
    }

    public boolean usuarioExiste(String username) {
        return VentanaLogin.USUARIOS.stream()
                .anyMatch(u -> u.getUsername().equals(username));
    }

    public Usuario registrarNuevoUsuario(String username, String password, String nombre) {

        if (usuarioExiste(username)) {
            throw new IllegalStateException("Ese nombre de usuario ya existe, intente con otro.");
        }

        Usuario nuevoUsuario = new Usuario(username, password, nombre);
        VentanaLogin.USUARIOS.add(nuevoUsuario);

        VentanaLogin.guardarUsuarios();
        this.iniciarSesion(nuevoUsuario);
        return nuevoUsuario;
    }

    public List<Resultado> getHistorialPersonal() {
        if (usuarioActual != null) {
            return usuarioActual.getHistorialPersonal();
        }
        return List.of();
    }

    public String generarHistorialPersonalFormateado() {
        if (usuarioActual == null) {
            return "Error: No hay sesión de usuario activa.";
        }

        List<Resultado> historial = usuarioActual.getHistorialPersonal();

        if (historial.isEmpty()) {
            return "No hay resultados registrados en tu historial.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-8s %-10s %-10s\n", "Ronda", "Apuesta", "Número", "Ganancia Neta"));
        sb.append("-------------------------------------------\n");

        for (int i = 0; i < historial.size(); i++) {
            Resultado r = historial.get(i);
            String acierto = r.isAcierto() ? " (GANÓ)" : "";
            sb.append(String.format("%-10d %-8c %-10d %-10.2f %s\n",
                    i + 1,
                    r.getTipoApuesta(),
                    r.getNumero(),
                    r.getGanancia(),
                    acierto
            ));
        }

        return sb.toString();
    }
}