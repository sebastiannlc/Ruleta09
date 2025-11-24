package Vista;

import Utilidades.GestorPersistencia;
import Modelo.Usuario;
import Controlador.SessionController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaLogin {

    private static final String ARCHIVO_USUARIOS = "usuarios.dat";

    public static final List<Usuario> USUARIOS =
            GestorPersistencia.cargarDatos(ARCHIVO_USUARIOS);

    private final JFrame frame = new JFrame("Login");
    private final JTextField txtUsername = new JTextField(15);
    private final JPasswordField txtPassword = new JPasswordField(15);
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnRegistro = new JButton("Registrarse");

    public VentanaLogin() {
        // Asegurar usuario inicial si es la primera ejecución
        if (USUARIOS.isEmpty()) {
            USUARIOS.add(new Usuario("admin", "123", "Administrador", 5000.0));
            guardarUsuarios();
        }
        configurarComponentes();
        agregarListeners();
    }

    public static void guardarUsuarios() {
        GestorPersistencia.guardarDatos(USUARIOS, ARCHIVO_USUARIOS);
    }

    private void configurarComponentes() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtPassword);

        panel.add(btnRegistro);
        panel.add(btnLogin);

        frame.add(panel);
        frame.pack();

        // Listener para guardar datos al cerrar la aplicación
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                guardarUsuarios();
                System.exit(0);
            }
        });
    }

    private void agregarListeners() {
        btnLogin.addActionListener(e -> intentarLogin());
        btnRegistro.addActionListener(e -> mostrarVentanaRegistro());
    }

    public void mostrarVentana() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void intentarLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "El usuario y la contraseña no pueden estar vacios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario usuario = SessionController.getInstancia().autenticarYIniciarSesion(username, password);

            JOptionPane.showMessageDialog(frame, "Login exitoso. ¡Bienvenido!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            new VentanaMenu(usuario.getNombre()).mostrarVentana();

        } catch (IllegalStateException e){
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Error de login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarVentanaRegistro() {
        frame.dispose();
        new VentanaRegistro().mostrarVentana();
    }
}