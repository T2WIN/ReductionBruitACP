package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
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
    }

    @FXML
    private void fillImage() throws IOException {
        this.image = new Image("demo/src/main/resources/com/example/lena.jpg", Integer.parseInt(choiceSigma.getValue()),  Integer.parseInt(outputText.getText()));
        System.out.println(this.image.getSigma());
    }
}
