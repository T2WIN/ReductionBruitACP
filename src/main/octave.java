package src.main;


import java.util.ArrayList;
import java.util.List;

public class octave {
    public static List<Image> DecoupeImage(Image X, int W, int n){

        List<Image> imagettes = new ArrayList<>();
        List<int[][]> imagettesPositions = new ArrayList<>();

        int[][] Tab = X.getMatrix();

        int imagettesEnLargeur = Tab.length / W;
        int imagettesEnHauteur = Tab[0].length / W;

        int nombreTotalImagettes = imagettesEnLargeur * imagettesEnHauteur;

        if (nombreTotalImagettes > n){
            throw new IllegalArgumentException("Nombre d'imagettes demandé supérieurau nombre d'imagettes disponibles.");
            return imagettes;
        }

        for ( int i = 0 ; i < imagettesEnHauteur ; i++ ){
            for ( int j = 0 ; j < imagettesEnLargeur ; j++){
                int x = i * W;
                int y = j * W;

                Image imagette = getSubimage(x,y,W,X);

                imagettes.add(imagette);
                int[][] position =new int[x][y];
                imagettesPositions.add(position);
            }
        }
        return imagettes;
    }  
    
    public static Image getSubImage(int x, int y, int W, Image X){

        int[][] Tab = createMatrix(X);
        int[][] imagettesMatrice;
        Image imagettes;

        for ( int i = 0 ; i < W ; i++){
            for ( int j = 0 ; j < W ; j++){
                
                imagettesMatrice[i][j] = Tab[x+i][y+j];
            }
        imagettes = createImageFromMatrix(Tab);
        return(imagettes);
    }

    }
}
