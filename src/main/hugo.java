package src.main;
// Test Lecteur Image 

// Import Lecture Image
import java.io.*;

import java.util.ArrayList;
import java.util.Scanner;
//import java.util.Arrays;

public class hugo{
    public static void main(String[] args) throws IOException {
        
        // Extraction Patch Globale
        Scanner sc;
		sc = new Scanner(System.in);
		System.out.print("Saisir l'entier s, taille du patch : ");
		int s;
		s = sc.nextInt(); 
        
        int [][] patchcourant = new int[s][s];
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
                // Position du pixel du coin gauche 
                coint_sup_x= i;
                coint_sup_y= j;
                // Création d'un patch à partir de chaque pixel
                Patch patch = new Patch(patchcourant,coint_sup_x,coint_sup_y);
                // Ajout des pixels de l'image dans chaque patch
                for (int k = coint_sup_x; k < coint_sup_x + s;k++){
                    for (int l = coint_sup_y; l < coint_sup_y + s; l++){
                        patchcourant[k - coint_sup_x][l - coint_sup_y] = test[k][l];
                        System.out.println(test[k][l]);
                    }
                }
                System.out.println(newLine);

                ListePatch.add(patch);
            }   
        }
        sc.close();

        // Reconstruction Patch
        
    }
}





