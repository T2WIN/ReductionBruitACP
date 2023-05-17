package src.main;
// Test Lecteur Image 

// Import Lecture Image
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.midi.Patch;
import javax.swing.text.Position;

import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
// Import Algo Extraction
import java.util.Scanner;
//import java.util.Arrays;

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
                // System.out.println(tabImage[i][j]);
            }
        }
        // Extraction Patch Globale? 
        // Algo 1 Dans Descriptif Projet 
        // Remarque x=l et y=c
        Scanner sc;
		sc = new Scanner(System.in);
		System.out.print("Saisir l'entier s, taille du patch : ");
		int s;
		s = sc.nextInt(); 


        
        int [][] patchcourant = new int[s][s];
        int coint_sup_x;
        int coint_sup_y;
        ArrayList<Patch> ListePatch= new ArrayList<Patch>();


        for (int i = 0; i < x-s; i++) {
            for (int j = 0; j < y-s; j++) {
                // Création d'un patch à partir de chaque pixel
                Patch patch = new Patch(patchcourant,coint_sup_x,coint_sup_y);
                // Position du pixel du coin gauche 
                coint_sup_x= i;
                coint_sup_y= j;
                // Ajout des pixels de l'image dans chaque patch
                for (int k = coint_sup_x; k < coint_sup_x + s;k++){
                    for (int l = coint_sup_y; l < coint_sup_y + s; l++){
                        patchcourant[k][l]=tabImage[k][l];
                    }
                }

                ListePatch.add(patch);

            }   
        }
        sc.close();


    }
}





