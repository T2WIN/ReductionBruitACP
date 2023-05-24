package src.main.java;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        
        int sigma = readConsole("Saisir un entier sigma pour ajouter à la photo un bruit gaussien : ");
        int taille = readConsole("donner la taille du patch que vous souhaitez : ");
        Image image = new Image("src/main/lenaa.png", sigma, taille);
        Seuillage seuillage = new Seuillage(image);
        int choixMethode = chooseMethode();

        if (choixMethode == 1) {
            image.noising();
            int[][] imageBruité = image.getNoisedMatrix();
            int l=imageBruité.length;
            int c=imageBruité[1].length;
            ArrayList<Patch> listePatch = image.extractionPatch(image.getNoisedMatrix());
            int[][] matricePatchs = image.vectorPatch(listePatch);
            ACP acp = new ACP(matricePatchs);
            acp.MoyCov();
            acp.DoACP();
            acp.Proj();
         

            double[][] alpha = acp.getVcontrib();
            int choixSeuil = chooseSeuil();
            int choixSeuillage = chooseSeuillage();

            if (choixSeuil == 1) {

                double  threshold = seuillage.getVisuShrink();

                if (choixSeuillage == 1) {

                    for (int i = 0; i < alpha.length; i++) {

                        for (int j = 0; j < alpha.length; j++) {

                        double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha[i][j]);
                        alpha[i][j] = hardThresholdingResult;

                        }
                    }
                }
                if (choixSeuillage == 2) {

                    for (int i = 0; i < alpha.length; i++) {

                        for (int j = 0; j < alpha.length; j++) {

                        double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha[i][j]);
                        alpha[i][j] = softThresholdingResult;

                        }
                
                    }
                }
            }

            if (choixSeuil == 2) {

                double  threshold = seuillage.getBayesShrink();

                if (choixSeuillage == 1) {

                    for (int i = 0; i < alpha.length; i++) {

                        for (int j = 0; j < alpha.length; j++) {

                        double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha[i][j]);
                        alpha[i][j] = hardThresholdingResult;

                        }
                    }
                }
                if (choixSeuillage == 2) {

                    for (int i = 0; i < alpha.length; i++) {

                        for (int j = 0; j < alpha.length; j++) {

                        double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha[i][j]);
                        alpha[i][j] = softThresholdingResult;
                        int[][] assemblerMatrice = image.assemblagePatch(listePatch, l, c);

                
                    }
                }
            }
        }
        
    }

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


        // double  threshold = seuillage.getVisuShrink();
        // double alpha = -150;
        // double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha);
        // double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha);

        // System.out.println("Résultat du seuillage avec HardThresholding : " + hardThresholdingResult);
        // System.out.println("Résultat du seuillage avec SoftThresholding : " + softThresholdingResult);

        // double seuillageV = seuillage.seuilV();
        // double seuillageB = seuillage.seuilB();


        // System.out.println("Résultat du seuilV : " + seuillageV);
        // System.out.println("Résultat du seuilB : " + seuillageB);


    }    
    public static int readConsole(String message) {
        try {
            Scanner sc;
            sc = new Scanner(System.in);
            System.out.print(message);
            int s;
            s = sc.nextInt();
            return s;
        } catch(Exception as) {
            System.out.println("Erreur de paramètre d'entrée. Il faut que ce soit un entier");
            readConsole(message);
        }
        return -1;
        
    }
    public static int chooseMethode() {

        int choixMethode = readConsole("voulez vous faire la méthode globale ou locale : globale : tapez 1  Locale : tapez 2 : ");

        if (choixMethode == 1) {

            return choixMethode;
        }
        if (choixMethode == 2) {

            return choixMethode;
        }
        else {
            System.out.println("Vous n'avez pas tapé un numéro correct pour le choix de la méthode de patchs. Recommencez s'il vous plaît.");
            chooseMethode();
        }

        return -1;

    }
    public static int chooseSeuil() {

        int choixSeuil = readConsole("Souhaitez vous un seuil visuShrink ou un seuil bayesShrink ? pour le seuil visuShrink : tapez 1  pour le seuil bayesShrink : tapez 2");

        if (choixSeuil == 1) {

            return choixSeuil;
        }
        if (choixSeuil == 2) {

            return choixSeuil;
        }
        else {
            System.out.println("Vous n'avez pas tapé un numéro correct pour le choix du type de seuil. Recommencez s'il vous plaît.");
            chooseSeuil();
        }

        return -1;

    }

    public static int chooseSeuillage() {

        int choixSeuillage = readConsole("Souhaitez vous un seuillage dur ou un seuillage doux ? pour le seuillage dur : tapez 1  pour le seuillage doux : tapez 2");

        if (choixSeuillage == 1) {

            return choixSeuillage;
        }
        if (choixSeuillage == 2) {

            return choixSeuillage;
        }
        else {
            System.out.println("Vous n'avez pas tapé un numéro correct pour le choix du type de seuillage. Recommencez s'il vous plaît.");
            chooseSeuillage();
        }

        return -1;

    }
}
