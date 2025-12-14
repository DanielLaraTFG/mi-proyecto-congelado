package principal;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Random;

public class VentanaJuego3 {

    private JPanel panelJuego;

    private Random random = new Random();

    private int puntuacion = 0;

    private JLabel marcador;
    private JLabel record;
    private JLabel contador;

    private int tiempoRestante = 60;

    private int ancho;
    private int alto;

    private int tamagnoSprite = 80;
    private int tamagnoArea = 600;

    private int offsetX;
    private int offsetY;

    // Constructor del juego
    public VentanaJuego3(int ancho, int alto, VentanaPrincipal principal, String usuario) {

        this.ancho = ancho;
        this.alto = alto;

        this.offsetX = (ancho - tamagnoArea) / 2;
        this.offsetY = (alto - tamagnoArea) / 2;

        // Panel con fondo estrellado
        panelJuego = new JPanel() {

            private final int[][] estrellas = new int[100][3];

            {
                for (int i = 0; i < estrellas.length; i++) {
                    estrellas[i][0] = random.nextInt(ancho);
                    estrellas[i][1] = random.nextInt(alto);
                    estrellas[i][2] = random.nextInt(3) + 1;
                }
            }

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
            }
        };

        panelJuego.setLayout(null);
        panelJuego.setBounds(0, 0, ancho, alto);

        // Cursor personalizado
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image imagenCursor = toolkit.getImage(getClass().getResource("/imagenes/mirilla.png"));
        Point hotspot = new Point(
                imagenCursor.getWidth(null) / 2,
                imagenCursor.getHeight(null) / 2);
        Cursor cursorPersonalizado = toolkit.createCustomCursor(imagenCursor, hotspot, "CursorMirilla");
        panelJuego.setCursor(cursorPersonalizado);

        // Marcador
        marcador = new JLabel("Puntuacion: " + puntuacion);
        marcador.setFont(new Font("Arial", Font.PLAIN, 20));
        marcador.setForeground(Color.WHITE);
        marcador.setBounds(50, 30, 300, 30);
        panelJuego.add(marcador);

        // Record personal
        int puntuacionRecord = 0;
        try {
            puntuacionRecord = AccesoUsuario.consultarPuntuacion2(usuario);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        record = new JLabel("Record personal: " + puntuacionRecord);
        record.setFont(new Font("Arial", Font.PLAIN, 20));
        record.setForeground(Color.WHITE);
        record.setBounds(50, 110, 300, 30);
        panelJuego.add(record);

        // Contador de tiempo
        contador = new JLabel("Tiempo: 60");
        contador.setFont(new Font("Arial", Font.PLAIN, 20));
        contador.setForeground(Color.WHITE);
        contador.setBounds(50, 70, 300, 30);
        panelJuego.add(contador);

        // Primer boton
        crearBoton();

        // Timer principal del juego
        Timer timerJuego = new Timer(1000, e -> {

            tiempoRestante--;
            contador.setText("Tiempo: " + tiempoRestante);

            if (tiempoRestante <= 0) {

                ((Timer) e.getSource()).stop();

                JOptionPane.showMessageDialog(
                        panelJuego,
                        "¡Tiempo agotado!\nPuntuacion final: " + puntuacion);

                try {
                    int puntuacionAntigua = AccesoUsuario.consultarPuntuacion2(usuario);

                    if (puntuacionAntigua < puntuacion) {
                        AccesoUsuario.actualizarPuntuacion2(usuario, puntuacion);
                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    ex.printStackTrace();
                }

                principal.volverAlMenuPrincipal();
            }
        });

        timerJuego.start();
    }

    // Devuelve el panel del juego.
    public JPanel getPanel() {
        return panelJuego;
    }

    // Crea un nuevo boton animado.
    private void crearBoton() {

        int x = offsetX + random.nextInt(tamagnoArea - tamagnoSprite);
        int y = offsetY + random.nextInt(tamagnoArea - tamagnoSprite);

        BotonAnimado boton = new BotonAnimado(
                "/imagenes/sprite_",
                tamagnoSprite,
                panelJuego,
                random,
                offsetX,
                offsetY,
                tamagnoArea);

        boton.setBounds(x, y, tamagnoSprite, tamagnoSprite);
        panelJuego.add(boton);

        boton.addActionListener(new java.awt.event.ActionListener() {

            private boolean pulsado = false;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                if (pulsado) {
                    return;
                }

                pulsado = true;

                puntuacion++;
                marcador.setText("Puntuacion: " + puntuacion);

                int bonus = 0;

                if (puntuacion % 100 == 0) {
                    bonus = 15;
                    tiempoRestante += 15;
                } else if (puntuacion % 10 == 0) {
                    bonus = 5;
                    tiempoRestante += 5;
                }

                contador.setText("Tiempo: " + tiempoRestante);

                if (bonus > 0) {
                    mostrarTextoFlotante(
                            "+" + bonus + "s",
                            boton.getX(),
                            boton.getY());
                }

                boton.animarYDurante(() -> crearBoton());
            }
        });
    }

    // Muestra un texto flotante de bonificacion.
    private void mostrarTextoFlotante(String texto, int x, int y) {

        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        lbl.setForeground(Color.YELLOW);
        lbl.setBounds(x, y, 100, 30);

        panelJuego.add(lbl);
        panelJuego.setComponentZOrder(lbl, 0);

        Timer timerTexto = new Timer(30, null);
        final int[] dy = { 0 };

        timerTexto.addActionListener(e -> {

            dy[0] -= 2;
            lbl.setLocation(lbl.getX(), lbl.getY() + dy[0]);

            if (dy[0] < -30) {
                panelJuego.remove(lbl);
                panelJuego.repaint();
                timerTexto.stop();
            }
        });

        timerTexto.start();
    }
}

// Boton animado reutilizable.
class BotonAnimado extends JButton {

    private ImageIcon[] frames;
    private int frameActual = 0;

    private JPanel panelJuego;
    private Random random;

    private int offsetX;
    private int offsetY;
    private int tamagnoArea;

    public BotonAnimado(
            String rutaBase,
            int tamagno,
            JPanel panelJuego,
            Random random,
            int offsetX,
            int offsetY,
            int tamagnoArea) {

        this.panelJuego = panelJuego;
        this.random = random;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.tamagnoArea = tamagnoArea;

        frames = new ImageIcon[4];

        for (int i = 1; i <= 4; i++) {
            java.net.URL url = getClass().getResource(rutaBase + i + ".png");
            if (url == null) {
                System.err.println(
                        "No se encontro la imagen: " + rutaBase + i + ".png");
                continue;
            }

            Image img = new ImageIcon(url)
                    .getImage()
                    .getScaledInstance(tamagno, tamagno, Image.SCALE_SMOOTH);

            frames[i - 1] = new ImageIcon(img);
        }

        setIcon(frames[0]);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    // Ejecuta la animacion y genera un nuevo boton durante ella.
    public void animarYDurante(Runnable generarNuevoBoton) {

        frameActual = 0;

        Timer animTimer = new Timer(50, null);
        animTimer.addActionListener(e -> {

            setIcon(frames[frameActual]);
            frameActual++;

            if (frameActual == 1) {
                generarNuevoBoton.run();
            }

            if (frameActual >= frames.length) {
                animTimer.stop();
                panelJuego.remove(this);
                panelJuego.revalidate();
                panelJuego.repaint();
            }
        });

        animTimer.start();
    }
}
