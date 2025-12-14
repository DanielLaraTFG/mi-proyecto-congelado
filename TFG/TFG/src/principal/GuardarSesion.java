package principal;

import java.util.prefs.Preferences;

public class GuardarSesion {

    // Nodo de preferencias asociado a esta clase
    private static final Preferences prefs = Preferences.userNodeForPackage(GuardarSesion.class);

    // Clave utilizada para guardar el usuario activo
    private static final String KEY_USUARIO = "usuario_activo";

    // Guarda el nombre del usuario que ha iniciado sesión.
    public static void guardarUsuarioActivo(String usuario) {
        prefs.put(KEY_USUARIO, usuario);
    }

    // Obtiene el usuario que tiene la sesión activa.
    public static String obtenerUsuarioActivo() {
        return prefs.get(KEY_USUARIO, null);
    }

    // Elimina la sesión activa del usuario.
    public static void borrarUsuarioActivo() {
        prefs.remove(KEY_USUARIO);
    }
}
