package src.main;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        Image image = new Image("src/main/lenaa.png", 2);
        System.out.println(image.getMatrix());
        BufferedImage newImage = image.createImageFromMatrix(image.getMatrix());
    }
}
