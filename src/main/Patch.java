package src.main;

public class Patch {
    int[][] matrix;
    int positionX;
    int positionY;

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
    public int[][] intoMatrix(int[] vector) {
        int [][] newMatrix = new int[this.matrix.length][this.matrix[0].length];
        for (int i = 0; i<this.matrix.length; i++) {
            int c = i/this.matrix[0].length;
            int l = i - c*this.matrix[0].length;
            newMatrix[l][c] = vector[i];
            System.out.println(l + "," + c);
        }
            
        
        return newMatrix;
    }
}
