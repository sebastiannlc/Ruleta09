package Controlador;

import Modelo.ApuestaBase;
import Modelo.Resultado;
import Modelo.Ruleta;
import Modelo.Usuario;

public class RuletaController {

    private final Ruleta ruleta = new Ruleta();

    public Resultado jugarRonda(ApuestaBase apuesta) {

        Usuario usuario = SessionController.getInstancia().getUsuarioActual();
        if (usuario == null) {
            throw new IllegalStateException("No hay sesión de usuario activa.");
        }

        if (apuesta == null) {
            throw new IllegalArgumentException("Apuesta requerida.");
        }

        if (usuario.getSaldo() < apuesta.getMonto()) {
            throw new IllegalStateException("Saldo insuficiente.");
        }

        int numeroGanador = ruleta.girar();
        String colorGanador = Ruleta.getColor(numeroGanador);

        boolean acierto = apuesta.acierta(numeroGanador, colorGanador);
        double gananciaNeta;
        if (acierto) {
            // Ganancia = Apuesta * 2 - Apuesta inicial (es decir, Apuesta * 1)
            gananciaNeta = apuesta.getMonto();
        } else {
            // Pérdida = - Apuesta inicial
            gananciaNeta = -apuesta.getMonto();
        }

        usuario.actualizarSaldo(gananciaNeta);

        // Crear objeto resultado
        Resultado resultadoRonda = new Resultado(
                numeroGanador,
                apuesta.getMonto(),
                acierto,
                apuesta.getEtiqueta(),
                gananciaNeta
        );

        usuario.agregarResultado(resultadoRonda);

        ResultadoController.registrarResultado(resultadoRonda);

        return resultadoRonda;
    }

    public String generarLogRonda(Resultado resultado) {
        String resultadoTexto = resultado.isAcierto() ? "¡GANÓ!" : "PERDIÓ.";

        int numRonda = SessionController.getInstancia().getHistorialPersonal().size();

        String logEntry = String.format("Ronda #%d: Apuesta: %c | Salió: %d (%s). Neta: $%.2f\n",
                numRonda,
                resultado.getTipoApuesta(),
                resultado.getNumero(),
                resultadoTexto,
                resultado.getGanancia()
        );

        return logEntry;
    }
}