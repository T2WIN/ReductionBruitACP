// Test Lecteur Image 

import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
public class hugo{
    public static void main(String[] args) throws IOException {
        File fileSelected = new File("lenaa.png");
        System.out.println("file to be opened :" + fileSelected);

        BufferedImage image;

        image = ImageIO.read(fileSelected);
        int x;
        int y;
        x=image.getWidth();
        y=image.getHeight();
        int [][] tabImage = new int[x][y];
        // Parcours de l'intégralité des pixels 
        // Puis création de la matrice associée à l'image
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                // Transformation des pixels en couleur en gris
                Color pixel = new Color(image.getRGB(i, j));
                int r= pixel.getRed();
                int g=pixel.getGreen();
                int b=pixel.getBlue();
                int gris = (r+g+b)/3;
                tabImage[i][j]=gris;
                System.out.println(tabImage[i][j]);
            }
        }
    }
}




