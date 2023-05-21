package src.main;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;
public class Image {

    //Matrice de l'image
    private int[][] matrix;
    private int[][] noisedmatrix;

    //Variance du bruit ajouté à l'image
    private Integer sigma;

    public int getSigma() {

        return this.sigma;
    }

     public void setSigma(Integer sigma) {

        this.sigma = sigma;
    }

    
    public Image(String path, int sigma) {
        this.matrix = createMatrix(path);
        this.sigma = sigma;
    }

    //Ajoute un bruit gaussien de variance sigma
    public void noising() {
        this.noisedmatrix = new int[this.matrix.length][this.matrix[0].length];
        Random rand = new Random(123);
        for (int i=0; i<this.matrix.length; i++) {
            for (int j = 0; j<this.matrix[0].length; j++) {
                int coef = this.matrix[i][j];
                this.noisedmatrix[i][j] = Math.abs((coef + (int) (rand.nextGaussian()*sigma))%255); 
            }
        }
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] getNoisedMatrix() {
        return this.noisedmatrix;
    }

    public void setNoisedMatrix(int[][] matrix) {
        this.noisedmatrix = matrix;
    }

    //Créer une matrice à partir d'un fichier image
    public static int[][] createMatrix(String path) {
        File fileSelected = new File(path);
        System.out.println("file to be opened : " + fileSelected);

        BufferedImage image;
        
        
        try {
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
                    tabImage[i][j] = calcGreyValue(image, i, j);
                }
            }
            return tabImage;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Le fichier est inconnu au bataillon");
            e.printStackTrace();
            System.exit(-1);
        }

        int[][] default2 = new int[1][1];
        return default2;
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

    public BufferedImage createImageFromMatrix() {
        BufferedImage image = new BufferedImage(this.noisedmatrix.length, this.noisedmatrix[0].length, BufferedImage.TYPE_INT_RGB);
        try {
            
            for(int i=0; i<this.noisedmatrix.length; i++) {
                for(int j=0; j<this.noisedmatrix[0].length; j++) {
                    int a = this.noisedmatrix[i][j];
                    Color newColor = new Color(a,a,a);
                    image.setRGB(i,j,newColor.getRGB());
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

    public ArrayList<Patch> extractionPatch(int[][] matrix){

        int x= matrix.length;
        int y= matrix[1].length;
        Scanner sc;
		sc = new Scanner(System.in);
		System.out.print("Saisir l'entier s, taille du patch : ");
		int s;
		s = sc.nextInt(); 
        
        int [][] patchcourant = new int[s][s];
        int coint_sup_x;
        int coint_sup_y;
        ArrayList<Patch> ListePatch= new ArrayList<Patch>();


        for (int i = 0; i < x-s+1; i++) {
            for (int j = 0; j < y-s+1; j++) {
                // Position du pixel du coin gauche 
                coint_sup_x= i;
                coint_sup_y= j;
                // Création d'un patch à partir de chaque pixel
                Patch patch = new Patch(patchcourant,coint_sup_x,coint_sup_y);
                // Ajout des pixels de l'image dans chaque patch
                for (int k = coint_sup_x; k < coint_sup_x + s;k++){
                    for (int l = coint_sup_y; l < coint_sup_y + s; l++){
                        patchcourant[k - coint_sup_x][l - coint_sup_y]=matrix[k][l];
                    }
                }
                ListePatch.add(patch);
            }   
        }
        sc.close();
        return ListePatch;
    }
}
