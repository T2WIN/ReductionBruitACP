package src.main;

public class Patch {
    int[][] matrix;
    int positionX;
    int positionY;

    public Patch(int[][] matrix, int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.matrix = matrix;
    }

    public int[] vectorize() {
        int[] vector = new int[this.matrix.length*this.matrix[0].length];
        for (int i=0; i<this.matrix.length; i++) {
            for (int j=0; j<this.matrix[0].length; j++) {
                vector[i*this.matrix[0].length + j] = this.matrix[i][j];
            }
        }
        return vector;
    }

    public int[][] intoMatrix(int[] vector) {
        int i = 0;
        int [][] matrix = new int[this.matrix.length][this.matrix[0].length];
        while (i < vector.length) {
            int c = i/this.matrix[0].length;
            int l = i - c*this.matrix[0].length;
            matrix[l][c] = vector[i];
            System.out.println(l + "," + c);
            i++;
        }
        return matrix;
    }
}
