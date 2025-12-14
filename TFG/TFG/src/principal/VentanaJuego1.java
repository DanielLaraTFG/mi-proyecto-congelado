package principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class VentanaJuego1 {

    private JPanel panelJuego;

    private Random random = new Random();

    private int puntuacion = 0;

    private JLabel marcador;

    private String usuario;

    private VentanaPrincipal ventanaPrincipal;

    private int ancho;
    private int alto;

    private int tamagnoMarciano = 80;

    private int tamagnoArea = 600;
    private int offsetX;
    private int offsetY;

    public VentanaJuego1(int ancho, int alto, VentanaPrincipal ventanaPrincipal, String usuario) {

        this.usuario = usuario;
        this.ventanaPrincipal = ventanaPrincipal;
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
                    g.fillOval(estrellas[i][0], estrellas[i][1], estrellas[i][2], estrellas[i][2]);
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
        marcador = new JLabel("Puntuación: " + puntuacion + " | Jugador: " + usuario);
        marcador.setFont(new Font("Arial", Font.PLAIN, 20));
        marcador.setForeground(Color.WHITE);
        marcador.setBounds(50, 30, 400, 30);
        panelJuego.add(marcador);

        // Botón menú principal
        JButton botonMenu = new JButton("Menu Principal");
        botonMenu.setBounds(ancho - 180, 30, 150, 30);
        botonMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelJuego.add(botonMenu);

        botonMenu.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    panelJuego,
                    "¿Deseas salir al menú principal?",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                ventanaPrincipal.volverAlMenuPrincipal();
            }
        });

        // Crear primer marciano
        crearMarciano();
    }

    // Devuelve el panel del juego.
    public JPanel getPanel() {
        return panelJuego;
    }

    // Actualiza la puntuación.
    private void actualizarPuntuacion(int puntosGanados) {
        puntuacion += puntosGanados;
        marcador.setText("Puntuación: " + puntuacion + " | Jugador: " + usuario);
    }

    // Crea un nuevo marciano en una posición aleatoria.
    private void crearMarciano() {

        int x = offsetX + random.nextInt(tamagnoArea - tamagnoMarciano);
        int y = offsetY + random.nextInt(tamagnoArea - tamagnoMarciano);

        BotonAnimado marciano = new BotonAnimado(
                "/imagenes/sprite_",
                tamagnoMarciano,
                panelJuego,
                random,
                offsetX,
                offsetY,
                tamagnoArea);

        marciano.setBounds(x, y, tamagnoMarciano, tamagnoMarciano);
        panelJuego.add(marciano);

        marciano.addActionListener(new java.awt.event.ActionListener() {

            private boolean pulsado = false;

            @Override
            public void actionPerformed(ActionEvent e) {

                if (pulsado) {
                    return;
                }

                pulsado = true;
                actualizarPuntuacion(10);

                marciano.animarYDurante(() -> crearMarciano());
            }
        });

        panelJuego.revalidate();
        panelJuego.repaint();
    }
}

// Botón animado usado para los marcianos.
class BotonAnimado extends JButton {

    private ImageIcon[] frames;
    private int frameActual = 0;

    private JPanel panelJuego;
    private Random random;

    private int offsetX;
    private int offsetY;
    private int tamagnoArea;

    // Constructor del botón animado.
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
                System.err.println("No se encontró la imagen: " + rutaBase + i + ".png");
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

    // Reproduce la animación y genera un nuevo botón durante la animación.
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
