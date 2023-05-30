package com.example;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class PrimaryController {
    
    @FXML
    private ChoiceBox<String> choiceSigma;
    @FXML
    private TextArea outputText;

    @FXML
    private ChoiceBox<String> choiceSeuil;
    
    @FXML
    private ChoiceBox<String> choiceSeuillage;

    private Image image;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
        fillImage();
        Seuillage seuillage = new Seuillage(this.image);
        int choixSeuil = -1;
        int choixSeuillage = -1;
        if (choiceSeuil.getValue().equals("VisuShrink")) {
            choixSeuil = 1;
        } else if (choiceSeuil.getValue().equals("BayesShrink")) {
            choixSeuil = 2;
        }

        if (choiceSeuillage.getValue().equals("Seuillage dur")) {
            choixSeuillage = 1;
        } else if (choiceSeuillage.getValue().equals("Seuillage doux")) {
            choixSeuillage = 2;
        }
        
        int[][] assemblerMatrice = methodeGlobale(this.image, seuillage, image.getS(), choixSeuil, choixSeuillage);
        Error error = new Error(image, new Image(image.getNoisedMatrix(), image.getNoisedMatrix(), image.getS()));
        Error error2 = new Error(image, new Image(assemblerMatrice, assemblerMatrice, image.getS()));

        System.out.println("Erreur de départ");
        System.out.println(error.MeanSquaredError());
        System.out.println(error.PeakSignalToNoiseRatio());
        System.out.println("Erreur de fin");
        System.out.println(error2.MeanSquaredError());
        System.out.println(error2.PeakSignalToNoiseRatio());
        BufferedImage imagefinale = Image.createImageFromMatrix(assemblerMatrice);
        Image.createfile(imagefinale,"global");
    }

    @FXML
    private void fillImage() throws IOException {
        this.image = new Image("demo/src/main/java/com/example/lena.jpg", Integer.parseInt(choiceSigma.getValue()),  Integer.parseInt(outputText.getText()));
        this.image.noising();
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
