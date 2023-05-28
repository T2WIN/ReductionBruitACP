package src.main.java;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        
        int sigma = readConsole("Saisir un entier sigma pour ajouter à la photo un bruit gaussien : ");
        int taille = readConsole("donner la taille du patch que vous souhaitez : ");
        Image image = new Image("src/main/img/lenaa.png", sigma, taille);
        image.noising();
        int[][] imageBruité = image.getNoisedMatrix();
        int l=imageBruité.length;
        int c=imageBruité[1].length;
        Seuillage seuillage = new Seuillage(image);
        int choixMethode = chooseMethode();
        

        if (choixMethode == 1) {
            
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
        for (int k = 0; k < listePatch.size(); k++) {
            double [] somme = new double[taille*taille];
            Patch patch = listePatch.get(k);
            double [] patchVect2 = new double[taille*taille];
            //Pour chaque (alpha(i), u(i)), on applique la formule
            for (int j = 0; j < alpha[0].length; j++) {
                for (int i = 0; i <alpha[0].length; i++) {
                    somme[j] += alpha[k][i] * u[i][j];
                }
            }
            // System.out.println(Arrays.toString(somme));
            // System.out.println(Arrays.toString(centeredVectors[k]));
            
            // for (int i=0; i<taille*taille; i++){
            //     //On calcule le produit alpha(i) * u(i)
            //     for(int n=0; n<taille*taille;n++){
            //         a = alphak[i] * u[i][n];
            //         somme[i] += a;
            //     }
            //     // System.out.println("Somme1 : " + i + " = " + somme[i]);
            // }   
            // System.out.println("Vect Moyen");
            // System.out.println("Patch " + k);
            for (int i=0; i<taille*taille; i++){
                //On décentre les vecteurs
                patchVect2[i] = meanVector[i] + somme[i];
                // if (patchVect2[i] > 255) {
                //     System.out.println("Sup 255");
                // }
    
                // if (patchVect2[i] < 0) {
                //     System.out.println("Inf 0");
                // }

            
            }
            // for (int index = 0; index < patchVect2.length; index++) {
            //     System.out.println(patchVect2[index]);
            // }
            //Verifie si un des vecteurs contient un élément négatif
            // for (int f = 0; f < patchVect2.length; f++) {
            //     if (patchVect2[f] < 0) {
            //         System.out.println("Négatif");
            //     }
            //     System.out.println(meanVector[f]);
            // }

            //Transformation de la matrice de patch de double vers entier
            double[][] patchMatrixDouble = patch.intoMatrix(patchVect2);
            int[][] patchMatrixEntier = new int[patchMatrixDouble.length][patchMatrixDouble[0].length];
            
            for (int indexC = 0; indexC<patchMatrixEntier.length; indexC++) {
                for (int indexL = 0; indexL<patchMatrixEntier[0].length; indexL++) {
                    patchMatrixEntier[indexC][indexL] = (int) patchMatrixDouble[indexC][indexL];      
                }    
            }
            patch.setMatrix(patchMatrixEntier);
            listePatch.set(k, patch);
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

    // public static image methodeGlobale(Image image) {

    //     ArrayList<Patch> listePatch = image.extractionPatch(image.getNoisedMatrix());
    //     int[][] matricePatchs = image.vectorPatch(listePatch);
    //     ACP acp = new ACP(matricePatchs);
    //     acp.MoyCov();
    //     acp.DoACP();
    //     acp.Proj();
    //     acp.afficherResultat();
    //     double[][] alpha = acp.getVcontrib();
    // }

    // // public static image methodeLocale(Image image) {
    // //     int tailleim = readConsole("donnez la taille de l'imagette que vous souhaitez : ");
    // //     int[] coord = DecoupeImage(image, tailleim);
    // //     Image imagette;
    // //     ArrayList<int[][]> listeImagettes = new ArrayList<int[][]>();
    // //     for (int i = 0 ; i< coord[0] ; i++) {
    // //         for (int j = 0 ; j< coord[1] ; j++) {
    // //             int[][] imagetteMatrice;
    // //             imagetteMatrice = image.createMatrix("src/main/img/imagette("+ i +","+ j +").jpg");
    // //             listeImagettes.add(methodeGlobale(imagetteMatrice));
    // //         }
    // //     }
    // //     for (int i = 0 ; i< coord[0] ; i++) {
    // //         for (int j = 0 ; j< coord[1] ; j++) {
            
    // //         }
    // //     }
    // }
}



