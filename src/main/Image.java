package src.main;
import java.util.ArrayList;
import java.io.File;
import java.util.Random;
import java.awt.image.BufferedImage;

public class Image {
    private ArrayList<ArrayList<Integer>> matrix;
    private int sigma;
    
    public Image(String path, int sigma) {
        this.matrix = createMatrix(path);
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

    public static void createMatrix(String path) {
        File fileSelected = new File(path);
        System.out.println("file to be opened :" + fileSelected);

        BufferedImage image;

        image = ImageIO.read(fileSelected);
        int x;
        int y;
        x=image.getWidth();
        y=image.getHeight();
        int [][] tabImage = new int[x][y];
        // Parcours de l'intégralité des pixels 
        // Puis création de la matrice associée à l'image
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                // Transformation des pixels en couleur en gris
                Color pixel = new Color(image.getRGB(i, j));
                int r= pixel.getRed();
                int g=pixel.getGreen();
                int b=pixel.getBlue();
                int gris = (r+g+b)/3;
                tabImage[i][j]=gris;
                System.out.println(tabImage[i][j]);
            }
        }
    }

}
