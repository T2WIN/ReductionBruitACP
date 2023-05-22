package src.main;

import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        Image image = new Image("src/main/lenaa.png", 30);
        image.noising();
        BufferedImage newImage = image.createImageFromMatrix();
        image.createfile(newImage);

        // Error error = new Error("src/main/lenaa.png", 20);
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

    }    
}
