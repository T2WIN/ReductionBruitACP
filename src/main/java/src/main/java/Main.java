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
        
        image.noising(); 
        int [][] imageBruité = image.getNoisedMatrix();
        int l;
        int c;
        l = imageBruité.length;
        c = imageBruité[1].length;
        ArrayList<Patch> listePatch = new ArrayList<Patch>();
        listePatch = image.extractionPatch(imageBruité);   

        
        




        // Après l'ACP et le seuillage, une fois que la liste de patch est modifiée (forme matrice)

        int [][] imageDébruité = image.assemblagePatch(listePatch,l,c);
        
    }
}
