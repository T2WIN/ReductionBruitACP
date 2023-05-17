import java.util.ArrayList;

public class ACP {

    ArrayList<ArrayList<Float>> vectorisePatchs;
    ArrayList<Float> meanVector = new ArrayList<>(Collections.nCopies(vectorisePatchs.get(0).size(), 0.0));;
    ArrayList<ArrayList<Float>> covariance;
    ArrayList<ArrayList<Float>> centeredVectors;

    
    public void MoyCov() {

        // liste des moyennes des vecteurs
        ArrayList<Float> moyenne_vecteurs = new ArrayList<>(Collections.nCopies(vectorisePatchs.size(), 0.0));;

        // Déterminer le vecteur moyen
        // parcours des axes
        for (j=0; j<vectorisePatchs.get(0).size(); j++) {
            // parcours de la liste des vecteurs
            for (i=0; i<vectorisePatchs.size(); i++) {
                // additions de toute les valeurs
                meanVector.set(j, meanVector.get(j) + vectorisePatchs.get(i).get(j));
            }
            // division par le nombre de vecteurs pour faire la moyenne de chaque axe
            meanVector.set(j, meanVector.get(j) / vectorisePatchs.size());
        }

        // Centrer les vecteurs
        // parcours de la liste des vecteurs
        for (i=0; i<vectorisePatchs.size(); i++) {
            // parcours des axes
            for (j=0; j<vectorisePatchs.get(0).size(); j++) {
                // calcul pour centrer le vecteur
                centeredVectors.get(i).add(vectorisePatchs.get(i).get(j) - meanVectors.get(j));
                // additions des valeurs du vecteur (pour la Matrice de Covariance après)
                moyenne_vecteurs.set(i, moyenne_vecteurs.get(i) + vectorisePatchs.get(i).get(j));
            }
            // calcul de la moyenne du vecteur (pour la Matrice de Covariance après)
            moyenne_vecteurs.set(i, moyenne_vecteurs.get(i) / vectorisePatchs.get(i).size());
        }

        // Déterminer la matrice de covariance
        // taille des vecteurs
        int N = vectorisePatchs.get(0).size();
        float somme;
        // parcours de la liste des vecteurs
        for (i=0; i<vectorisePatchs.size(); i++) {
            // initialisation de la somme (pour le calcul de la covariance)
            somme = 0.0;
            // 2ème parcours de la liste des vecteurs (on ne remplit que la partie supérieure de la matrice car elle est symétrique)
            for (j=0; j<i+1; j++) {
                // parcours des axes
                for (k=0; k<N; k++) {
                    somme += (vectorisePatchs.get(i).get(k) - moyenne_vecteurs.get(i))*(vectorisePatchs.get(j).get(k) - moyenne_vecteurs.get(j));
                }
                // calcul de la matrice de covariance 
                covariance.get(i).add(somme/N);
                // remplissage de la partie inférieure de la matrice par symétrie
                if (i!=j) {
                    covariance.get(j).add(somme/N);
                }
            }
        }

    }

}
