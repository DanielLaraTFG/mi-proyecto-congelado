package principal;

public class Usuario {

    private String correo;

    private String nombreUsuario;

    private String contrasegna;

    public Usuario(String correo, String nombreUsuario, String contrasegna) {
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
        this.contrasegna = contrasegna;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasegna() {
        return contrasegna;
    }

    public void setContrasegna(String contrasegna) {
        this.contrasegna = contrasegna;
    }
}
