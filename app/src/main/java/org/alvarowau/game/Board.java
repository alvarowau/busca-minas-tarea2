package org.alvarowau.game;

import org.alvarowau.model.Configuration;

import java.util.Random;

public class Board {
    private int cells;
    private int hypotenochas;
    private int[][] matrix;
    private boolean[][] pressed;


    public Board(Configuration config) {
        cells = config.getCells();
        hypotenochas = config.getHypotenochas();

        // Creating an additional matrix to track the pressed cells.
        pressed = new boolean[cells][cells];
        for (int x = 0; x < cells; x++) {
            for (int y = 0; y < cells; y++) {
                pressed[x][y] = false;
            }
        }
    }


    public void play() {
        placeHypotenochas();
        placeClues();
    }

    public void placeHypotenochas() {
        matrix = new int[cells][cells];
        Random rnd = new Random();

        int index = 0;

        while (index < hypotenochas) {
            int x = rnd.nextInt(cells);
            int y = rnd.nextInt(cells);
            if (matrix[x][y] != -1) {
                matrix[x][y] = -1;
                index += 1;
            }
        }
    }


    private void placeClues() {
        for (int x = 0; x < cells; x++) {
            for (int y = 0; y < cells; y++) {
                if (matrix[x][y] != -1) {
                    int counter = 0;
                    if ((x - 1 >= 0) && (y - 1 >= 0) && matrix[x - 1][y - 1] == -1) counter++;
                    if ((x - 1 >= 0) && matrix[x - 1][y] == -1) counter++;
                    if ((x - 1 >= 0) && (y + 1 < cells) && matrix[x - 1][y + 1] == -1) counter++;
                    if ((y - 1 >= 0) && matrix[x][y - 1] == -1) counter++;
                    if ((y + 1 < cells) && matrix[x][y + 1] == -1) counter++;
                    if ((x + 1 < cells) && (y - 1 >= 0) && matrix[x + 1][y - 1] == -1) counter++;
                    if ((x + 1 < cells) && matrix[x + 1][y] == -1) counter++;
                    if ((x + 1 < cells) && (y + 1 < cells) && matrix[x + 1][y + 1] == -1)
                        counter++;
                    matrix[x][y] = counter;
                }
                // Uncomment to see the matrix in the console (for debugging).
                System.out.print(matrix[x][y] == -1 ? "B" : matrix[x][y]);
            }
            // Uncomment to see the matrix in the console (for debugging).
            System.out.println("\n");
        }
    }


    public int checkCell(int x, int y) {
        if (x >= 0 && x < cells && y >= 0 && y < cells) {
            return matrix[x][y];
        } else {
            return -2;
        }
    }


    public boolean isPressed(int x, int y) {
        return pressed[x][y];
    }


    public void setPressed(int x, int y) {
        pressed[x][y] = true;
    }
}
