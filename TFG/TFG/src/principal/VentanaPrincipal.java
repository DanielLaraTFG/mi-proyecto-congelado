package principal;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class VentanaPrincipal extends JFrame {

    private String usuario;

    private JLabel titulo;
    private FondoEstrellado fondoAplicacion;

    private JButton botonIniciarPartida;
    private JButton botonIniciarPartida2;
    private JButton botonIniciarPartida3;
    private JButton botonSalir;

    private ImageIcon cruzSalir = new ImageIcon(getClass().getResource("/imagenes/cruz_salir.png"));

    public VentanaPrincipal(String usuario) {

        this.usuario = usuario;

        setSize(1280, 720);
        setUndecorated(true);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        inicializarComponentes();
        funcionamientoBotones();
    }

    // Inicializa todos los componentes visuales.
    private void inicializarComponentes() {

        fondoAplicacion = new FondoEstrellado();
        fondoAplicacion.setBounds(0, 0, getWidth(), getHeight());
        add(fondoAplicacion);

        titulo = new JLabel("Bienvenid@ " + usuario, SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(0, 80, getWidth(), 50);
        fondoAplicacion.add(titulo);

        ImageIcon img1 = new ImageIcon(getClass().getResource("/imagenes/modo_libre.png"));

        botonIniciarPartida = new JButton(new ImageIcon(img1.getImage()));

        botonIniciarPartida.setBounds(180, 215, 200, 250);
        botonIniciarPartida.setContentAreaFilled(false);
        botonIniciarPartida.setBorderPainted(false);
        botonIniciarPartida.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fondoAplicacion.add(botonIniciarPartida);

        JLabel texto1 = new JLabel(
                "<html>Aqui puedes pasar el rato<br>" +
                        "matando marcianos de manera<br>" +
                        "infinita sin preocuparte de nada</html>",
                SwingConstants.CENTER);
        texto1.setForeground(Color.WHITE);
        texto1.setBounds(180, 520, 200, 100);
        fondoAplicacion.add(texto1);

        ImageIcon img2 = new ImageIcon(getClass().getResource("/imagenes/modo_batalla.png"));

        botonIniciarPartida2 = new JButton(new ImageIcon(img2.getImage()));

        botonIniciarPartida2.setBounds(540, 215, 200, 250);
        botonIniciarPartida2.setContentAreaFilled(false);
        botonIniciarPartida2.setBorderPainted(false);
        botonIniciarPartida2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fondoAplicacion.add(botonIniciarPartida2);

        JLabel texto2 = new JLabel(
                "<html>Si quieres un buen desafio<br>" +
                        "esta es tu mejor opcion,<br>" +
                        "derrota marcianos y jefes<br>" +
                        "para conseguir la mayor<br>" +
                        "puntuacion posible</html>",
                SwingConstants.CENTER);
        texto2.setForeground(Color.WHITE);
        texto2.setBounds(540, 520, 200, 100);
        fondoAplicacion.add(texto2);

        ImageIcon img3 = new ImageIcon(getClass().getResource("/imagenes/modo_contrareloj.png"));

        botonIniciarPartida3 = new JButton(new ImageIcon(img3.getImage()));

        botonIniciarPartida3.setBounds(900, 215, 200, 250);
        botonIniciarPartida3.setContentAreaFilled(false);
        botonIniciarPartida3.setBorderPainted(false);
        botonIniciarPartida3.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fondoAplicacion.add(botonIniciarPartida3);

        JLabel texto3 = new JLabel(
                "<html>Juega contra el tiempo y trata<br>" +
                        "de conseguir la mayor puntuacion<br>" +
                        "antes de que se acabe</html>",
                SwingConstants.CENTER);
        texto3.setForeground(Color.WHITE);
        texto3.setBounds(900, 520, 200, 100);
        fondoAplicacion.add(texto3);

        botonSalir = new JButton(cruzSalir);
        botonSalir.setBounds(1250, 10, 20, 20);
        botonSalir.setContentAreaFilled(false);
        botonSalir.setFocusPainted(false);
        botonSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        fondoAplicacion.add(botonSalir);
    }

    // Define el comportamiento de los botones.
    private void funcionamientoBotones() {

        botonIniciarPartida.addActionListener(e -> {
            getContentPane().removeAll();
            VentanaJuego1 juego = new VentanaJuego1(getWidth(), getHeight(), this, usuario);
            getContentPane().add(juego.getPanel());
            revalidate();
            repaint();
        });

        botonIniciarPartida2.addActionListener(e -> {
            getContentPane().removeAll();
            VentanaJuego2 juego = new VentanaJuego2(getWidth(), getHeight(), this, usuario);
            getContentPane().add(juego.getPanel());
            revalidate();
            repaint();
        });

        botonIniciarPartida3.addActionListener(e -> {
            getContentPane().removeAll();
            VentanaJuego3 juego = new VentanaJuego3(getWidth(), getHeight(), this, usuario);
            getContentPane().add(juego.getPanel());
            revalidate();
            repaint();
        });

        botonSalir.addActionListener(e -> {

            String[] opciones = { "Salir", "Cerrar sesion" };

            int eleccion = JOptionPane.showOptionDialog(
                    this,
                    "¿Que deseas hacer?",
                    "Salir",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            if (eleccion == 0) {
                System.exit(0);
            } else if (eleccion == 1) {
                GuardarSesion.borrarUsuarioActivo();
                System.exit(0);
            }
        });
    }

    // Fondo animado con estrellas y cometas.
    class FondoEstrellado extends JPanel {

        private int[][] estrellas;
        private Cometa[] cometas;
        private Random random = new Random();

        public FondoEstrellado() {

            setLayout(null);

            generarEstrellas();
            generarCometas();

            Timer timer = new Timer(30, e -> moverCometas());
            timer.start();
        }

        private void generarEstrellas() {

            estrellas = new int[200][3];

            for (int i = 0; i < estrellas.length; i++) {
                estrellas[i][0] = random.nextInt(1280);
                estrellas[i][1] = random.nextInt(720);
                estrellas[i][2] = random.nextInt(3) + 1;
            }
        }

        private void generarCometas() {

            cometas = new Cometa[4];

            for (int i = 0; i < cometas.length; i++) {
                cometas[i] = new Cometa(
                        random.nextInt(1280),
                        random.nextInt(720),
                        4 + random.nextInt(4),
                        80 + random.nextInt(40));
            }
        }

        private void moverCometas() {

            for (Cometa c : cometas) {

                c.x -= c.velocidad;
                c.y += c.velocidad / 2;

                if (c.x < -200 || c.y > 900) {
                    c.x = 1280 + random.nextInt(300);
                    c.y = random.nextInt(300);
                }
            }

            repaint();
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

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(200, 200, 255));

            for (Cometa c : cometas) {
                g2.drawLine(
                        c.x,
                        c.y,
                        c.x + c.longitud,
                        c.y - c.longitud / 2);
            }
        }

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
    }

    // Vuelve al menu principal desde cualquier juego.
    public void volverAlMenuPrincipal() {

        getContentPane().removeAll();
        inicializarComponentes();
        funcionamientoBotones();
        revalidate();
        repaint();
    }
}
