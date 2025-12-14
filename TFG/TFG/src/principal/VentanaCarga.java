package principal;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class VentanaCarga extends JFrame {

    private String usuario;

    private int[][] estrellas;

    private Cometa[] cometas;

    // Constructor de la ventana de carga
    public VentanaCarga(String usuario) {
        this.usuario = usuario;

        setUndecorated(true);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generarEstrellas();
        generarCometas();

        // Panel con fondo estrellado y cometas
        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Fondo negro
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                // Dibujar estrellas
                g.setColor(Color.WHITE);
                for (int i = 0; i < estrellas.length; i++) {
                    g.fillOval(
                            estrellas[i][0],
                            estrellas[i][1],
                            estrellas[i][2],
                            estrellas[i][2]);
                }

                // Dibujar cometas
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(150, 180, 255));

                for (Cometa c : cometas) {
                    g2.setStroke(new BasicStroke(2));
                    g2.drawLine(
                            c.x,
                            c.y,
                            c.x + c.longitud,
                            c.y - c.longitud / 2);
                }
            }
        };

        fondo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        // Spinner de carga
        SpinnerLoading spinner = new SpinnerLoading(80);
        gbc.gridy = 0;
        fondo.add(spinner, gbc);

        JLabel mensajeCarga = new JLabel(mensajeAleatorio());
        mensajeCarga.setFont(new Font("Arial", Font.PLAIN, 20));
        mensajeCarga.setForeground(Color.WHITE);
        gbc.gridy = 1;
        gbc.insets = new Insets(100, 0, 0, 0);
        fondo.add(mensajeCarga, gbc);

        setContentPane(fondo);
        spinner.start();

        // Animacion de cometas
        Timer animCometas = new Timer(30, e -> {
            moverCometas();
            fondo.repaint();
        });
        animCometas.start();

        // Temporizador para abrir la ventana principal
        Timer timer = new Timer(5000, e -> {
            spinner.stop();
            dispose();
            new VentanaPrincipal(usuario).setVisible(true);
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Genera las estrellas del fondo
    private void generarEstrellas() {
        estrellas = new int[200][3];
        Random random = new Random();

        for (int i = 0; i < estrellas.length; i++) {
            estrellas[i][0] = random.nextInt(1280);
            estrellas[i][1] = random.nextInt(720);
            estrellas[i][2] = random.nextInt(3) + 1;
        }
    }

    // Genera los cometas iniciales.
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

    // Mueve los cometas y los reinicia cuando salen de la pantalla.
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

    // Devuelve un mensaje de carga aleatorio.
    private String mensajeAleatorio() {
        String[] mensajes = {
                "Cargando particulas estelares...",
                "Calculando la trayectoria del universo...",
                "Reajustando la gravedad local...",
                "Sincronizando con servidores interdimensionales...",
                "Recalibrando el flujo del espacio-tiempo...",
                "Reparando bugs antes de que los encuentres...",
                "Renderizando estrellas adicionales para tu experiencia..."
        };

        Random random = new Random();
        return mensajes[random.nextInt(mensajes.length)];
    }

    // Componente grafico que muestra un spinner animado
    class SpinnerLoading extends JComponent {

        private int angle = 0;
        private Timer timer;
        private int size;

        public SpinnerLoading(int size) {
            this.size = size;
            setPreferredSize(new Dimension(size, size));

            timer = new Timer(20, e -> {
                angle += 5;
                repaint();
            });
        }

        public void start() {
            timer.start();
        }

        public void stop() {
            timer.stop();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int stroke = 8;
            g2.setStroke(new BasicStroke(
                    stroke,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
            g2.setColor(Color.WHITE);

            int diameter = size - stroke;
            int x = stroke / 2;
            int y = stroke / 2;

            g2.drawArc(x, y, diameter, diameter, angle, 300);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaCarga("").setVisible(true));
    }
}
