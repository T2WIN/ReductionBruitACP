package src.main;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        // Image image = new Image("src/main/lenaa.png", 20);
        // image.noising();
        // BufferedImage newImage = image.createImageFromMatrix();
        // image.createfile(newImage);

        Error error = new Error("src/main/lenaa.png", 200);
        System.out.println(error.MeanSquaredError());
    }

        
        
    
}
