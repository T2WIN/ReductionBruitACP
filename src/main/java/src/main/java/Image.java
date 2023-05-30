package src.main.java;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
public class Image {

    //Matrice de l'image
    private int[][] matrix;
    private int[][] noisedmatrix;
    int s;

    //Variance du bruit ajouté à l'image
    private Integer sigma;

    //Constructeur utilisant directement le chemin d'un fichier image
    public Image(String path, int sigma, int s) {
        this.matrix = createMatrix(path);
        this.sigma = sigma;
        this.s = s;
    }

    //Constructeur utilisant les matrices de représentation bruitée et non bruitée et la taille des patchs pour initialiser l'image
    public Image(int[][] matrix,int[][] noisedmatrix, int taille){
        this.matrix = matrix;
        this.noisedmatrix = noisedmatrix;
        this.s = taille;
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

    //Créer une matrice à partir d'un fichier image
    public static int[][] createMatrix(String path) {
        File fileSelected = new File(path);
        BufferedImage image;
        
        try {
            
            image = ImageIO.read(fileSelected);
            int x = image.getWidth();
            int y = image.getHeight();
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

    //Utilise la matrice décrivant l'image dégradée pour la retourner
    public BufferedImage createImageFromMatrix() {
        BufferedImage image = new BufferedImage(this.noisedmatrix.length, this.noisedmatrix[0].length, BufferedImage.TYPE_INT_RGB);
        try {
            
            for(int i=0; i<this.noisedmatrix.length; i++) {
                for(int j=0; j<this.noisedmatrix[0].length; j++) {
                    int a = this.noisedmatrix[i][j];
                    try {
                        Color newColor = new Color(a,a,a);
                        image.setRGB(i,j,newColor.getRGB());
                    } catch (Exception as) {
                        
                    }
                    
                }
            }
        }
        
        catch(Exception e) {
            System.out.println(("Error creating image"));
            e.printStackTrace();
        }
        return image;
    }



    //Extraction des patchs à partir de la matrice de l'image
    public ArrayList<Patch> extractionPatch(int[][] matrix){
        //Nombre de vecteurs de la matrice
        int x= matrix.length;
        //Taille des vecteurs de la matrice
        int y= matrix[1].length;
        int coint_sup_x;
        int coint_sup_y;
        ArrayList<Patch> ListePatch= new ArrayList<Patch>();


        for (int i = 0; i < x-s+1; i++) {
            for (int j = 0; j < y-s+1; j++) {
                // Position du pixel du coin gauche 
                int [][] patchcourant = new int[s][s];
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
        return ListePatch;
    }

    public int[][] assemblagePatch(ArrayList<Patch> ListePatch,int l,int c){
        int [][] imageRecon = new int[l][c];
        int [][] matricePoids = new int [l][c]; //Matrice qui contient le nombre de patchs auquel appartient chaque pixel
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < c; j++) {
                imageRecon[i][j]=0;
                matricePoids[i][j]=0;
            }
        }

        int cointx;
        int cointy;

        // Ajout des patchs dans la matrice 
        for (int k = 0; k < ListePatch.size(); k++) {
            cointx = ListePatch.get(k).getPositionX();
            cointy = ListePatch.get(k).getPositionY();
            int[][] patch = ListePatch.get(k).getMatrix();
            
            for (int n = 0; n < s; n++) {
                for (int m = 0; m < s; m++) {
                    // Superposition des patchs dans la matrice
                    imageRecon[n + cointx][m + cointy] += patch[n][m];
                    // On compte le nombre de patchs ayant été superposés pour ce pixel
                    matricePoids[cointx + n][cointy + m] += 1;
                }
            }
        }
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < c; j++) {
                //La valeur RGB de chaque pixel est la moyenne de la valeur de tous les patchs s'étant superposés en ce pixel
                imageRecon[i][j] = imageRecon[i][j]/matricePoids[i][j];
            }
        }
        
        return imageRecon;
    }

    public static BufferedImage createImageFromMatrix(int[][] matrix) {
        //On crée une BufferedImage qui pourra ensuite être transformée en fichier
        BufferedImage image = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_INT_RGB);
            for(int i=0; i<matrix.length; i++) {
                for(int j=0; j<matrix[0].length; j++) {
                    int a = matrix[i][j];
                    try {
                        //Trois fois le même coef pour les couleurs RGB car cela donne un degré de gris
                        Color newColor = new Color(a,a,a);
                        image.setRGB(i,j,newColor.getRGB());
                    } catch(Exception e) {
                    }
                }
            }
        
        
        return image;
    }

    //Utilise un objet BufferedImage pour retourner l'image sous forme de fichier jpg
    public static void createfile(BufferedImage image, String nom) {
        File output = new File("./src/main/img/" + nom + ".jpg");
        try {
            output.createNewFile();
            ImageIO.write(image, "jpg", output);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //Transformation de la liste de patch en une matrice donc chaque ligne correspond à 1 patch sous forme de vecteur
    public int[][] vectorPatch(ArrayList<Patch> listePatch) {
        int[][] matrixPatchs = new int[listePatch.size()][listePatch.get(0).vectorize().length];
        for (int i =0; i < listePatch.size(); i++) {
            int[] vector = listePatch.get(i).vectorize();
            for (int j=0; j<listePatch.get(0).vectorize().length; j++) {
                matrixPatchs[i][j] = vector[j];
            }
        }
        return matrixPatchs;
    }

    //On extrait les imagettes de l'image de manière aléatoire
    public ArrayList<Patch> extractImagettes(int[][] X, int W, int n) {
        // Récupérer la taille de l'image
        int x = X.length;
        int y = X[0].length;

        ArrayList<Patch> ListeImagette = new ArrayList<Patch>();
        Random random = new Random();

        // Extraire les imagettes
        for (int i = 0; i < n; i++) {
            // Générer des coordonnées aléatoires pour l'origine de l'imagette
            int cointx = random.nextInt(x - W + 1);
            int cointy = random.nextInt(y - W + 1);

            // Extraire l'imagette à partir de l'image d'intérêt
            int[][] imagette = new int[W][W];
            for (int j = 0; j < W; j++) {
                System.arraycopy(X[cointx + j], cointy, imagette[j], 0, W);
            }
    
            // Ajouter l'imagette et ses coords dans liste imagette
            Patch patch = new Patch(imagette, cointx, cointy);
            ListeImagette.add(patch);

            
        }

        return ListeImagette;
    }

    //On réassemble les imagettes pour obtenir notre image débruitée
    public int[][] assemblageImagette(ArrayList<Patch> ListeImagette, int[][] imageDepart, int l,int c, int W){
        
        int [][] imageRecon = new int[l][c];
        int [][] matricePoids = new int [l][c]; 
        int cointx;
        int cointy;

        // Ajout des patchs dans la matrice 
        for (int k = 0; k < ListeImagette.size(); k++) {
            cointx = ListeImagette.get(k).getPositionX();
            cointy = ListeImagette.get(k).getPositionY();
            int[][] patch = ListeImagette.get(k).getMatrix();
            
            for (int n = 0; n < W; n++) {
                for (int m = 0; m < W; m++) {
                    // Superposition des patchs dans la matrice
                    imageRecon[n + cointx][m + cointy] += patch[n][m];
                    // Ajout du poids pour chaque élément de la matrice
                    matricePoids[cointx + n][cointy + m] += 1;
                }
            }
        }
        for (int i = 0; i < l; i++) {
            for (int j = 0; j < c; j++) {
                //Si le pixel a été traité dans une des imagette
                if (matricePoids[i][j] >= 1) {
                    imageRecon[i][j] = imageRecon[i][j]/matricePoids[i][j];
                //S'il ne faisait partie d'aucune imagette, on le remplace par le pixel bruité
                } else {
                    imageRecon[i][j] = imageDepart[i][j];
                }
            }
        }
        
        return imageRecon;
    }

    public int getSigma() {

        return this.sigma;
    }

    public void setSigma(Integer sigma) {

        this.sigma = sigma;
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
}