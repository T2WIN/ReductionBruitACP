package src.main;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Image {

    //Matrice de l'image
    private ArrayList<ArrayList<Integer>> matrix;

    //Variance du bruit ajouté à l'image
    private int sigma;
    
    public Image(String path, int sigma) {
        this.matrix = createMatrix(path);
        this.sigma = sigma;
    }

    //Ajoute un bruit gaussien de variance sigma
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

    //Créer une matrice à partir d'un fichier image
    public static ArrayList<ArrayList<Integer>> createMatrix(String path) {
        File fileSelected = new File(path);
        System.out.println("file to be opened : " + fileSelected);

        BufferedImage image;
        
        ArrayList<ArrayList<Integer>> tabImage = new ArrayList<ArrayList<Integer>>();
        
        try {
            image = ImageIO.read(fileSelected);
            int x;
            int y;
            x=image.getWidth();
            y=image.getHeight();
            // Parcours de l'intégralité des pixels 
            // Puis création de la matrice associée à l'image
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    // Transformation des pixels en couleur en gris
                    tabImage.get(i).add(calcGreyValue(image, i, j));
                    System.out.println(tabImage.get(i).get(j));
                }
            }
            return tabImage;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Le fichier est inconnu au bataillon");
            e.printStackTrace();
            System.exit(-1);
        }
        return tabImage;
    }

    // Transformation des pixels en couleur en gris
    public static int calcGreyValue(BufferedImage image, int i, int j) {
        Color pixel = new Color(image.getRGB(i, j));
        int r= pixel.getRed();
        int g=pixel.getGreen();
        int b=pixel.getBlue();
        int gris = (r+g+b)/3;
        return gris;
    }

    public BufferedImage createImageFromMatrix(ArrayList<ArrayList<Integer>> matrix) {
        BufferedImage image = new BufferedImage(null, null, false, null);
        try {
            
            for(int i=0; i<matrix.size(); i++) {
                for(int j=0; j<matrix.get(0).size(); j++) {
                    int a = matrix.get(i).get(j);
                    Color newColor = new Color(a,a,a);
                    image.setRGB(j,i,newColor.getRGB());
                }
            }
        }
        
        catch(Exception e) {
            System.out.println(("Error creating image"));
            e.printStackTrace();
        }
        return image;
    }

    public void createfile(BufferedImage image) {
        File output = new File("GrayScale.jpg");
        try {
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
