package src.main;
import java.util.ArrayList;
import java.io.File;
import java.util.Random;

public class Image {
    private ArrayList<ArrayList<Integer>> matrix;
    private int sigma;
    
    public Image(File file, int sigma) {
        // this.matrix = createMatrix(file);
        this.sigma = sigma;
    }

    public void noising() {
        Random rand = new Random(123);
        for (int i=0; i<this.matrix.size(); i++) {
            for (int j = 0; j<this.matrix.get(0).size(); j++) {
                int coef = this.matrix.get(i).get(j);
                this.matrix.get(i).set(j, coef + floor(rand.nextGaussian()*sigma)); 
            }
        }
    }

    private int floor(double d) {
        return 0;
    }

    public ArrayList<ArrayList<Integer>> getMatrix() {
        return this.matrix;
    }

    public void setMatrix(ArrayList<ArrayList<Integer>> matrix) {
        this.matrix = matrix;
    }

}
