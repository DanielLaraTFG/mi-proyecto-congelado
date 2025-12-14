package principal;

import javax.swing.*;

public class Main {

    // Método principal de entrada a la aplicación.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String usuarioActivo = GuardarSesion.obtenerUsuarioActivo();
            if (usuarioActivo != null) {
                new VentanaCarga(usuarioActivo).setVisible(true);
            } else {
                new VentanaInicioSesion().setVisible(true);
            }
        });
    }

    /// Comprueba si existe conexión a Internet realizando una petición a un
    /// servidor externo.
    public static boolean hayConexion() {
        try {
            java.net.URL url = new java.net.URL("https://www.google.com/");

            java.net.HttpURLConnection conexion = (java.net.HttpURLConnection) url.openConnection();

            conexion.setRequestMethod("HEAD");
            conexion.setConnectTimeout(2000);

            conexion.connect();

            return conexion.getResponseCode() == 200;

        } catch (Exception e) {
            return false;
        }
    }
}
