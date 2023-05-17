import java.util.ArrayList;
import java.lang.Math;

public class Error{

    public float MeanSquaredError( Image X , Image Y ){
        int MSE = 0;
        
        for ( int i = 0 ; i < X.size ; i++){
            for ( int j = 0 ; j < X[i].size ; j++){
                MSE = MSE + X.get(i).get(j) - Y.get(i).get(j);
            }
        }
        MSE = MSE / (X.size*X[0].size);
        return(MSE);
    }

    public float PeakSignalToNoiseRatio(Image X , Image Y){
        int PSNR = 10*(Math.log(255/this.MeanSquaredError(Image X, Image Y))/Math.log(10));
        return(PSNR);
    }
    
}

