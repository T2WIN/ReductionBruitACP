package src.main.java;

public class Patch {
    private int[][] matrix;
    private int positionX;
    private int positionY;

    //Un patch est une matrice carrée dont le coin en haut à gauche est (positionX, positionY)
    public Patch(int[][] matrix, int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.matrix = matrix;

    }
    //Transformation du patch sous forme matricielle vers sa forme vectorielle
    public int[] vectorize() {
        int[] vector = new int[this.matrix.length*this.matrix[0].length];
        for (int i=0; i<this.matrix.length; i++) {
            for (int j=0; j<this.matrix[0].length; j++) {
                vector[i*this.matrix[0].length + j] = this.matrix[i][j];
            }
        }
        return vector;
    }

    //Met à jour la version matricielle d'un patch à partir d'un vecteur
    public double[][] intoMatrix(double[] vector) {
        double [][] newMatrix = new double[this.matrix.length][this.matrix[0].length];
        for (int i = 0; i<this.matrix.length*this.matrix[0].length; i++) {
            //L'indice de colonne (le numéro du vecteur auquel appartient le coefficient)
            //est le résultat de la division entière entre l'indice dans le vecteur d'origine et la longueur d'un vecteur dans la matrice
            int c = i/this.matrix[0].length;
            int l = i - c*this.matrix[0].length;
            newMatrix[l][c] = vector[i];
        }
        
        return newMatrix;
    }

    public void setMatrix(int [][] matrix2){
        this.matrix = matrix2;
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public int getPositionX() {
        return positionX;
    }
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }
    public int getPositionY() {
        return positionY;
    }
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
