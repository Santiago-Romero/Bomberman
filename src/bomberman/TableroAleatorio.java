package bomberman;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 *
 * @author ADMIN
 */
public class TableroAleatorio {

    int tamañoX;
    int tamañoY;
    int enemigosAleatoriaX;
    int enemigosAleatoriaY;
    int numeroEnemigos;
    int aleatorioEstado;
    int[][] tablero;
    BufferedWriter bfwriter;

    public TableroAleatorio() {
        generarMatrizTablero();
        crearFileNivel();
    }

    public void generarMatrizTablero() {

        // genera números entre 10y 25 para el tamaño del tablero    
        tamañoX = (int) (Math.random() * (14 - 10 + 1) + 10);
        tamañoY = (int) (Math.random() * (25 - 10 + 1) + 10);

        tablero = new int[tamañoX][tamañoY];

        System.err.println(tamañoX + " " + tamañoY);
        // genera paredes y pasto
        for (int i = 0; i < tamañoX; i++) {
            for (int j = 0; j < tamañoY; j++) {

                aleatorioEstado = (int) (Math.random() * 3);
                if (aleatorioEstado == 0) {
                    tablero[i][j] = 0;
                }
                if (aleatorioEstado == 1) {
                    tablero[i][j] = 1;
                }

                if (aleatorioEstado == 2) {
                    tablero[i][j] = 2;
                }

                if (i == 0 || j == 0 || i == tamañoX - 1 || j == tamañoY - 1) {
                    tablero[i][j] = 1;
                }

            }
        }


        //genera la cantidad de enemigos    
        numeroEnemigos = (int) (Math.random() * 3) + 1;

        // genera posiciones de enemigos
        for (int i = 0; i < numeroEnemigos; i++) {
            do {
                enemigosAleatoriaX = (int) (Math.random() * (tamañoX - 2)) + 1;
                enemigosAleatoriaY = (int) (Math.random() * (tamañoY - 2)) + 1;
            } while ((tablero[enemigosAleatoriaX][enemigosAleatoriaY] == 5));
            tablero[enemigosAleatoriaX][enemigosAleatoriaY] = 5;
        }
        
       tablero[1][1]=0;

    }

    public void crearFileNivel() {
        FileWriter fw = null;
        try {
            fw = new FileWriter("nivel.txt");
            bfwriter = new BufferedWriter(fw);
            for (int i = 0; i < tamañoX; i++) {
                for (int j = 0; j < tamañoY; j++) {
                    fw.write(tablero[i][j] + "");
                }
                fw.write("\n");
            }

            bfwriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {//cierra el flujo principal
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
