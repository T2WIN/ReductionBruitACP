package src.main;


import java.util.ArrayList;
import java.util.List;

public class octave {
    public static List<Image> DecoupeImage(Image X, int W, int n){

        int imageWidth = X.getWidth();
        int imageHeight = X.getheight();
        int numCols = imageWidth / W;
        int numRows = imageHeight / W;

        if (numCols * numRows < n){
            throw new IllegalArgumentException("Nombre d'imagettes demandé supérieurau nombre d'imagettes disponibles.");
        }

        List<Image> imagettes = new ArrayList<>();


        for (int i = 0 ; i < n ; i++ ){
            int colIndex = i % numCols;
            int rowIndex = i / numCols;
            
            WritableImage imagette=new WritableImage(X.getPixelReader(), colIndex * W, rowIndex * W, W, W);
            









        }
    }       
}
