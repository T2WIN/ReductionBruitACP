package src.main.java;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import java.awt.Color;

public class octave {
    public static void DecoupeImage(Image X, int W){

        int[][] Tab = X.getMatrix();

        int imagettesEnLargeur = Tab.length / W;
        int imagettesEnHauteur = Tab[0].length / W;

        int nombreTotalImagettes = imagettesEnLargeur * imagettesEnHauteur;

        for ( int i = 0 ; i < imagettesEnHauteur ; i++ ){
            for ( int j = 0 ; j < imagettesEnLargeur ; j++){
                int x = i * W;
                int y = j * W;

                BufferedImage imagette = getSubImage(x,y,W,X);
                createfile(imagette, "imagette(" + i + "," + j + ")");
            }
        }
    }  
    
    public static BufferedImage getSubImage(int x, int y, int W, Image X){

        int[][] Tab = X.getMatrix();
        int[][] imagetteMatrice = new int[W][W];
        BufferedImage imagette;

        for ( int i = 0 ; i < W ; i++){
            for ( int j = 0 ; j < W ; j++){
                
                imagetteMatrice[i][j] = Tab[x+i][y+j];
            }
        }
        imagette = createImageFromMatrix(imagetteMatrice);
        return imagette;
    }

    public static BufferedImage createImageFromMatrix(int[][] matrix) {
        BufferedImage image = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_RGB);
        try {
            
            for(int i=0; i<matrix.length; i++) {
                for(int j=0; j<matrix[0].length; j++) {
                    int a = matrix[i][j];
                    Color newColor = new Color(a,a,a);
                    image.setRGB(i,j,newColor.getRGB());
                }
            }
        }
        
        catch(Exception e) {
            System.out.println(("Error creating image"));
            e.printStackTrace();
        }
        return image;
    }

    //Utilise un objet BufferedImage pour retourner l'image sous forme de fichier jpg
    public static void createfile(BufferedImage image, String nom) {
        File output = new File("./src/main/img/" + nom + ".jpg");
        try {
            output.createNewFile();
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

  
}
