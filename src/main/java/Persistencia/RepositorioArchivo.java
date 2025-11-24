package Persistencia;

import Interfaces.IRepositorioResultados;
import Modelo.Resultado;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioArchivo implements IRepositorioResultados {

    private static final String NOMBRE_ARCHIVO = "historial_global.csv";
    private static final String SEPARADOR = ",";

    public RepositorioArchivo() {
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
            } catch (IOException e) {
                System.err.println("Error al crear el archivo CSV: " + e.getMessage());
            }
        }
    }

    @Override
    public void agregarResultado(Resultado nuevoResultado) {
        List<Resultado> historialActual;
        try {
            historialActual = obtenerHistorialGlobal();
        } catch (RuntimeException e) {
            throw e;
        }

        historialActual.add(nuevoResultado);

        try (PrintWriter writer = new PrintWriter(new FileWriter(NOMBRE_ARCHIVO, false))) {
            for  (Resultado resultado : historialActual) {
                String linea = resultado.getNumero() + SEPARADOR +
                               resultado.getMonto() + SEPARADOR +
                               resultado.isAcierto() + SEPARADOR +
                               resultado.getTipoApuesta() + SEPARADOR +
                               resultado.getGanancia();
                writer.println(linea);
            }
            System.out.println("Archivo agregado correctamente en" + NOMBRE_ARCHIVO);
        } catch (IOException e) {
            throw new RuntimeException("Error critico al escribir en el archivo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Resultado> obtenerHistorialGlobal() {
        List<Resultado> historial = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(NOMBRE_ARCHIVO))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split(SEPARADOR);
                if (partes.length == 5) {
                    try {
                        int numero = Integer.parseInt(partes[0].trim());
                        double monto = Double.parseDouble(partes[1].trim());
                        boolean acierto = Boolean.parseBoolean(partes[2].trim());
                        char tipoApuesta = partes[3].trim().charAt(0);
                        double ganancia = Double.parseDouble(partes[4].trim());

                        historial.add(new Resultado(numero, monto, acierto, tipoApuesta, ganancia));
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.err.println("Línea de historial inválida: " + linea);
                    }
                }
            }
            return historial;

        } catch (FileNotFoundException e) {
            System.out.println(" Archivo " + NOMBRE_ARCHIVO + " no encontrado. Iniciando historial vacío.");
            return historial;
        } catch (IOException e) {
            throw new RuntimeException("Error crítico al leer el archivo CSV. " + e.getMessage(), e);
        }
    }
}