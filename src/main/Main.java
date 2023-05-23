package src.main;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        // Image image = new Image("src/main/lenaa.png", 2);
        // System.out.println(image.getMatrix());
        // BufferedImage newImage = image.createImageFromMatrix(image.getMatrix());
        // image.createfile(newImage);
        Image image = new Image("src/main/GrayScale.jpg", 20);


        // Error error = new Error("src/main/lenaa.png", 20);
        // System.out.println(error.MeanSquaredError());

        Seuillage seuillage = new Seuillage(image);


        double  threshold = seuillage.getVisuShrink();
        double alpha = 50;
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
