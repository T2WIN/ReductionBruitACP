package src.main.java.src.main.java;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

public class ACP {

    int [][] vectorisePatchs;
    double [] meanVector = new double [vectorisePatchs[0].length];
    double [][] covariance = new double [vectorisePatchs.length][vectorisePatchs.length];
    double [][] centeredVectors = new double [vectorisePatchs.length][vectorisePatchs[0].length];
    double [][] U = new double [vectorisePatchs[0].length][vectorisePatchs[0].length];
    double [][] Vcontrib = new double [vectorisePatchs.length][vectorisePatchs[0].length];

    
    public void MoyCov() {

        // liste des moyennes des vecteurs
        double [] moyenne_vecteurs = new double [vectorisePatchs.length];

        // Déterminer le vecteur moyen
        // parcours des axes
        for (int j=0; j<vectorisePatchs[0].length; j++) {
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
                // additions des valeurs du vecteur (pour la Matrice de Covariance après)
                moyenne_vecteurs[i] += vectorisePatchs[i][j];
            }
            // calcul de la moyenne du vecteur (pour la Matrice de Covariance après)
            moyenne_vecteurs[i] = moyenne_vecteurs[i] / vectorisePatchs[i].length;
        }

        // Déterminer la matrice de covariance
        // taille des vecteurs
        int N = vectorisePatchs[0].length;
        double somme;
        // parcours de la liste des vecteurs
        for (int i=0; i<vectorisePatchs.length; i++) {
            // initialisation de la somme (pour le calcul de la covariance)
            somme = 0.0;
            // 2ème parcours de la liste des vecteurs (on ne remplit que la partie supérieure de la matrice car elle est symétrique)
            for (int j=0; j<i+1; j++) {
                // parcours des axes
                for (int k=0; k<N; k++) {
                    somme += (vectorisePatchs[i][k] - moyenne_vecteurs[i]) * (vectorisePatchs[j][k] - moyenne_vecteurs[j]);
                }
                // calcul de la matrice de covariance 
                covariance[i][j] = somme / N;
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
        RealMatrix eigenVectors = eigenDecomposition.getV();

        // Détermination de la base orthonormée
        for (int i=0; i<vectorisePatchs[0].length; i++) {
            for (int j=0; j<vectorisePatchs[0].length; j++) {
                U[i][j] = eigenVectors.getEntry(i, j);
            }
        }

    }

    public void Proj() {
        // parcours de la liste des vecteurs
        for (int i=0; i<vectorisePatchs.length; i++) {
            // parcours des axes
            for (int j=0; j<vectorisePatchs[0].length; j++) {
                // calcul du coefficient a(i)(k)
                int somme = 0;
                for (int l=0; l<U[j].length; l++) {
                    somme += U[j][l] * centeredVectors[i][l];
                }
                Vcontrib[i][j] = somme;
            }
        }
    }

}
