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
    }

    @FXML
    private void fillImage() throws IOException {
        this.image = new Image("demo/src/main/java/com/example/lena.jpg", Integer.parseInt(choiceSigma.getValue()),  Integer.parseInt(outputText.getText()));
        this.image.noising();
    }

    private void MethodeGlobale() {

        //Récupération de la matrice de l'image bruitée
        int[][] imageBruité = this.image.getNoisedMatrix();
        int l=imageBruité.length;
        int c=imageBruité[1].length;

        //Transformation de la matrice bruitée en matrice de patchs vectorisés
        ArrayList<Patch> listePatch = this.image.extractionPatch(image.getNoisedMatrix());
        int[][] matricePatchs = image.vectorPatch(listePatch);
        

        //Réalisation des calculs de l'ACP
        ACP acp = new ACP(matricePatchs);
        acp.MoyCov();
        acp.DoACP();
        acp.Proj();
        
        //Seuillage des vecteurs de contribution
        double[][] alpha = acp.getVcontrib();
        Seuillage seuillage = new Seuillage(this.image);

        if (choiceSeuil.getValue().equals("VisuShrink")) {

            double  threshold = seuillage.getVisuShrink();

            if (choiceSeuillage.getValue().equals("Seuillage Dur")) {

                for (int i = 0; i < alpha.length; i++) {

                    for (int j = 0; j < alpha[1].length; j++) {

                    double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha[i][j]);
                    alpha[i][j] = hardThresholdingResult;

                    }
                }
            }
            if (choiceSeuillage.getValue().equals("Seuillage Doux")) {

                for (int i = 0; i < alpha.length; i++) {

                    for (int j = 0; j < alpha[1].length; j++) {

                    double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha[i][j]);
                    alpha[i][j] = softThresholdingResult;

                    }
            
                }
            }
        }

        if (choiceSeuil.getValue().equals("BayesShrink")) {

            double  threshold = seuillage.getBayesShrink();

            if (choiceSeuillage.getValue().equals("Seuillage Dur")) {

                for (int i = 0; i < alpha.length; i++) {

                    for (int j = 0; j < alpha[1].length; j++) {

                    double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha[i][j]);
                    alpha[i][j] = hardThresholdingResult;

                    }
                }
            }
            if (choiceSeuillage.getValue().equals("Seuillage Doux")) {

                for (int i = 0; i < alpha.length; i++) {

                    for (int j = 0; j < alpha[1].length; j++) {

                    double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha[i][j]);
                    alpha[i][j] = softThresholdingResult;
                    

            
                }
            }
            
        }

    }
    for (int i = 0; i < listePatch.size(); i++) {
        int x;
        int y;
        System.out.println(alpha.length);
        System.out.println(alpha[0].length);
        x = listePatch.get(i).positionX;
        y = listePatch.get(i).positionY;
        int[][] matPatch = listePatch.get(i).matrix;
        Patch patch = new Patch(matPatch,x,y);
        int[][] patchVect = new int[alpha.length][alpha[0].length];
        for (int k = 0; k < alpha.length; k++){
            for (int j = 0; j < alpha[1].length; j++ ) {
                patchVect[k][j] = (int) alpha[k][j];
                System.out.println(alpha[k][j]);
            }
        }
        matPatch = patch.intoMatrix(patchVect[i]);
        patch.setMatrix(patchVect);
        listePatch.set(i, patch);
    }
    int[][] assemblerMatrice = image.assemblagePatch(listePatch, l, c);
    BufferedImage imagefinale = image.createImageFromMatrix(assemblerMatrice);
    Image.createfile(imagefinale, "imagefinale");
    }
}
