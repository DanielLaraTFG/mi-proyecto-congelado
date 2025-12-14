package principal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccesoUsuario {

    // Comprueba si existe un usuario con el nombre indicado.
    public static String consultarUsuario(String texto) throws SQLException, ClassNotFoundException {
        String nombreUsuario = "";

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sentenciaSql = "SELECT nombreusuario FROM usuarios WHERE nombreusuario = ?";

            try (PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {

                sentencia.setString(1, texto);

                ResultSet resultados = sentencia.executeQuery();

                while (resultados.next()) {
                    nombreUsuario = resultados.getString("nombreusuario");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nombreUsuario;
    }

    // Obtiene la contraseña de un usuario.
    public static String consultarContrasegna(String nombreUsuario)
            throws SQLException, ClassNotFoundException {

        String contrasena = "";

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sentenciaSql = "SELECT contrasegna FROM usuarios WHERE nombreusuario = ?";

            try (PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {
                sentencia.setString(1, nombreUsuario);
                ResultSet resultados = sentencia.executeQuery();

                while (resultados.next()) {
                    contrasena = resultados.getString("contrasegna");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return contrasena;
    }

    // Comprueba si un correo existe en la base de datos.
    public static String consultarCorreo(String correo) throws SQLException, ClassNotFoundException {
        String correoBd = "";

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sentenciaSql = "SELECT correo FROM usuarios WHERE correo = ?";

            try (PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {
                sentencia.setString(1, correo);
                ResultSet resultados = sentencia.executeQuery();

                while (resultados.next()) {
                    correoBd = resultados.getString("correo");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return correoBd;
    }

    // Inserta un nuevo usuario en la base de datos.
    public static int insertarUsuario(Usuario usuario) throws SQLException, ClassNotFoundException {
        int filasInsertadas = 0;

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sentenciaSql = "INSERT INTO usuarios (correo, nombreusuario, contrasegna) VALUES (?, ?, ?)";

            try (PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {

                sentencia.setString(1, usuario.getCorreo());
                sentencia.setString(2, usuario.getNombreUsuario());
                sentencia.setString(3, usuario.getContrasegna());

                filasInsertadas = sentencia.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filasInsertadas;
    }

    // Obtiene la puntuación principal del usuario.
    public static int consultarPuntuacion(String nombreUsuario)
            throws SQLException, ClassNotFoundException {

        int puntuacion = 0;

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sentenciaSql = "SELECT puntuacion FROM usuarios WHERE nombreusuario = ?";

            try (PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {
                sentencia.setString(1, nombreUsuario);
                ResultSet resultados = sentencia.executeQuery();

                while (resultados.next()) {
                    puntuacion = resultados.getInt("puntuacion");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return puntuacion;
    }

    // Actualiza la puntuación principal del usuario.
    public static boolean actualizarPuntuacion(String nombreUsuario, int nuevaPuntuacion)
            throws SQLException, ClassNotFoundException {

        boolean actualizado = false;

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sentenciaSql = "UPDATE usuarios SET puntuacion = ? WHERE nombreusuario = ?";

            try (PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {
                sentencia.setInt(1, nuevaPuntuacion);
                sentencia.setString(2, nombreUsuario);

                int filasAfectadas = sentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    actualizado = true;
                }
            }
        }

        return actualizado;
    }

    // Obtiene la segunda puntuación del usuario.
    public static int consultarPuntuacion2(String nombreUsuario)
            throws SQLException, ClassNotFoundException {

        int puntuacion = 0;

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sentenciaSql = "SELECT puntuacion2 FROM usuarios WHERE nombreusuario = ?";

            try (PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {
                sentencia.setString(1, nombreUsuario);
                ResultSet resultados = sentencia.executeQuery();

                while (resultados.next()) {
                    puntuacion = resultados.getInt("puntuacion2");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return puntuacion;
    }

    // Actualiza la segunda puntuación del usuario.
    public static boolean actualizarPuntuacion2(String nombreUsuario, int nuevaPuntuacion)
            throws SQLException, ClassNotFoundException {

        boolean actualizado = false;

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sentenciaSql = "UPDATE usuarios SET puntuacion2 = ? WHERE nombreusuario = ?";

            try (PreparedStatement sentencia = conexion.prepareStatement(sentenciaSql)) {
                sentencia.setInt(1, nuevaPuntuacion);
                sentencia.setString(2, nombreUsuario);

                int filasAfectadas = sentencia.executeUpdate();
                if (filasAfectadas > 0) {
                    actualizado = true;
                }
            }
        }

        return actualizado;
    }

    public static boolean actualizarContrasena(String nombreUsuario, String nuevaContrasena)
            throws SQLException, ClassNotFoundException {

        // Se obtiene la contraseña actual
        String contrasenaActual = consultarContrasegna(nombreUsuario);

        // Si es la misma, no se actualiza
        if (contrasenaActual.equals(nuevaContrasena)) {
            return false;
        }

        boolean actualizado = false;

        try (Connection conexion = SupabaseConnection.abrirConexion()) {

            String sql = "UPDATE usuarios SET contrasegna = ? WHERE nombreusuario = ?";

            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setString(1, nuevaContrasena);
                ps.setString(2, nombreUsuario);

                int filas = ps.executeUpdate();
                if (filas > 0)
                    actualizado = true;
            }
        }

        return actualizado;
    }
}
