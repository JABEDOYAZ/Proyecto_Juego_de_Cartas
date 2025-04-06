import java.util.Random;

import javax.swing.JPanel;

public class Jugador {

    private int TOTAL_CARTAS = 10;
    private int MARGEN = 10;
    private int DISTANCIA = 40;

    private Carta[] cartas = new Carta[TOTAL_CARTAS];

    private Random r = new Random(); // la suerte del jugador

    public void repartir() {
        for (int i = 0; i < TOTAL_CARTAS; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        int posicion = MARGEN + (TOTAL_CARTAS - 1) * DISTANCIA;
        for (Carta carta : cartas) {
            carta.mostrar(pnl, posicion, MARGEN);
            posicion -= DISTANCIA;
        }
        pnl.repaint();
    }

    public String getGrupos() {
        String mensaje = "No se encontraron figuras";
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getNombre().ordinal()]++;
        }

        boolean hayGrupos = false;
        for (int contador : contadores) {
            if (contador > 1) {
                hayGrupos = true;
                break;
            }
        }

        if (hayGrupos) {
            mensaje = "Se encontraron los siguientes grupos:\n";
            int fila = 0;
            for (int contador : contadores) {
                if (contador > 1) {
                    mensaje += Grupo.values()[contador] + " de " + NombreCarta.values()[fila] + "\n";
                }
                fila++;
            }
        }

        return mensaje;
    }

    public String puntaje() {

        String mensaje = "";
        // Inicializar contadores y matrices
        int numeroDeCartas = NombreCarta.values().length; // desde as hasta rey (13)
        int numeroDePintas = Pinta.values().length; // 4 pintas
        int[] contadores = new int[numeroDeCartas];
        boolean[][] esParteDeEscalera = new boolean[numeroDePintas][numeroDeCartas];
        // boolean[] cartaContadaEnEscalera = new boolean[numeroDeCartas];

        // Crear una matriz para contar las cartas por pinta y valor
        int[][] matrizCartas = new int[numeroDePintas][numeroDeCartas];
        for (Carta c : cartas) {
            matrizCartas[c.getPinta().ordinal()][c.getNombre().ordinal()]++;
            contadores[c.getNombre().ordinal()]++;
        }

        // Identificar las cartas que forman parte de escaleras
        for (int pinta = 0; pinta < matrizCartas.length; pinta++) {
            int consecutivas = 0;
            for (int valor = 0; valor < matrizCartas[pinta].length; valor++) {
                if (matrizCartas[pinta][valor] > 0) {
                    consecutivas++;
                    if (consecutivas >= 3) {
                        for (int i = valor - consecutivas + 1; i <= valor; i++) {
                            esParteDeEscalera[pinta][i] = true;
                        }
                    }
                } else {
                    consecutivas = 0;
                }
            }
        }

        for (int pinta = 0; pinta < matrizCartas.length; pinta++) {
            for (int valor = 0; valor < matrizCartas[pinta].length; valor++) {
                if (esParteDeEscalera[pinta][valor] && matrizCartas[pinta][valor] > 1) {// Si la carta forma parte de  una escalera y hay más de una carta
                    matrizCartas[pinta][valor]--;
                    contadores[valor]--;
                } else if (esParteDeEscalera[pinta][valor]) { // Si la carta forma parte de una escalera
                    contadores[valor]--;
                }
            }
        }
        int puntos2 = 0; // Puntaje de las cartas que no forman grupos
        // Calcular el puntaje total
        for (int i = 0; i < contadores.length; i++) { // Para cada carta
            if (contadores[i] == 1) { // Si hay una carta suelta
                if (i == 0 || i >= 10) { // Si es un as o si es 10, jack, queen o king valen 10 puntos
                    puntos2 += 10; 
                } else { // Si es un 2, 3, 4, 5, 6, 7, 8 o 9
                    puntos2 += i + 1; 
                }
            }

        }
        if (puntos2 == 0) { 
            mensaje = "Tiene un puntaje de 0";
        } else {
            mensaje = "Tiene un puntaje de " + puntos2;
        }

        return mensaje;
    }

    public String getEscaleras() {
        StringBuilder mensaje = new StringBuilder("");
        boolean hayEscaleras = false;

        // Crear una matriz para contar las cartas por pinta y valor
        int[][] contadores = new int[Pinta.values().length][NombreCarta.values().length];
        for (Carta c : cartas) {
            contadores[c.getPinta().ordinal()][c.getNombre().ordinal()]++;
        }

        // Verificar escaleras por cada pinta
        for (int pinta = 0; pinta < contadores.length; pinta++) {
            int consecutivas = 0;
            for (int valor = 0; valor < contadores[pinta].length; valor++) {
                if (contadores[pinta][valor] > 0) {
                    consecutivas++;
                    if (consecutivas >= 3) {
                        hayEscaleras = true;
                    }
                } else {
                    if (consecutivas >= 3) {
                        mensaje.append("\nEscalera de ").append(Pinta.values()[pinta])
                                .append(" desde ").append(NombreCarta.values()[valor - consecutivas])
                                .append(" hasta ").append(NombreCarta.values()[valor - 1]);
                    }
                    consecutivas = 0;
                }
            }
            // Si la escalera llega al final
            if (consecutivas >= 3) {
                mensaje.append("\nEscalera de ").append(Pinta.values()[pinta])
                        .append(" desde ").append(NombreCarta.values()[contadores[pinta].length - consecutivas])
                        .append(" hasta ").append(NombreCarta.values()[contadores[pinta].length - 1]);
            }
        }

        return hayEscaleras ? mensaje.toString() : "No se encontraron escaleras.";
    }

}
