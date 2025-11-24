package Controlador;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controlador.SessionController;
import Modelo.Usuario;
import Vista.VentanaLogin;

import java.util.ArrayList;

public class SessionControllerTest {

    private SessionController sessionController;
    private final double saldoInicial = 100.0;
    private final String usuarioExistente = "usuarioExistente";
    private final String passwordValida = "pass123";

    @BeforeEach
    void setUp() {
        sessionController = SessionController.getInstancia();

        VentanaLogin.USUARIOS.clear();
        sessionController.cerrarSesion();

        VentanaLogin.USUARIOS.add(new Usuario(usuarioExistente, passwordValida, "Test", saldoInicial));
    }

    @Test
    void autenticarYIniciarSesion_CredencialesCorrectas_IniciaSesion() {
        Usuario usuarioSesion = sessionController.autenticarYIniciarSesion(usuarioExistente, passwordValida);

        assertNotNull(usuarioSesion, "Debe retornar el objeto Usuario.");
        assertEquals(usuarioExistente, usuarioSesion.getUsername());
        assertNotNull(sessionController.getUsuarioActual(), "La sesión debe estar activa.");
    }

    @Test
    void autenticarYIniciarSesion_UsernameNulo_LanzaIllegalArgumentException() {
        String usernameNulo = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sessionController.autenticarYIniciarSesion(usernameNulo, passwordValida);
        }, "Debe lanzar IllegalArgumentException si el username es null.");

        assertEquals("El nombre de usuario no puede ser nulo o vacío.", exception.getMessage());
        assertNull(sessionController.getUsuarioActual(), "La sesión NO debe iniciarse.");
    }

    @Test
    void autenticarYIniciarSesion_UsuarioNoRegistrado_LanzaIllegalStateException() {
        String usuarioInexistente = "noexiste";

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            sessionController.autenticarYIniciarSesion(usuarioInexistente, "clavefalsa");
        }, "Debe lanzar IllegalStateException si las credenciales son inválidas.");

        assertEquals("Credenciales inválidas. Usuario o contraseña incorrectos.", exception.getMessage());
        assertNull(sessionController.getUsuarioActual(), "La sesión NO debe iniciarse tras un intento fallido.");
    }
}