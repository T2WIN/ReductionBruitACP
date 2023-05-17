package src.main;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        Image image = new Image("src/main/lenaa.png", 30);
        System.out.println(image.getMatrix());
        System.out.println(image.getMatrix()[0][0]);
        image.noising();
        BufferedImage newImage = image.createImageFromMatrix(image.getMatrix());
        image.createfile(newImage);
    }
}
