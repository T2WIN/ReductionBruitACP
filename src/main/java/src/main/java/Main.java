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
      int choixSeuil = chooseSeuil();
      int choixSeuillage = chooseSeuillage();
        

      if (choixMethode == 1) {
         methodeGlobale(image,"src/main/img/global",seuillage,taille,choixSeuil,choixSeuillage);
      }
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
    //     double[][] alpha = acp.getVcosc = new Scanner(System.in)ntrib();
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
   

   public static void methodeGlobale(Image image,String fichier,Seuillage seuillage,int taille,int choixSeuil,int choixSeuillage) {
      ArrayList<Patch> listePatch = image.extractionPatch(image.getNoisedMatrix());
      int l = image.getNoisedMatrix().length;
      int c = image.getNoisedMatrix()[1].length;
      int[][] matricePatchs = image.vectorPatch(listePatch);
      ACP acp = new ACP(matricePatchs);
      acp.MoyCov();
      acp.DoACP();
      acp.Proj();
      double[][] alpha = acp.getVcontrib();
      double[] meanVector = acp.getMoyCov();
      double[][] u = acp.getU();
 
    
      // Modification de la matrice alpha par les seuillages
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
            for (int i=0; i<taille*taille; i++){
            //On décentre les vecteurs
               patchVect2[i] = meanVector[i] + somme[i];
            }
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
         Image.createfile(imagefinale, fichier);
      }
   }
}

