package src.main.java;



public class Seuillage {

    private double visuShrink;
    private double bayesShrink;
    private Image image;

    public double getVisuShrink() {
        return this.visuShrink;
    }

     public void setVisuShrink(Integer visuShrink) {
        this.visuShrink = visuShrink;
    }

    public double getBayesShrink() {
        return this.bayesShrink;
    }

     public void setBayesShrink(Integer bayesShrink) {
        this.bayesShrink = bayesShrink;
    }

    //Seuillage dur
    public int HardThresholding(int threshold, int alpha) {

        if (threshold >= Math.abs(alpha)) {
            return 0;
        }
        else {
            return alpha;
        }
    }

    //Seuillage doux
    public int SoftThresholding(int threshold, int alpha) {

        if (threshold >= Math.abs(alpha)) {
            return 0;
        }
        if (threshold < alpha) {
            return (alpha - threshold);
        }
        if ((-threshold) >= alpha) {
            return (alpha + threshold);
        }

        return 0;
    }

    public double max(double a, double b) {
        if (a<=b){
            return b;
        }
        else {
            return a;
        }
    }
    // fonction pour calculer la moyenne d'un echantillon
    public double mean(int[] entiers) {
        double moy = 0;
        for (int i = 0 ; i < entiers.length ; i++) {
            moy = moy + entiers[i];
        } 
        return (moy/entiers.length);
    }
    //fonction calculer la variance 
    public double calculVariance() {
        double moyenne;
        double somme = 0;
        int[] echantillon = new int[this.image.getMatrix().length*this.image.getMatrix()[0].length];
        for (int i= 0 ; i< this.image.getMatrix().length ; i++) {
            for (int j= 0 ; j< this.image.getMatrix()[0].length ; j++) {
                echantillon[i*this.image.getMatrix().length + j] = this.image.getMatrix()[i][j];
            }
        }
        moyenne = mean(echantillon);
        for (int i = 0 ; i < echantillon.length ; i++) {
            somme = somme + (echantillon[i] - moyenne)*(echantillon[i] - moyenne);
        }
        return somme/(echantillon.length -1);

        
        
      
    }
    //Calcul du seuil de BayesShrink
    public int seuilB() {
        double variance = calculVariance();
        double ecartType = Math.sqrt(max((Math.pow(variance,2)-Math.pow(image.getSigma(),2)) , 0));
        bayesShrink = Math.pow(image.getSigma(),2)/ecartType;
        return 0;
    }


}
