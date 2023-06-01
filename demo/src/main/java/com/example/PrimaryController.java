package com.example;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

public class PrimaryController {
    
    @FXML
    private ChoiceBox<String> choiceSigma;

    @FXML
    private TextArea outputText;

    @FXML
    private ChoiceBox<String> choiceSeuil;
    
    @FXML
    private ChoiceBox<String> choiceSeuillage;

   @FXML
    private Image image;
    @FXML
    private VBox vbox; 

    @FXML
    private VBox vbox1; 

    @FXML
    private ChoiceBox<String> choiceMethode;

    @FXML
    private TextArea W = new TextArea("");
    
    @FXML
    private TextArea n = new TextArea("");

    @FXML
    private Label text1 = new Label("Donnez la taille des imagettes :");
    @FXML
    private Label text2 = new Label("Donnez le nombre d'imagettes :");

    @FXML
    Label ErrorLabel1 = new Label("");
    @FXML
    Label ErrorLabel2 = new Label("");

    @FXML
    private void switchToSecondary() throws IOException {
        fillImage();
        App.setRoot("secondary");
        if(choiceMethode.getValue().equals("Global")) {
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


         BufferedImage imagefinale = Image.createImageFromMatrix(assemblerMatrice);

         this.image.setMatrix(assemblerMatrice);
         this.image.error1 = error.PeakSignalToNoiseRatio();
         this.image.error2 = error2.PeakSignalToNoiseRatio();

         Image.createfile(imagefinale,"global" + "_sigma:" + image.getSigma() + "_patch:" + image.getS() + "_seuil:" + choixSeuil + "_seuillage:" + choixSeuillage);
         Image.createfile(imagefinale, "result");
        } else {
         
         int imagetteSize = Integer.parseInt(W.getText());
         int imagetteAmount = Integer.parseInt(n.getText());

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

         int [][] imageBruit = image.getNoisedMatrix();

         //On sépare l'image de départ en n imagettes de taille W
         ArrayList<Patch> listeImagette = image.extractImagettes(image.getMatrix(), imagetteSize, imagetteAmount);
         ArrayList<Patch> listeImagetteBruité = image.extractImagettes(imageBruit, imagetteSize, imagetteAmount);

         int [][] imagette = new int[imagetteSize][imagetteSize];
         int [][] imagetteBruité = new int[imagetteSize][imagetteSize];
         for (int i = 0; i < listeImagetteBruité.size(); i++) {
            imagette = listeImagette.get(i).getMatrix();
            imagetteBruité = listeImagetteBruité.get(i).getMatrix();
            Image objImagette = new Image(imagette, imagetteBruité, image.getS());
            //On applique la méthode globale aux n imagettes bruitées
            imagetteBruité = methodeGlobale(objImagette, seuillage, image.getS(), choixSeuil, choixSeuillage);
            listeImagetteBruité.get(i).setMatrix(imagetteBruité);
         }
         int l = imageBruit.length;
         int c = imageBruit[0].length;
         //On rassemble ensuite les n imagettes débruitées pour obtenir l'image finale
         int[][] imageRecon = image.assemblageImagette(listeImagetteBruité, imageBruit, l,c, imagetteSize);

         Error error = new Error(image, new Image(imageBruit, imageBruit, image.getS()));
         Error error2 = new Error(image, new Image(imageRecon, imageRecon, image.getS()));
         
         BufferedImage imagefinale = Image.createImageFromMatrix(imageRecon);
         this.image.setMatrix(imageRecon);
         this.image.error1 = error.PeakSignalToNoiseRatio();
         this.image.error2 = error2.PeakSignalToNoiseRatio();

         Image.createfile(imagefinale,"local" + "_sigma:" + image.getSigma() + "_patch:" + image.getS() + "_seuil:" + choixSeuil + "_seuillage:" + choixSeuillage + "_imagetteS:" + imagetteSize + "_imagetteA:" + imagetteAmount);
         Image.createfile(imagefinale, "result.jpg");
      }
        
    }

    @FXML
    private void fillImage() throws IOException {
        this.image = new Image("demo/src/main/java/com/example/lena.jpg", Integer.parseInt(choiceSigma.getValue()),  Integer.parseInt(outputText.getText()));
        this.image.noising();
        vbox1.getScene().getWindow().setUserData(image);
    }

    @FXML 
    private void updatePrimary() {
      
      if(choiceMethode.getValue().equals("Local") && vbox1.getChildren().size() <= 18) {
         this.W.setMaxWidth(30);
         this.n.setMaxWidth(30);
         vbox1.getChildren().add(17, this.text1);
         vbox1.getChildren().add(18, this.W);
         vbox1.getChildren().add(19,this.text2);
         vbox1.getChildren().add(20, this.n);
      } else if (choiceMethode.getValue().equals("Global")) {
         vbox1.getChildren().remove(this.W);
         vbox1.getChildren().remove(this.n);
         vbox1.getChildren().remove(this.text1);
         vbox1.getChildren().remove(this.text2);

      }
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

     @FXML
     private void switchToPrimary() throws IOException {
         App.setRoot("primary");
     }
 

     @FXML
     public void displayImage(){

     if(vbox.getChildren().size() == 3) {
      Stage stage = (Stage) vbox.getScene().getWindow();
      Image img = (Image) stage.getUserData();

      int sigma = img.getSigma();
      javafx.scene.image.Image image = new javafx.scene.image.Image("file:sigma" + sigma + ".jpg");
      ImageView imageView = new ImageView();
      imageView.setImage(image);
     
      imageView.setFitWidth(300);
      imageView.setFitHeight(300);
   
      javafx.scene.image.Image image2 = new javafx.scene.image.Image("file:result.jpg");
      ImageView imageView2 = new ImageView();
      imageView2.setImage(image2);

      this.ErrorLabel1.setText("PSNR : " + img.error1);
      this.ErrorLabel2.setText("PSNR : " + img.error2);
      
      imageView2.setFitWidth(300);
      imageView2.setFitHeight(300);
      vbox.getChildren().add(imageView);
      vbox.getChildren().add(ErrorLabel1);
      vbox.getChildren().add(imageView2);
      vbox.getChildren().add(ErrorLabel2);
     }
     
 }
}
