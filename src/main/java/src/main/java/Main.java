package src.main.java;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        Image image = new Image("src/main/lenaa.png", 30, 5);
        octave i  = new octave();
        octave.DecoupeImage(image, 100);
        System.out.println("");
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
        Seuillage seuillage = new Seuillage(image);


        double  threshold = seuillage.getVisuShrink();
        double alpha = -150;
        double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha);
        double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha);

        System.out.println("Résultat du seuillage avec HardThresholding : " + hardThresholdingResult);
        System.out.println("Résultat du seuillage avec SoftThresholding : " + softThresholdingResult);

        double seuillageV = seuillage.seuilV();
        double seuillageB = seuillage.seuilB();


        System.out.println("Résultat du seuilV : " + seuillageV);
        System.out.println("Résultat du seuilB : " + seuillageB);


    }    
}
