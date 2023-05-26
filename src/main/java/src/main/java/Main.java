package src.main.java;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        
        int sigma = readConsole("Saisir un entier sigma pour ajouter à la photo un bruit gaussien : ");
        int taille = readConsole("donner la taille du patch que vous souhaitez : ");
        Image image = new Image("src/main/img/lenaa.png", sigma, taille);
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
        
        //On crée la matrice qui contient tous les patchs vectorisés (à valeurs entières)
        int[][] patchVect = new int[alpha.length][alpha[0].length];
        for (int k = 0; k < alpha.length; k++){
            for (int j = 0; j < alpha[1].length; j++ ) {
                patchVect[k][j] = (int) alpha[k][j];
            }
        }

        
        int [] somme = new int[taille*taille];
        int[] meanVectore = new int[meanVector.length];
        int a;
        
        for (int i = 0; i < 10; i++) {
            Patch patch = listePatch.get(i);
            System.out.println("Patch " + i);
            for (int index = 0; index < patch.getMatrix().length; index++) {
                for (int index2 = 0; index2 < patch.getMatrix()[0].length; index2++) {
                    System.out.println(patch.getMatrix()[index][index2]);
                }
            
            }
            int [] patchVect2 = new int[taille*taille];
            
            //Pour chaque (alpha(i), u(i)), on applique la formule
            for (int m=0; m<taille*taille;m++){
                //On calcule le produit alpha(i) * u(i)
                for(int n=0; n<taille*taille;n++){
                    a = patchVect[i][m] * (int) u[m][n];
                    somme[m] = somme[m] + a;
                }

                for (int o=0;o<taille*taille;o++){
                    //Cette première ligne sert à transformer le vecteur moyen en vecteur d'entiers
                    meanVectore[o]= (int) meanVector[o];

                    patchVect2[o] = meanVectore[o] + somme[o];
                }
            }

            //Verifie si un des vecteurs contient un élément négatif
            for (int f = 0; f < patchVect2.length; f++) {
                if (patchVect2[f] < 0) {
                    System.out.println("Négatif");
                    System.exit(0);
                }
            }
            // int[] test = patchVect2;
            // System.out.println("Patch n° " + i);
            // for (int j = 0; j<test.length; j++) {
                
            //     System.out.println(test[j]);
                
            // }
            patch.setMatrix(patch.intoMatrix(patchVect2));
            listePatch.set(i, patch);
        }

        for (int x = 0; x < 10; x++) {
            
        }
        
        
        int[][] assemblerMatrice = image.assemblagePatch(listePatch, l, c);
        BufferedImage imagefinale = Image.createImageFromMatrix(assemblerMatrice);
        Image.createfile(imagefinale, "imagefinalee");
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

