package src.main.java;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

import java.util.Arrays;

public class ACP {

    double [][] vectorisePatchs;
    double [] meanVector;
    double [][] centeredVectors;
    double [][] covariance;
    double [] eigenValues;
    double [][] U;
    double [][] Vcontrib;

    public ACP(int[][] int_vectorisePatchs) {
        // Conversion : int [][]  -->  double [][]
        vectorisePatchs = new double [int_vectorisePatchs.length][int_vectorisePatchs[0].length];
        for (int i=0; i<vectorisePatchs.length; i++) {
            for (int j=0; j<vectorisePatchs[0].length; j++) {
                vectorisePatchs[i][j] = Double.valueOf(int_vectorisePatchs[i][j]);
            }
        }

        meanVector = new double [vectorisePatchs[0].length];
        covariance = new double [vectorisePatchs[0].length][vectorisePatchs[0].length];
        centeredVectors = new double [vectorisePatchs.length][vectorisePatchs[0].length];
        U = new double [vectorisePatchs[0].length][vectorisePatchs[0].length];
        Vcontrib = new double [vectorisePatchs.length][vectorisePatchs[0].length];
    }

    // convertir une matrice en string pour l'afficher
    public static String[][] getStrings(double[][] a) {
        String[][] output = new String[a.length][];
        int i = 0;
        for (double[] d : a){
            output[i++] = Arrays.toString(d).replace("[", "").replace("]", "").split(",");
        }
        return output;
    }

    public double[][] getVcontrib() {
        return this.Vcontrib;
    }
    public void afficherResultat() {
        System.out.println("\nPatchs vectorisés :");
        for (String[] s : getStrings(vectorisePatchs)){
            System.out.println(Arrays.toString(s));
        }
        System.out.println("\nVecteur moyen :");
        System.out.println(Arrays.toString(meanVector));
        System.out.println("\nVecteurs centrés :");
        for (String[] s : getStrings(centeredVectors)){
            System.out.println(Arrays.toString(s));
        }
        System.out.println("\nMatrice de covariance :");
        for (String[] s : getStrings(covariance)){
            System.out.println(Arrays.toString(s));
        }
        System.out.println("\nValeurs propres :");
        System.out.println(Arrays.toString(eigenValues));
        System.out.println("\nPourcentages d'inertie :");
        double[] pourcent = new double[eigenValues.length];
        double somme = 0.0;
        for (int i=0; i<eigenValues.length; i++) {
            somme += eigenValues[i];
        }
        for (int i=0; i<eigenValues.length; i++) {
            pourcent[i] = eigenValues[i] / somme;
        }
        System.out.println(Arrays.toString(pourcent));
        System.out.println("\nBase orthonormale (vecteurs propres) :");
        for (String[] s : getStrings(U)){
            System.out.println(Arrays.toString(s));
        }
        System.out.println("\nVecteurs de contribution :");
        for (String[] s : getStrings(Vcontrib)){
            System.out.println(Arrays.toString(s));
        }
    }
    
    public void MoyCov() {

        // Déterminer le vecteur moyen
        // parcours des axes
        for (int j=0; j<meanVector.length; j++) {
            meanVector[j] = 0.0;
            // parcours de la liste des vecteurs
            for (int i=0; i<vectorisePatchs.length; i++) {
                // additions de toute les valeurs
                meanVector[j] += vectorisePatchs[i][j];
            }
            // division par le nombre de vecteurs pour faire la moyenne de chaque axe
            meanVector[j] = meanVector[j] / vectorisePatchs.length;
        }

        // Centrer les vecteurs
        // parcours de la liste des vecteurs
        for (int i=0; i<vectorisePatchs.length; i++) {
            // parcours des axes
            for (int j=0; j<vectorisePatchs[0].length; j++) {
                // calcul pour centrer le vecteur
                centeredVectors[i][j] = vectorisePatchs[i][j] - meanVector[j];
            }
        }

        // Déterminer la matrice de covariance
        // taille des vecteurs
        int N = vectorisePatchs[0].length;
        double somme;
        // parcours de la liste des variables (les axes)
        for (int i=0; i<N; i++) {
            // 2ème parcours de la liste des variables (on ne remplit que la partie supérieure de la matrice car elle est symétrique)
            for (int j=0; j<i+1; j++) {
                // initialisation de la somme (pour le calcul de la covariance entre les variables i et j)
                somme = 0.0;
                // parcours des valeurs des vecteurs sur l'axe
                for (int k=0; k<vectorisePatchs.length; k++) {
                    somme += centeredVectors[k][i] * centeredVectors[k][j];
                }
                // calcul de la matrice de covariance 
                covariance[i][j] = somme / vectorisePatchs.length;
                // remplissage de la partie inférieure de la matrice par symétrie
                if (i!=j) {
                    covariance[j][i] = covariance[i][j];
                }
            }
        }

    }

    public void DoACP() {
        
        // Création de la matrice de covariance
        RealMatrix covarianceMatrix = MatrixUtils.createRealMatrix(covariance);

        // Calcul des valeurs propres et des vecteurs propres
        EigenDecomposition eigenDecomposition = new EigenDecomposition(covarianceMatrix);
        eigenValues = eigenDecomposition.getRealEigenvalues();
        RealMatrix eigenVectors = eigenDecomposition.getV();

        // Conversion : RealMatrix  -->  double [][]
        U = eigenVectors.getData();

        // On vérifie si la base est bien orthonormale (pour les tests)
        int N = vectorisePatchs[0].length;
        double [][] produitScalaire = new double [N][N];
        double somme;
        // parcours de la liste des vecteurs de la base
        for (int i=0; i<N; i++) {
            // 2ème parcours de la liste des vecteurs (on ne remplit que la partie supérieure de la matrice car le produit scalaire est symétrique)
            for (int j=0; j<i+1; j++) {
                // initialisation de la somme (pour le calcul du produit scalaire entre les vecteurs i et j)
                somme = 0.0;
                // parcours des valeurs des vecteurs sur l'axe
                for (int k=0; k<N; k++) {
                    somme += U[i][k] * U[j][k];
                } 
                produitScalaire[i][j] = somme;
                // remplissage de la partie inférieure de la matrice par symétrie
                if (i!=j) {
                    produitScalaire[j][i] = produitScalaire[i][j];
                }
            }
        }
        /* 
        System.out.println("\nVérification de la base :");
        for (String[] s : getStrings(produitScalaire)) {
            System.out.println(Arrays.toString(s));
        }
        */

    }

    // Détermination des vecteurs de contribution (Projection)
    public void Proj() {
        // parcours de la liste des vecteurs
        for (int k=0; k<vectorisePatchs.length; k++) {
            // parcours des axes
            for (int i=0; i<vectorisePatchs[0].length; i++) {
                // calcul du coefficient a(i)(k)
                double somme = 0.0;
                for (int l=0; l<U[i].length; l++) {
                    somme += U[i][l] * centeredVectors[k][l];
                }
                Vcontrib[k][i] = somme;
            }
        }
    }

}
