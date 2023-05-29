package src.main.java;

import java.lang.Math;

public class Error{
    
    //Matrices représentant les images sur lesquels on calcule l'erreur
    int [][] matrice1;
    int [][] matrice2;

    public Error(Image test1, Image test2) {
        this.matrice1 = test1.getMatrix();
        this.matrice2 = test2.getMatrix();
    }

    public float MeanSquaredError(){

        float MSE = 0;
        for ( int i = 0 ; i < this.matrice1.length ; i++){
            for ( int j = 0 ; j < this.matrice1[0].length ; j++){
                MSE = MSE + (this.matrice1[i][j] - this.matrice2[i][j])*(this.matrice1[i][j] - this.matrice2[i][j]);
            }
        }

        MSE = MSE / (matrice1.length*this.matrice1[0].length);
        return(MSE);
    }

    public float PeakSignalToNoiseRatio(){
        float mse = MeanSquaredError();
        float PSNR = 10* (float) (Math.log10(255*255 / mse));
        return(PSNR);
    }

    
}

