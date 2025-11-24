package Controlador;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Controlador.SessionController;
import Modelo.Usuario;
import Vista.VentanaLogin;

import java.util.ArrayList;
import java.util.List;

public class SessionControllerTest {

    private SessionController sessionController;

    @BeforeEach
    void setUp() {
        sessionController = SessionController.getInstancia();

        VentanaLogin.USUARIOS.clear();
        sessionController.cerrarSesion(); // Asegurarse de que no haya sesión activa

        VentanaLogin.USUARIOS.add(new Usuario("usuarioExistente", "pass123", "Test", 100.0));
        VentanaLogin.guardarUsuarios();
    }

    @Test
    void autenticarYIniciarSesion_UsuarioNoRegistrado_LanzaIllegalStateException() {
        String usuarioInexistente = "noexiste";
        String passwordIncorrecta = "clavefalsa";

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            sessionController.autenticarYIniciarSesion(usuarioInexistente, passwordIncorrecta);
        }, "Debe lanzar IllegalStateException si las credenciales son inválidas o el usuario no existe.");

        assertEquals("Credenciales inválidas. Usuario o contraseña incorrectos.", exception.getMessage());

        assertNull(sessionController.getUsuarioActual(), "La sesión NO debe iniciarse tras un intento fallido.");
    }

    @Test
    void autenticarYIniciarSesion_CredencialesCorrectas_IniciaSesion() {
        String usuarioValido = "usuarioExistente";
        String passwordValida = "pass123";

        Usuario usuarioSesion = sessionController.autenticarYIniciarSesion(usuarioValido, passwordValida);

        assertNotNull(usuarioSesion, "Debe retornar un objeto Usuario.");
        assertEquals(usuarioValido, usuarioSesion.getUsername());
        assertNotNull(sessionController.getUsuarioActual(), "La sesión debe estar activa.");
    }
}