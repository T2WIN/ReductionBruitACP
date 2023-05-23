package src.main.java;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Image image = new Image("src/main/lenaa.png", 30, 5);
        

        // Error error = new Error("src/main/lenaa.png", 20);import java.awt.Color;
        // System.out.println(error.MeanSquaredError());
        // int[][] matrix = new int[2][2];
        // matrix[0][0] = 1;
        // matrix[0][1] = 2;
        // matrix[1][0] = 3;
        // matrix[1][1] = 4;
        // Patch patch = new Patch(matrix, 0, 0);
        // int[] vect = patch.vectorize();
        // patch.intoMatrix(vect);
        // System.out.println(patch.matrix[0][0]);
        // Extraction Patch Globale
        
        int [][] matriceImage = image.createMatrix("src/main/lenaa.png");
        image.noising(); 
        int [][] imageBruité = image.getNoisedMatrix();
        
        ArrayList<Patch> listePatch = new ArrayList<Patch>();
        listePatch = image.extractionPatch(matriceImage);   // a faire sur image bruité normalement

        
    }
}
