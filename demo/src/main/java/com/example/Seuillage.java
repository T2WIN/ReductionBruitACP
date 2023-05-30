package com.example;

public class Seuillage {

    private double visuShrink;
    private double bayesShrink;
    private Image image;

    // constructeur de seuillage
    public Seuillage(Image image) {
        this.image = image;
        this.bayesShrink = seuilB();
        this.visuShrink = seuilV();
    }

    // seuillage dur avec en paramètre threshold le seuil et alpha le coefficient
    public double HardThresholding(double threshold, double alpha) {

        if (threshold >= Math.abs(alpha)) {
            return 0;   // met à 0 les coefficients en dessous du seuil
        }
        else {
            return alpha; //ne modifie pas les coefficients au dessus du seuil
        }
    }

    //seuillage doux avec en paramètre threshold le seuil et alpha le coefficient
    public double SoftThresholding(double threshold, double alpha) {

        if (threshold >= Math.abs(alpha)) {
            return 0;  // met à 0 les coefficients en dessous du seuil
        }
        if (threshold < alpha) {
            return (alpha - threshold); // retourne le coefficient moins le seuil si le seuil est strictement inférieur au coefficient
        }
        if ((-threshold) >= alpha) {
            return (alpha + threshold); // retourne le seuil plus le coefficient si le coefficient est inférieur au seuil qui lui est négatif
        }

        return 0;
    }

    public double seuilV() {
        int L = image.getMatrix().length*image.getMatrix()[0].length;
        visuShrink = image.getSigma()*Math.sqrt(2*Math.log(L));
        System.out.println(visuShrink);
        return visuShrink;
    }

    //Fonction pour calculer le maximum entre deux nombres
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

    //fonction calculer la variance d'un echantillon
    public double calculVariance() {
        double moyenne;
        double somme = 0;
        int[] echantillon = new int[this.image.getNoisedMatrix().length*this.image.getNoisedMatrix()[0].length];

        //On transforme d'abord la matrice bruitée en vecteur
        for (int i= 0 ; i< this.image.getNoisedMatrix().length ; i++) {
            for (int j= 0 ; j< this.image.getNoisedMatrix()[0].length ; j++) {
                echantillon[i*this.image.getNoisedMatrix().length + j] = this.image.getNoisedMatrix()[i][j];
            }
        }
        moyenne = mean(echantillon);
        //On peut alors calculer la variance
        for (int i = 0 ; i < echantillon.length ; i++) {
            somme = somme + (echantillon[i] - moyenne)*(echantillon[i] - moyenne);
        }
        return somme/(echantillon.length -1);      
    }


    public double seuilB() {
        double variance = calculVariance();
        double ecartType = Math.sqrt(max(variance-Math.pow(image.getSigma(),2) , 0));

        bayesShrink = Math.pow(image.getSigma(),2)/ecartType;
        return bayesShrink;
    }

    // getter et setter de visuShrink
    public double getVisuShrink() {
        return this.visuShrink;
    }


     public void setVisuShrink(Integer visuShrink) {
        this.visuShrink = visuShrink;
    }

    // getter et setter de bayesShrink
    public double getBayesShrink() {
        return this.bayesShrink;
    }

    
    public void setBayesShrink(Integer bayesShrink) {
        this.bayesShrink = bayesShrink;
    }

}
