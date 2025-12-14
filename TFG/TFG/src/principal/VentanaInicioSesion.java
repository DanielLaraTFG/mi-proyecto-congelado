package principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.naming.directory.*;
import javax.naming.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.Random;

public class VentanaInicioSesion extends JFrame {

    // CAMPOS UI
    private JLabel titulo;
    private JLabel nombreUsuarioLabel;
    private JLabel contrasegnaLabel;
    private JTextField textoNombreUsuario;
    private JPasswordField textoContrasegna;
    private JButton botonMostrarContrasegna;
    private JButton botonContrasegnaOlvidada;
    private JButton botonRegistro;
    private JButton botonIniciarSesion;
    private JButton botonSalir;
    private JCheckBox botonSesionIniciada;

    private ImageIcon ojoAbierto = new ImageIcon(getClass().getResource("/imagenes/ojo_abierto.png"));
    private ImageIcon ojoCerrado = new ImageIcon(getClass().getResource("/imagenes/ojo_cerrado.png"));
    private ImageIcon cruzSalir = new ImageIcon(getClass().getResource("/imagenes/cruz_salir.png"));

    // FONDO ESTRELLADO
    private int[][] estrellas;
    private Cometa[] cometas;

    public VentanaInicioSesion() {
        setSize(1280, 720);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generarEstrellas();
        generarCometas();

        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.WHITE);
                for (int i = 0; i < estrellas.length; i++) {
                    g.fillOval(
                            estrellas[i][0],
                            estrellas[i][1],
                            estrellas[i][2],
                            estrellas[i][2]);
                }

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 180, 255));
                g2.setStroke(new BasicStroke(2));

                for (Cometa c : cometas) {
                    g2.drawLine(
                            c.x,
                            c.y,
                            c.x + c.longitud,
                            c.y - c.longitud / 2);
                }
            }
        };

        fondo.setLayout(null);
        setContentPane(fondo);

        Timer animCometas = new Timer(30, e -> {
            moverCometas();
            fondo.repaint();
        });
        animCometas.start();

        inicializarComponentes();
        cargarSesionGuardada();
        funcionamientoBotones();
    }

    // Genera las estrellas del fondo.
    private void generarEstrellas() {
        estrellas = new int[200][3];
        Random random = new Random();

        for (int i = 0; i < estrellas.length; i++) {
            estrellas[i][0] = random.nextInt(1280);
            estrellas[i][1] = random.nextInt(720);
            estrellas[i][2] = random.nextInt(3) + 1;
        }
    }

    // Genera los cometas del fondo.
    private void generarCometas() {
        Random random = new Random();
        cometas = new Cometa[4];

        for (int i = 0; i < cometas.length; i++) {
            cometas[i] = new Cometa(
                    random.nextInt(1280),
                    random.nextInt(720),
                    4 + random.nextInt(4),
                    70 + random.nextInt(60));
        }
    }

    // Mueve los cometas por la pantalla.
    private void moverCometas() {
        for (Cometa c : cometas) {
            c.x -= c.velocidad;
            c.y += c.velocidad / 2;

            if (c.x < -250 || c.y > 900) {
                Random random = new Random();
                c.x = 1280 + random.nextInt(300);
                c.y = random.nextInt(200);
            }
        }
    }

    // Clase interna que representa un cometa.
    class Cometa {
        int x;
        int y;
        int velocidad;
        int longitud;

        public Cometa(int x, int y, int velocidad, int longitud) {
            this.x = x;
            this.y = y;
            this.velocidad = velocidad;
            this.longitud = longitud;
        }
    }

    // Inicializa los componentes de la interfaz.
    private void inicializarComponentes() {

        titulo = new JLabel("CAZADOR CÓSMICO");
        titulo.setBounds((getWidth() - 600) / 2, 100, 600, 50);
        titulo.setFont(new Font("Arial", Font.BOLD, 60));
        titulo.setForeground(Color.WHITE);
        add(titulo);

        nombreUsuarioLabel = new JLabel("Nombre de Usuario");
        nombreUsuarioLabel.setBounds((getWidth() - 250) / 2, 250, 250, 25);
        nombreUsuarioLabel.setFont(new Font("Arial", Font.BOLD, 25));
        nombreUsuarioLabel.setForeground(Color.WHITE);
        add(nombreUsuarioLabel);

        textoNombreUsuario = new JTextField();
        textoNombreUsuario.setBounds((getWidth() - 190) / 2, 290, 190, 20);
        bloquearBeepCampoVacio(textoNombreUsuario);
        textoNombreUsuario.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (textoNombreUsuario.getText().length() >= 25)
                    e.consume();
            }
        });
        add(textoNombreUsuario);

        contrasegnaLabel = new JLabel("Contraseña");
        contrasegnaLabel.setBounds((getWidth() - 140) / 2, 350, 140, 25);
        contrasegnaLabel.setFont(new Font("Arial", Font.BOLD, 25));
        contrasegnaLabel.setForeground(Color.WHITE);
        add(contrasegnaLabel);

        textoContrasegna = new JPasswordField();
        textoContrasegna.setBounds((getWidth() - 190) / 2, 390, 190, 20);
        bloquearBeepCampoVacio(textoContrasegna);
        add(textoContrasegna);

        botonMostrarContrasegna = new JButton(ojoCerrado);
        botonMostrarContrasegna.setBounds(750, 390, 20, 20);
        botonMostrarContrasegna.setBorderPainted(false);
        botonMostrarContrasegna.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(botonMostrarContrasegna);

        botonContrasegnaOlvidada = new JButton("¿Has olvidado tu contraseña?");
        botonContrasegnaOlvidada.setBounds((getWidth() - 210) / 2, 410, 210, 25);
        botonContrasegnaOlvidada.setBorderPainted(false);
        botonContrasegnaOlvidada.setContentAreaFilled(false);
        botonContrasegnaOlvidada.setFocusPainted(false);
        botonContrasegnaOlvidada.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonContrasegnaOlvidada.setForeground(Color.WHITE);
        add(botonContrasegnaOlvidada);

        botonSesionIniciada = new JCheckBox("Mantener sesión iniciada");
        botonSesionIniciada.setBounds((getWidth() - 180) / 2, 440, 180, 40);
        botonSesionIniciada.setOpaque(false);
        botonSesionIniciada.setForeground(Color.WHITE);
        botonSesionIniciada.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(botonSesionIniciada);

        botonIniciarSesion = new JButton("INICIAR SESIÓN");
        botonIniciarSesion.setBounds((getWidth() - 450) / 2, 520, 200, 50);
        botonIniciarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(botonIniciarSesion);

        botonRegistro = new JButton("REGISTRAR USUARIO");
        botonRegistro.setBounds((getWidth() - 450) / 2 + 250, 520, 200, 50);
        botonRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(botonRegistro);

        botonSalir = new JButton(cruzSalir);
        botonSalir.setBounds(1250, 10, 20, 20);
        botonSalir.setContentAreaFilled(false);
        botonSalir.setFocusPainted(false);
        botonSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(botonSalir);
    }

    // Define el comportamiento de los botones.
    private void funcionamientoBotones() {

        botonMostrarContrasegna.addActionListener(e -> {
            if (textoContrasegna.getEchoChar() != '\0') {
                textoContrasegna.setEchoChar((char) 0);
                botonMostrarContrasegna.setIcon(ojoAbierto);
            } else {
                textoContrasegna.setEchoChar('•');
                botonMostrarContrasegna.setIcon(ojoCerrado);
            }
        });

        botonIniciarSesion.addActionListener(e -> {
            String usuarioTexto = textoNombreUsuario.getText().trim();
            String contrasegnaTexto = new String(textoContrasegna.getPassword()).trim();

            try {
                if (usuarioTexto.isEmpty() || contrasegnaTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Introduzca usuario y contraseña");
                    return;
                }

                String usuarioBd = AccesoUsuario.consultarUsuario(usuarioTexto);
                String contrasegnaBd = AccesoUsuario.consultarContrasegna(usuarioTexto);

                if (!usuarioTexto.equals(usuarioBd) || !contrasegnaTexto.equals(contrasegnaBd)) {
                    JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
                    return;
                }

                if (botonSesionIniciada.isSelected()) {
                    GuardarSesion.guardarUsuarioActivo(usuarioTexto);
                } else {
                    GuardarSesion.borrarUsuarioActivo();
                }

                dispose();
                new VentanaCarga(usuarioTexto).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        botonSalir.addActionListener(e -> System.exit(0));
        botonRegistro.addActionListener(e -> abrirRegistro());
        botonContrasegnaOlvidada.addActionListener(e -> abrirVentanaContrasenaOlvidada());
    }

    private void abrirRegistro() {
        JDialog ventanaRegistro = new JDialog(this, "Registro", true);
        ventanaRegistro.setUndecorated(true);
        ventanaRegistro.setSize(400, 300);
        ventanaRegistro.setLocationRelativeTo(this);
        ventanaRegistro.setLayout(null);

        JLabel correoLabel = new JLabel("Correo electrónico");
        correoLabel.setBounds(130, 20, 150, 20);
        ventanaRegistro.add(correoLabel);

        JTextField textoCorreo = new JTextField();
        textoCorreo.setBounds(100, 40, 200, 20);
        ventanaRegistro.add(textoCorreo);

        JLabel usuarioLabel = new JLabel("Nombre de usuario");
        usuarioLabel.setBounds(130, 70, 150, 20);
        ventanaRegistro.add(usuarioLabel);

        JTextField textoUsuario = new JTextField();
        textoUsuario.setBounds(100, 90, 200, 20);
        ventanaRegistro.add(textoUsuario);

        JLabel contrasegnaLabel = new JLabel("Contraseña");
        contrasegnaLabel.setBounds(150, 120, 100, 20);
        ventanaRegistro.add(contrasegnaLabel);

        JTextField textoContrasegna = new JTextField();
        textoContrasegna.setBounds(100, 140, 200, 20);
        ventanaRegistro.add(textoContrasegna);

        JButton botonRegistrar = new JButton("Registrar");
        botonRegistrar.setBounds(90, 200, 100, 30);
        botonRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ventanaRegistro.add(botonRegistrar);

        JButton botonSalirReg = new JButton("Salir");
        botonSalirReg.setBounds(210, 200, 100, 30);
        botonSalirReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ventanaRegistro.add(botonSalirReg);

        botonSalirReg.addActionListener(ev -> ventanaRegistro.dispose());

        botonRegistrar.addActionListener(ev -> {
            String correoTexto = textoCorreo.getText().trim();
            String usuarioTexto = textoUsuario.getText().trim();
            String contrasegnaTexto = textoContrasegna.getText().trim();

            try {
                if (correoTexto.isEmpty() || usuarioTexto.isEmpty() || contrasegnaTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Rellene todos los campos.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!ValidadorCorreo.correoValido(correoTexto)) {
                    JOptionPane.showMessageDialog(null, "El correo no es válido o no existe realmente.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (contrasegnaTexto.length() < 6) {
                    JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String usuarioBd = AccesoUsuario.consultarUsuario(usuarioTexto);
                String correoBd = AccesoUsuario.consultarCorreo(correoTexto);

                if (usuarioTexto.equals(usuarioBd)) {
                    JOptionPane.showMessageDialog(null, "Nombre de usuario ya en uso.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (correoTexto.equals(correoBd)) {
                    JOptionPane.showMessageDialog(null, "Correo electrónico ya registrado.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Usuario usuario = new Usuario(correoTexto, usuarioTexto, contrasegnaTexto);
                int resultado = AccesoUsuario.insertarUsuario(usuario);

                if (resultado == 0) {
                    JOptionPane.showMessageDialog(null, "No se pudo registrar el usuario.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario registrado correctamente.", "Registro exitoso",
                            JOptionPane.INFORMATION_MESSAGE);
                    ventanaRegistro.dispose();

                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            EnviarCorreo.enviarCorreo(
                                    correoTexto,
                                    "Registro exitoso",
                                    "Gracias por registrarte en mi aplicación.");
                            return null;
                        }
                    }.execute();
                }

            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }
        });

        ventanaRegistro.setVisible(true);
    }

    private void abrirVentanaContrasenaOlvidada() {
        JDialog ventanaOlvido = new JDialog(this, "Recuperar contraseña", true);
        ventanaOlvido.setUndecorated(true);
        ventanaOlvido.setSize(400, 260);
        ventanaOlvido.setLocationRelativeTo(this);
        ventanaOlvido.setLayout(null);

        JLabel usuarioLabel = new JLabel("Nombre de usuario");
        usuarioLabel.setBounds(100, 20, 150, 20);
        ventanaOlvido.add(usuarioLabel);

        JTextField textoUsuario = new JTextField();
        textoUsuario.setBounds(100, 40, 200, 20);
        ventanaOlvido.add(textoUsuario);

        JLabel nuevaContrasegnaLabel = new JLabel("Nueva contraseña");
        nuevaContrasegnaLabel.setBounds(100, 70, 150, 20);
        ventanaOlvido.add(nuevaContrasegnaLabel);

        JTextField textoNuevaContrasegna = new JTextField();
        textoNuevaContrasegna.setBounds(100, 90, 200, 20);
        ventanaOlvido.add(textoNuevaContrasegna);

        JLabel confirmarContrasegnaLabel = new JLabel("Confirmar contraseña");
        confirmarContrasegnaLabel.setBounds(100, 120, 150, 20);
        ventanaOlvido.add(confirmarContrasegnaLabel);

        JTextField textoConfirmarContrasegna = new JTextField();
        textoConfirmarContrasegna.setBounds(100, 140, 200, 20);
        ventanaOlvido.add(textoConfirmarContrasegna);

        JButton botonCambiarContrasegna = new JButton("Cambiar");
        botonCambiarContrasegna.setBounds(80, 190, 100, 30);
        botonCambiarContrasegna.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ventanaOlvido.add(botonCambiarContrasegna);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBounds(220, 190, 100, 30);
        botonSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        ventanaOlvido.add(botonSalir);

        botonSalir.addActionListener(e -> ventanaOlvido.dispose());

        botonCambiarContrasegna.addActionListener(e -> {
            String usuarioTexto = textoUsuario.getText().trim();
            String nueva = textoNuevaContrasegna.getText().trim();
            String confirmar = textoConfirmarContrasegna.getText().trim();

            try {
                String usuarioBd = AccesoUsuario.consultarUsuario(usuarioTexto);

                if (nueva.isEmpty() || confirmar.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Rellene todos los campos.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (usuarioBd.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El usuario no existe.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!nueva.equals(confirmar)) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (nueva.length() < 6) {
                    JOptionPane.showMessageDialog(null, "La contraseña debe tener al menos 6 caracteres.", "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                boolean actualizado = AccesoUsuario.actualizarContrasena(usuarioTexto, nueva);
                if (!actualizado) {
                    JOptionPane.showMessageDialog(null,
                            "La contraseña es igual a la actual, no se actualizará.",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(null,
                        "Contraseña cambiada correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                ventanaOlvido.dispose();

            } catch (SQLException | ClassNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error al conectar con la base de datos.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        ventanaOlvido.setVisible(true);
    }

    private void bloquearBeepCampoVacio(JTextField campo) {
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE ||
                        e.getKeyCode() == KeyEvent.VK_DELETE)
                        && campo.getText().isEmpty()) {
                    e.consume();
                }
            }
        });
    }

    private void cargarSesionGuardada() {
        String usuarioActivo = GuardarSesion.obtenerUsuarioActivo();
        if (usuarioActivo != null) {
            textoNombreUsuario.setText(usuarioActivo);
            botonSesionIniciada.setSelected(true);
        }
    }

    // Clase encargada de validar correos electronicos.
    public static class ValidadorCorreo {

        private static final java.util.regex.Pattern emailPattern = java.util.regex.Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
                java.util.regex.Pattern.CASE_INSENSITIVE);

        public static boolean correoValido(String correo) {
            if (!emailPattern.matcher(correo).matches())
                return false;

            String dominio = correo.substring(correo.indexOf("@") + 1);
            return dominioTieneMx(dominio);
        }

        private static boolean dominioTieneMx(String dominio) {
            try {
                Hashtable<String, String> env = new Hashtable<>();
                env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
                DirContext ictx = new InitialDirContext(env);
                Attributes attrs = ictx.getAttributes(dominio, new String[] { "MX" });
                Attribute attr = attrs.get("MX");
                return attr != null && attr.size() > 0;
            } catch (NamingException e) {
                return false;
            }
        }
    }

    // Clase encargada del envio de correos.
    public static class EnviarCorreo {

        public static void enviarCorreo(String destinatario, String asunto, String mensajeTexto) {
            String remitente = "tfgdaniellara@gmail.com";
            String clave = "kuda hchs ahsu abdn";

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remitente, clave);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(remitente));
                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(destinatario));
                message.setSubject(asunto);
                message.setText(mensajeTexto);
                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
