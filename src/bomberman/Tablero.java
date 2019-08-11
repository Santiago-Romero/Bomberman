package bomberman;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author Santiago Romero
 */
public class Tablero extends JFrame implements Runnable {

    JLabel titulo;
    static JButton[][] botones;
    static int[][] tablero;
    static int[][] bombas;
    int tamx = 0;
    int tamy = 0;
    int tamb = 0;
    int tamc = 0;
    int tame = 0;
    private int blancox;
    private int blancoy;
    int barrasx[];
    int barrasy[];
    int bloqx[];
    int bloqy[];
    static ArrayList<Integer> enemiesx = new ArrayList<Integer>();;
    static ArrayList<Integer> enemiesy = new ArrayList<Integer>();;
    int resp;
    Font fuente;
    Container c;
    BufferedImage barraIcon;
    Thread h1;
    ImageIcon imgBarra;
    ImageIcon imgBloque;
    ImageIcon imgMeta;
    ImageIcon imgPlayer;
    ImageIcon imgEnemie;
    static ImageIcon imgVacio;
    static ImageIcon imgBomba;

    private final List<Integer> camino = new ArrayList<Integer>();
    static ArrayList<Integer> movimientosx = new ArrayList<Integer>();
    static ArrayList<Integer> movimientosy = new ArrayList<Integer>();
    static ArrayList<Integer> huirBombax = new ArrayList<Integer>();
    static ArrayList<Integer> huirBombay = new ArrayList<Integer>();
    private final ArrayList bombasTime = new ArrayList();
    static boolean win = false;
    int startX = 1, startY = 1;
    static int xBomba, yBomba;
    final static int V = 6;
    boolean movcondi = false;
    static boolean bombacondi = false;
    int antecaminox = 1;
    int antecaminoy = 1;
    static int contadorBomba = 0;
    private long initTime;
    Bomba clasebomba = new Bomba(bombasTime);

    public Tablero() {
        resp = JOptionPane.showConfirmDialog(null, "\nDesea leer el archivo en el directorio?"
                + "\nDe lo contrario se generará aleatorio\n\n\n", "BOMBERMAN", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (resp == 1) {
            TableroAleatorio ta = new TableroAleatorio();
        }
        leerInicio();
        barrasx = new int[tamb];
        barrasy = new int[tamb];
        bloqx = new int[tamc];
        bloqy = new int[tamc];
        //enemiesx = new int[tame];
        //enemiesy = new int[tame];
        leerBarras();
        crearInterfaz();
        escogerblanco();
        h1 = new Thread(this);
        h1.start();

    }

    public void leerInicio() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            if (resp == 1) {
                archivo = new File("nivel.txt");
            } else {
                archivo = new File("nivel base.txt");
            }
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea;
            int ix = 0;
            while ((linea = br.readLine()) != null) {
                tamx++;
                tamy = linea.length();
                for (int i = 0; i < linea.length(); i++) {
                    if (linea.substring(i, i + 1).equals("1")) {
                        tamb++;
                    }
                    if (linea.substring(i, i + 1).equals("2")) {
                        tamc++;
                    }
                    if (linea.substring(i, i + 1).equals("5")) {
                        tame++;
                    }
                }
                ix++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void escogerblanco() {
        int r = (int) (Math.random() * ((bloqx.length - 1) - 1 + 1) + 1);
        blancox = bloqx[r];
        blancoy = bloqy[r];
        JOptionPane.showMessageDialog(null, "Blanco en : " + blancox + "," + blancoy);
    }

    public void crearBotones() {
        int x = 45;//Variable incial de posicion de botones
        int y = 45;//Variable incial de posicion de botones
        for (int iy = 0; iy < tamy; iy++) {
            for (int ix = 0; ix < tamx; ix++) {
                botones[ix][iy] = new JButton(" ");
                botones[ix][iy].setBounds(x, y, 45, 45);
                c.add(botones[ix][iy]);
                y = y + 45;
                if (y >= 45 * tamx + 45) { //Se acomoda las posiciones para los botones
                    y = 45;
                    x += 45;
                }
            }
        }
    }

    public void printtablero() {
        System.out.println();
        for (int ix = 0; ix < tamx; ix++) {
            System.out.println();
            for (int iy = 0; iy < tamy; iy++) {
                System.out.print(tablero[ix][iy] + " ");
            }
        }
    }

    public void crearInterfaz() {
        imgBarra = new ImageIcon("barra.jpg");
        imgBloque = new ImageIcon("bloque.jpg");
        imgMeta = new ImageIcon("meta.jpg");
        imgPlayer = new ImageIcon("agente.jpg");
        imgEnemie = new ImageIcon("enemie.jpg");
        imgVacio = new ImageIcon("vacio.png");
        imgBomba = new ImageIcon("bomba.jpg");
        botones = new JButton[tamx][tamy];
        tablero = new int[tamx][tamy];
        bombas = new int[tamx][tamy];
        c = getContentPane();
        c.setLayout(null);
        fuente = new Font("Arial", Font.BOLD, 16);
        titulo = new JLabel("BOMBERMAN");
        titulo.setForeground(Color.WHITE);
        crearBotones();

        for (int l = 0; l < tamx; l++) {
            for (int k = 0; k < tamy; k++) {
                tablero[l][k] = 0;
                botones[l][k].setBorderPainted(false);
                botones[l][k].setIcon(imgVacio);
            }
        }
        botones[1][1].setIcon(imgPlayer);

        for (int j = 0; j < barrasx.length; j++) {
            tablero[barrasx[j]][barrasy[j]] = 1;
            botones[barrasx[j]][barrasy[j]].setBorderPainted(true);
            botones[barrasx[j]][barrasy[j]].setIcon(imgBarra);
        }
        for (int j = 0; j < bloqx.length; j++) {
            tablero[bloqx[j]][bloqy[j]] = 2;
            botones[bloqx[j]][bloqy[j]].setIcon(imgBloque);
        }

        for (int j = 0; j < enemiesx.size(); j++) {
            tablero[enemiesx.get(j)][enemiesy.get(j)] = 5;
            botones[enemiesx.get(j)][enemiesy.get(j)].setIcon(imgEnemie);
        }

        titulo.setFont(fuente);
        titulo.setBounds((tamy * 45) / 2, 0, 150, 50);
        c.add(titulo);
        c.setBackground(Color.black);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(tamy * 45 + 100, tamx * 45 + 120);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void leerBarras() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            if (resp == 1) {
                archivo = new File("nivel.txt");
            } else {
                archivo = new File("nivel base.txt");
            }
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            String linea;
            int ix = 0;
            int j1 = 0;
            int j2 = 0;
            int j3 = 0;
            while ((linea = br.readLine()) != null) {
                for (int i = 0; i < linea.length(); i++) {
                    if (linea.substring(i, i + 1).equals("1")) {
                        barrasx[j1] = ix;
                        barrasy[j1] = i;
                        j1++;
                    }
                    if (linea.substring(i, i + 1).equals("2")) {
                        bloqx[j2] = ix;
                        bloqy[j2] = i;
                        j2++;
                    }

                    if (linea.substring(i, i + 1).equals("5")) {
                        enemiesx.add(j3,ix);
                        enemiesy.add(j3,i);
                        j3++;
                    }
                }
                ix++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void movimientoagente() {
        for (int i = 0; i < tablero.length; i++) {
            for (int j = 0; j < tablero[i].length; j++) {
                if (tablero[i][j] == 6) {
                    tablero[i][j] = 0;
                }
            }
        }      
        Profundidad.buscarCamino(tablero, startY, startX, camino);
        for (int p = 0; p < camino.size(); p += 2) {
            int caminoY = camino.get(p);
            int caminoX = camino.get(p + 1);
            //METO LA RUTA EN UN ARRAY
            movimientosx.add(caminoX);
            movimientosy.add(caminoY);
            if (p >= 2 && p <= 6) {
                huirBombax.add(caminoX);
                huirBombay.add(caminoY);
            }
        }

        Collections.reverse(movimientosx);
        Collections.reverse(movimientosy);
        if (!win) {
            movimientosx.remove(movimientosx.size() - 1);
            movimientosy.remove(movimientosy.size() - 1);
        }
    }

    public void movimientoLejosBomba() {
        for (int i = 0; i < huirBombax.size(); i++) {
            movimientosx.add(huirBombax.get(i));
        }
        for (int i = 0; i < huirBombay.size(); i++) {
            movimientosy.add(huirBombay.get(i));
        }
    }

    public void moverenemigo(int x, int y, int antex, int antey) {
        botones[x][y].setIcon(imgEnemie);
        tablero[x][y] = 5;
        botones[antex][antey].setIcon(imgVacio);
        tablero[antex][antey] = 0;
    }

    public void run() {
        int cont = 0;
        initTime = System.currentTimeMillis();

        Thread ct = Thread.currentThread();
        while (ct == h1) {
            try {

                //BOMBA
                for (int k = 2; k < bombasTime.size(); k += 3) {
                    long currentTime = System.currentTimeMillis() - initTime;
                    long tiempoExplota = Long.parseLong(bombasTime.get(k).toString());
                    int xbomba = Integer.parseInt(bombasTime.get(k - 2).toString());
                    int ybomba = Integer.parseInt(bombasTime.get(k - 1).toString());
                    if (currentTime - tiempoExplota >= 1000) {
                        clasebomba.explotaBomba(xbomba, ybomba);
                    }
                    if (currentTime - tiempoExplota >= 1500) {
                        clasebomba.borraExplosion(xbomba, ybomba, k, blancox, blancoy);
                    }
                }

                ///ENEMIGO
                try{
                for (int i = 0; i < enemiesx.size(); i++) {
                    int antex = enemiesx.get(i);
                    int antey = enemiesy.get(i);
                    int r = (int) (Math.random() * (4 - 1 + 1) + 1);
                    //ABAJO
                    int abajo = tablero[antex + 1][antey];
                    int derecha =tablero[antex][antey + 1];
                    int arriba = tablero[antex - 1][antey];
                    int izquierda =tablero[antex][antey - 1];
                    if (r == 1 && ((abajo== 0) || (abajo == 6)) && (antex + 1) != blancox && antey != blancoy) {
                        enemiesx.remove(i);
                        enemiesx.add(i,antex + 1);
                        if (botones[antex + 1][antey].getIcon().equals(imgPlayer)) {
                            JOptionPane.showMessageDialog(null, "Muerte!");
                            System.exit(0);
                        }
                        moverenemigo(antex + 1, antey, antex, antey);
                        //DERECHA
                    } else if (r == 2 && ((derecha == 0) || (derecha == 6)) && (antex) != blancox && (antey + 1) != blancoy) {
                        enemiesy.remove(i);
                        enemiesy.add(i,antey + 1);
                        if (botones[antex][antey + 1].getIcon().equals(imgPlayer)) {
                            JOptionPane.showMessageDialog(null, "Muerte!");
                            System.exit(0);
                        }
                        moverenemigo(antex, antey + 1, antex, antey);
                        //ARRIBA
                    } else if (r == 3 && ((arriba == 0) || (arriba == 6)) && (antex - 1) != blancox && antey != blancoy) {
                        enemiesx.remove(i);
                        enemiesx.add(i, antex - 1);
                        if (botones[antex - 1][antey].getIcon().equals(imgPlayer)) {
                            JOptionPane.showMessageDialog(null, "Muerte!");
                            System.exit(0);
                        }
                        moverenemigo(antex - 1, antey, antex, antey);
                        //IZQUIERDA
                    } else if (r == 4 && ((izquierda == 0) || (izquierda == 6)) && antex != blancox && (antey - 1) != blancoy) {
                        enemiesy.remove(i);
                        enemiesy.add(i,antey - 1);
                        if (botones[antex][antey - 1].getIcon().equals(imgPlayer)) {
                            JOptionPane.showMessageDialog(null, "Muerte!");
                            System.exit(0);
                        }
                        moverenemigo(antex, antey - 1, antex, antey);
                    }

                }
                }catch(Exception e){
                    JOptionPane.showMessageDialog(null, "error con los enemies");
                }

                try {
                    //MOVIMIENTO AGENTE
                    if (!movcondi && !bombacondi) {
                        movimientoagente();
                        movcondi = true;
                    }
                    //MOVIMIENTO EN PANTALLA

                    //System.out.println("X:" + movimientosx.get(cont) + " - Y:" + movimientosy.get(cont));
                    int equis = movimientosx.get(cont);
                    int ye = movimientosy.get(cont);
                    if (botones[equis][ye].getIcon().equals(imgEnemie)) {
                        JOptionPane.showMessageDialog(null, "Muerte!!");
                        System.exit(0);
                    }
                    botones[equis][ye].setIcon(imgPlayer);

                    //META
                    if (antecaminox == blancox && antecaminoy == blancoy) {
                        botones[antecaminox][antecaminoy].setIcon(imgMeta);
                        Thread.sleep(500);
                        JOptionPane.showMessageDialog(null, "¡WIN!");
                        System.exit(0);
                    }

                    if (bombas[antecaminox][antecaminoy] == 7) {
                        botones[antecaminox][antecaminoy].setIcon(imgBomba);
                        tablero[antecaminox][antecaminoy]=8;
                    } else {
                        botones[antecaminox][antecaminoy].setIcon(imgVacio);
                        tablero[antecaminox][antecaminoy]=0;
                    }
                    antecaminox = movimientosx.get(cont);
                    antecaminoy = movimientosy.get(cont);

                    cont++;
                    if (movimientosx.size() == cont) {
                        cont = 0;
                        movcondi = false;
                        startX = movimientosx.get(movimientosx.size() - 1);
                        startY = movimientosy.get(movimientosy.size() - 1);
                        xBomba = movimientosx.get(movimientosx.size() - 1);
                        yBomba = movimientosy.get(movimientosy.size() - 1);
                        if (!bombacondi && !win) {
                            clasebomba.ponerBomba(xBomba, yBomba, System.currentTimeMillis() - initTime);
                        }
                        camino.clear();
                        movimientosx.clear();
                        movimientosy.clear();
                    }

                    if (bombacondi && contadorBomba == 0) {
                        movimientoLejosBomba();
                        contadorBomba++;
                    }

                } catch (Exception e) {
                    //System.out.println("Se acabó el array");
                }

                Thread.sleep(100);

            } catch (InterruptedException e) {

            }
        }
    }

    public static void main(String[] args) {

        Tablero tb = new Tablero();

    }

}
