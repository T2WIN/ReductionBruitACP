package com.example;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    private VBox vbox; 

    public void displayImage(){
    Image image = new Image("file:demo/src/main/java/com/example/lena.jpg");
    ImageView imageView = new ImageView();
    imageView.setImage(image);
    
    imageView.setFitWidth(300);
    imageView.setFitHeight(300);
    
    vbox.getChildren().add(imageView);
}
}