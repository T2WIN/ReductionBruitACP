package src.main.java;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        
        int sigma = readConsole("Saisir un entier sigma pour ajouter à la photo un bruit gaussien : ");
        int taille = readConsole("donner la taille du patch que vous souhaitez : ");
        Image image = new Image("src/main/img/imagette(2,3).jpg", sigma, taille);
        image.noising();
        Seuillage seuillage = new Seuillage(image);
        int choixMethode = chooseMethode();

        if (choixMethode == 1) {
            int[][] imageBruité = image.getNoisedMatrix();
            int l=imageBruité.length;
            int c=imageBruité[1].length;
            ArrayList<Patch> listePatch = image.extractionPatch(image.getNoisedMatrix());
            int[][] matricePatchs = image.vectorPatch(listePatch);
            // for (int i = 0; i < listePatch.get(0).matrix.length; i++) {
            //     for (int j = 0; j < listePatch.get(0).matrix[0].length; j++) {
            //         System.out.println(listePatch.get(0).matrix[i][j]);
            //     }
            // }
            ACP acp = new ACP(matricePatchs);
            acp.MoyCov();
            acp.DoACP();
            acp.Proj();
            double[][] u = acp.getU();
         

            double[][] alpha = acp.getVcontrib();
            double[] meanVector = acp.getMoyCov();
            int[] meanVectore;
            // for (int k = 0; k < alpha.length; k++){
            //     for (int j = 0; j < alpha[1].length; j++ ) {
            //         System.out.println(alpha[k][j]);
            //     }
            //     System.out.println("ici");
            // }
            // Les valeurs varient entre +/-30 
            int choixSeuil = chooseSeuil();
            int choixSeuillage = chooseSeuillage();

            if (choixSeuil == 1) {

                double  threshold = seuillage.getVisuShrink();

                if (choixSeuillage == 1) {

                    for (int i = 0; i < alpha.length; i++) {

                        for (int j = 0; j < alpha[1].length; j++) {

                        double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha[i][j]);
                        alpha[i][j] = hardThresholdingResult;

                        }
                    }
                }
                if (choixSeuillage == 2) {

                    for (int i = 0; i < alpha.length; i++) {

                        for (int j = 0; j < alpha[1].length; j++) {

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

                        for (int j = 0; j < alpha[1].length; j++) {

                        double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha[i][j]);
                        alpha[i][j] = hardThresholdingResult;

                        }
                    }
                }
                if (choixSeuillage == 2) {

                    for (int i = 0; i < alpha.length; i++) {

                        for (int j = 0; j < alpha[1].length; j++) {

                        double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha[i][j]);
                        alpha[i][j] = softThresholdingResult;
                        

                
                    }
                }
                
            }

        }
        

        int[][] matPatch;
        int[][] patchVect = new int[alpha.length][alpha[0].length];
        int [] patchVect2 = new int[taille*taille];
        int [] somme = new int[taille*taille];
        int a;

        //System.out.println(listePatch.size());
        for (int i = 0; i < listePatch.size(); i++) {
            int x;
            int y;
            // System.out.println(alpha.length);
            // System.out.println(alpha[0].length);
            x = listePatch.get(i).positionX;
            y = listePatch.get(i).positionY;
            matPatch = listePatch.get(i).matrix;
            Patch patch = new Patch(matPatch,x,y);
            for (int k = 0; k < alpha.length; k++){
                for (int j = 0; j < alpha[1].length; j++ ) {
                    patchVect[k][j] = (int) alpha[k][j];
                }
            }

            for (int m=0; m<taille*taille;m++){
                for(int n=0; n<taille*taille;n++){
                    a = patchVect[i][m] * (int) u[m][n];
                    somme[m] = somme[m] + a;
                }
            for (int o=0;o<taille*taille;o++){
                meanVectore[o]= (int) meanVector[o];
            }
            for (int p=0;p<taille*taille;p++){
                patchVect2[p] = meanVectore[p] + somme[p];
            }
            
            matPatch = patch.intoMatrix(patchVect2);
            patch.setMatrix(patchVect);
            listePatch.set(i, patch);
        }
        int[][] assemblerMatrice = image.assemblagePatch(listePatch, l, c);
        for (int index = 0; index < l; index++) {
            for (int i = 0; i < c; i++) {
                System.out.println(assemblerMatrice[index][i]);
            }
            
        }
        BufferedImage imagefinale = image.createImageFromMatrix(assemblerMatrice);
        image.createfile(imagefinale, "imagefinale");
    }

        


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
            Scanner sc;
            sc = new Scanner(System.in);
            System.out.print(message);
            int s;
            s = sc.nextInt();
            return s;
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
