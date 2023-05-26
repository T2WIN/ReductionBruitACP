package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class PrimaryController {
    
    @FXML
    private ChoiceBox<String> choiceSigma;
    private Image image;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void fillImage() throws IOException {
        this.image = new Image("", int sigma, int s);
    }

    @FXML
    private void initialiseImage() {

    }
}
