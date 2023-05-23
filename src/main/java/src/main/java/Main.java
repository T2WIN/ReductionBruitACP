package src.main.java;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Image image = new Image("src/main/lenaa.png", 30, 5);
        octave p  = new octave();
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
        // Extraction Patch Globale
        Scanner sc;
		sc = new Scanner(System.in);
		System.out.print("Saisir l'entier s, taille du patch : ");
		int s;
		s = sc.nextInt(); 
        
        int coint_sup_x;
        int coint_sup_y;
        ArrayList<Patch> ListePatch= new ArrayList<Patch>();

        int [][] test = new int[5][5];
        String newLine = System.getProperty("line.separator");

        for (int index = 0; index < 5; index++) {
            for (int i = 0; i < 5; i++) {
                test[index][i]=i;
                System.out.println(test[index][i]);
            }
            System.out.println(newLine);
        }

        System.out.println("ici");

        for (int i = 0; i < 5-s+1; i++) {
            for (int j = 0; j < 5-s +1; j++) {
                int[][] patchcourant = new int[s][s];
                // Position du pixel du coin gauche 
                coint_sup_x= i;
                coint_sup_y= j;
                
                // Ajout des pixels de l'image dans chaque patch
                for (int k = coint_sup_x; k < coint_sup_x + s;k++){
                    for (int l = coint_sup_y; l < coint_sup_y + s; l++){
                        patchcourant[k - coint_sup_x][l - coint_sup_y] = test[k][l];
                        System.out.println(test[k][l]);
                    }
                }
                // Création d'un patch à partir de chaque pixel
                Patch patch = new Patch(patchcourant,coint_sup_x,coint_sup_y);
                System.out.println(newLine);
                ListePatch.add(patch);
            }   
        }
        sc.close();

        // Reconstruction Patch
        
        int x=5;
        int y=5;

        int [][] test2 = new int[x][y];
        int [][] matricePoids = new int [x][y]; 
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                test2[i][j]=0;
                matricePoids[i][j]=0;
            }
        }

        int cointx;
        int cointy;

        // Ajout des patchs dans la matrice 
        for (int k = 0; k < ListePatch.size(); k++) {
            cointx = ListePatch.get(k).positionX;
            cointy = ListePatch.get(k).positionY;
            int[][] patch = ListePatch.get(k).matrix;
        
            for (int l = 0; l < s; l++) {
                for (int m = 0; m < s; m++) {
                    // Superposition des patchs dans la matrice
                    test2[l + cointx][m + cointy] += patch[l][m];
                    // Ajout du poids pour chaque élément de la matrice
                    matricePoids[cointx + l][cointy + m] += 1;
                }
            }
        }
        System.out.println("ICI");
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                test2[i][j] = test2[i][j]/matricePoids[i][j];
                System.out.println(test2[i][j]);
            }
            System.out.println(newLine);
        }
        
    }
}
