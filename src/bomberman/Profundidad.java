/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bomberman;

import java.util.List;
import static bomberman.Tablero.win;

public class Profundidad {

    public static boolean buscarCamino(int[][] tablero, int x, int y,
            List<Integer> camino) {
        if (win) {
            if (tablero[y][x] == 9) {
                camino.add(x);
                camino.add(y);
                return true;
            }
        } else {
            if (tablero[y][x] == 2) {
                camino.add(x);
                camino.add(y);
                return true;
            }
        }

        if (tablero[y][x] == 0 || tablero[y][x] == 8 || tablero[y][x] == 7) {
            tablero[y][x] = 6;

            int dx = -1;
            int dy = 0;
            if (buscarCamino(tablero, x + dx, y + dy, camino)) {
                camino.add(x);
                camino.add(y);
                return true;
            }

            dx = 1;
            dy = 0;
            if (buscarCamino(tablero, x + dx, y + dy, camino)) {
                camino.add(x);
                camino.add(y);
                return true;
            }

            dx = 0;
            dy = -1;
            if (buscarCamino(tablero, x + dx, y + dy, camino)) {
                camino.add(x);
                camino.add(y);
                return true;
            }

            dx = 0;
            dy = 1;
            if (buscarCamino(tablero, x + dx, y + dy, camino)) {
                camino.add(x);
                camino.add(y);
                return true;
            }
        }
        return false;
    }

}
