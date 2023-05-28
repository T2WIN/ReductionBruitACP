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
        
      //Si on choisit la méthode globale
      if (choixMethode == 1) {
         int[][] assemblerMatrice = methodeGlobale(image,seuillage,taille,choixSeuil,choixSeuillage);
         BufferedImage imagefinale = Image.createImageFromMatrix(assemblerMatrice);
         Image.createfile(imagefinale,"global");
      }
      //Si on choisit la méthode locale
      else {
         int W = readConsole("Saisir un entier W, taille de l'imagette : ");
         int n = readConsole("Saisir un entier n, nombre d'imagette : ");
         int [][] imageBruit = image.getNoisedMatrix();

         //On sépare l'image de départ en n imagettes de taille W
         ArrayList<Patch> listeImagette = image.extractImagettes(image.getMatrix(), W, n);
         ArrayList<Patch> listeImagetteBruité = image.extractImagettes(imageBruit, W, n);

         int [][] imagette = new int[W][W];
         int [][] imagetteBruité = new int[W][W];
         for (int i = 0; i < listeImagetteBruité.size(); i++) {
            imagette = listeImagette.get(i).getMatrix();
            imagetteBruité = listeImagetteBruité.get(i).getMatrix();
            Image objImagette = new Image(imagette, imagetteBruité, taille);
            //On applique la méthode globale aux n imagettes bruitées
            imagetteBruité = methodeGlobale(objImagette, seuillage, taille, choixSeuil, choixSeuillage);
            listeImagetteBruité.get(i).setMatrix(imagetteBruité);
         }
         int l = imageBruit.length;
         int c = imageBruit[0].length;
         //On rassemble ensuite les n imagettes débruitées pour obtenir l'image finale
         int[][] imageRecon = image.assemblageImagette(listeImagetteBruité, imageBruit, l,c, W);
         BufferedImage imagefinale = Image.createImageFromMatrix(imageRecon);
         Image.createfile(imagefinale,"local");
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

   public static int[][] methodeGlobale(Image image,Seuillage seuillage,int taille,int choixSeuil,int choixSeuillage) {
      //Etape 1 : Extraction des patchs
      ArrayList<Patch> listePatch = image.extractionPatch(image.getNoisedMatrix());
      int l = image.getNoisedMatrix().length;
      int c = image.getNoisedMatrix()[1].length;

      //Etape 2 : Transformation des patchs en vecteurs
      int[][] matricePatchs = image.vectorPatch(listePatch);

      //Etape 3 : On réalise l'ACP sur les patchs vectorisés
      ACP acp = new ACP(matricePatchs);
      acp.MoyCov();
      acp.DoACP();
      acp.Proj();
      double[][] alpha = acp.getVcontrib();
      double[] meanVector = acp.getMoyCov();
      double[][] u = acp.getU();
 
      
      //Etape 4 : Seuillage des vecteurs de contribution
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

      //Etape 5 : Calcul des patchs vectorisés dans la nouvelle base
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
         
         //On a réalisé l'ACP sur des doubles, maintenant pour revenir au code RGB, on a besoin d'entiers
         for (int indexC = 0; indexC<patchMatrixEntier.length; indexC++) {
            for (int indexL = 0; indexL<patchMatrixEntier[0].length; indexL++) {
               patchMatrixEntier[indexC][indexL] = (int) patchMatrixDouble[indexC][indexL];      
            }    
         }
         patch.setMatrix(patchMatrixEntier);
         listePatch.set(k, patch);
      }

      //Etape 6 : Assemblage des patchs pour obtenir l'image débruitée
      int[][] assemblerMatrice = image.assemblagePatch(listePatch, l, c);
      return assemblerMatrice;
   }
}


