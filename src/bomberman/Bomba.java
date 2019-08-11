/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import static bomberman.Tablero.bombas;
import static bomberman.Tablero.botones;
import static bomberman.Tablero.enemiesx;
import static bomberman.Tablero.enemiesy;
import static bomberman.Tablero.imgBomba;
import static bomberman.Tablero.imgVacio;
import static bomberman.Tablero.tablero;
import static bomberman.Tablero.xBomba;
import static bomberman.Tablero.yBomba;
import static bomberman.Tablero.bombacondi;
import static bomberman.Tablero.huirBombax;
import static bomberman.Tablero.huirBombay;
import static bomberman.Tablero.movimientosx;
import static bomberman.Tablero.movimientosy;
import static bomberman.Tablero.contadorBomba;
import static bomberman.Tablero.win;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class Bomba {

    private ArrayList bombasTime = new ArrayList();
    ImageIcon imgExplotaCentro;
    ImageIcon imgExplotaIzquierda;
    ImageIcon imgExplotaDerecha;
    ImageIcon imgExplotaArriba;
    ImageIcon imgExplotaAbajo;
    ImageIcon imgMeta;

    public Bomba(ArrayList bombasTime) {
        this.bombasTime = bombasTime;
        imgBomba = new ImageIcon("bomba.jpg");
        imgExplotaCentro = new ImageIcon("explotaCentro.jpg");
        imgExplotaIzquierda = new ImageIcon("explotaIzq.jpg");
        imgExplotaDerecha = new ImageIcon("explotaDer.jpg");
        imgExplotaArriba = new ImageIcon("explotaUp.jpg");
        imgExplotaAbajo = new ImageIcon("explotaDown.jpg");
        imgMeta = new ImageIcon("meta.jpg");
        int posiLista = 0;
    }

    public void ponerBomba(int xbomba, int ybomba, long initbomba) {
        bombas[xBomba][yBomba] = 7;
        botones[xbomba][ybomba].setIcon(imgBomba);
        bombasTime.add(xbomba);
        bombasTime.add(ybomba);
        bombasTime.add(initbomba);
        //System.out.println("bomba" + initbomba);
        bombacondi = true;
        contadorBomba = 0;
    }

    public void explotaBomba(int xbom, int ybom) {
        botones[xbom][ybom].setIcon(imgExplotaCentro);
        if (tablero[xbom - 1][ybom] != 1) {
            if(tablero[xbom - 1][ybom] == 5){
                for(int i=0;i<enemiesx.size();i++){
                    if(enemiesx.get(i)==(xbom-1)){
                        enemiesx.remove(i);
                        enemiesy.remove(i);
                    }
                }
            }
            botones[xbom - 1][ybom].setIcon(imgExplotaArriba);
            tablero[xbom - 1][ybom] = 0;
        }

        if (tablero[xbom + 1][ybom] != 1) {
            if(tablero[xbom + 1][ybom] == 5){
                for(int i=0;i<enemiesx.size();i++){
                    if(enemiesx.get(i)==(xbom+1)){
                        enemiesx.remove(i);
                        enemiesy.remove(i);
                    }
                }
            }
            botones[xbom + 1][ybom].setIcon(imgExplotaAbajo);
            tablero[xbom + 1][ybom] = 0;
        }

        if (tablero[xbom][ybom - 1] != 1) {
            if(tablero[xbom][ybom - 1] == 5){
                for(int i=0;i<enemiesy.size();i++){
                    if(enemiesy.get(i)==(ybom-1)){
                        enemiesy.remove(i);
                        enemiesx.remove(i);
                    }
                }
            }
            botones[xbom][ybom - 1].setIcon(imgExplotaIzquierda);
            tablero[xbom][ybom - 1] = 0;

        }

        if (tablero[xbom][ybom + 1] != 1) {
            if(tablero[xbom][ybom + 1] == 5){
                for(int i=0;i<enemiesy.size();i++){
                    if(enemiesy.get(i)==(ybom+1)){
                        enemiesx.remove(i);
                        enemiesy.remove(i);
                    }
                }
            }
            botones[xbom][ybom + 1].setIcon(imgExplotaDerecha);
            tablero[xbom][ybom + 1] = 0;

        }
        bombas[xbom][ybom] = 0;
        tablero[xbom][ybom] = 0;

        //bombas seguidas
        if (bombas[xbom - 1][ybom] == 7) {
            explotaBomba(xbom - 1, ybom);
        }
        if (bombas[xbom + 1][ybom] == 7) {
            explotaBomba(xbom + 1, ybom);
        }
        if (bombas[xbom][ybom - 1] == 7) {
            explotaBomba(xbom, ybom - 1);
        }
        if (bombas[xbom][ybom + 1] == 7) {
            explotaBomba(xbom, ybom + 1);
        }

    }

    public void borraExplosion(int xbom, int ybom, int posLista, int metax, int metay) {
        botones[xbom][ybom].setIcon(imgVacio);
        bombacondi = false;
        movimientosx.clear();
        movimientosy.clear();
        huirBombax.clear();
        huirBombay.clear();
        if (!bombasTime.isEmpty()) {
            bombasTime.remove(posLista);
            bombasTime.remove(posLista - 1);
            bombasTime.remove(posLista - 2);
        }

        if (tablero[xbom - 1][ybom] != 1) {
            if ((xbom - 1) == metax && ybom == metay) {
                botones[xbom - 1][ybom].setIcon(imgMeta);
                tablero[xbom - 1][ybom] = 9;
                win = true;
            } else {
                botones[xbom - 1][ybom].setIcon(imgVacio);
            }
        }

        if (tablero[xbom + 1][ybom] != 1) {
            if ((xbom + 1) == metax && ybom == metay) {
                botones[xbom + 1][ybom].setIcon(imgMeta);
                tablero[xbom + 1][ybom] = 9;
                win = true;
            } else {
                botones[xbom + 1][ybom].setIcon(imgVacio);
            }
        }

        if (tablero[xbom][ybom - 1] != 1) {
            if (xbom == metax && (ybom - 1) == metay) {
                botones[xbom][ybom - 1].setIcon(imgMeta);
                tablero[xbom][ybom - 1] = 9;
                win = true;
            } else {
                botones[xbom][ybom - 1].setIcon(imgVacio);
            }
        }

        if (tablero[xbom][ybom + 1] != 1) {
            if (xbom == metax && (ybom + 1) == metay) {
                win = true;
                botones[xbom][ybom + 1].setIcon(imgMeta);
                tablero[xbom][ybom + 1] = 9;
            } else {
                botones[xbom][ybom + 1].setIcon(imgVacio);
            }
        }

    }

}
