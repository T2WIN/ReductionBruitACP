package src.main;

import java.util.ArrayList;
import java.lang.Math;

public class Error{
    public Error() {
        Image test1 = new Image()
        int[][] X = createMatrix(Image test1.jpg);
        int[][] Y = createMatrix(Image lenaa.png);

    }

    public float MeanSquaredError( int[][]  X , int[][]  Y ){
        int MSE = 0;
        
        for ( int i = 0 ; i < X.getWidth() ; i++){
            for ( int j = 0 ; j < X.getHeight() ; j++){
                MSE = MSE + X.get(i).get(j) - Y.get(i).get(j);
            }
        }
        MSE = MSE / (X.size*X[0].size);
        return(MSE);
    }

    public float PeakSignalToNoiseRatio(Image X , Image Y){
        float mse = MeanSquaredError(X,Y);
        float PSNR = 10* (float) (Math.log10(255*255 / mse));
        return(PSNR);
    }

    
}

