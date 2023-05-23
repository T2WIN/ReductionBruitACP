package src.main;


public class Main {
    public static void main(String[] args) {
        // Image image = new Image("src/main/lenaa.png", 2);
        // System.out.println(image.getMatrix());
        // BufferedImage newImage = image.createImageFromMatrix(image.getMatrix());
        // image.createfile(newImage);

        // Error error = new Error("src/main/lenaa.png", 20);
        // System.out.println(error.MeanSquaredError());
        // int[][] matrix = new int[2][2];
        // matrix[0][0] = 1;
        // matrix[0][1] = 2;
        // matrix[1][0] = 3;
        // matrix[1][1] = 4;
        // Patch patch = new Patch(matrix, 0, 0);
        // int[] vect = patch.vectorize();
        // patch.intoMatrix(vect);
        // System.out.println(patch.matrix[0][0]);

        // Extraction Patch Globale
        Scanner sc;
		sc = new Scanner(System.in);
		System.out.print("Saisir l'entier s, taille du patch : ");
		int s;
		s = sc.nextInt(); 
        
        int [][] patchcourant = new int[s][s];
        int coint_sup_x;
        int coint_sup_y;
        ArrayList<Patch> ListePatch= new ArrayList<Patch>();

        int [][] test = new int[5][5];
        String newLine = System.getProperty("line.separator");

        for (int index = 0; index < 5; index++) {
            for (int i = 0; i < 5; i++) {
                test[index][i]=i;
                System.out.println(test[index][i]);
            }
            System.out.println(newLine);
        }

        System.out.println("ici");

        for (int i = 0; i < 5-s+1; i++) {
            for (int j = 0; j < 5-s +1; j++) {
                // Position du pixel du coin gauche 
                coint_sup_x= i;
                coint_sup_y= j;
                // Création d'un patch à partir de chaque pixel
                Patch patch = new Patch(patchcourant,coint_sup_x,coint_sup_y);
                // Ajout des pixels de l'image dans chaque patch
                for (int k = coint_sup_x; k < coint_sup_x + s;k++){
                    for (int l = coint_sup_y; l < coint_sup_y + s; l++){
                        patchcourant[k - coint_sup_x][l - coint_sup_y] = test[k][l];
                        System.out.println(test[k][l]);
                    }
                }
                System.out.println(newLine);

                ListePatch.add(patch);
            }   
        }
        sc.close();

        // Reconstruction Patch
        
        int x=5;
        int y=5;

        int [][] test2 = new int[x][y];
        int [][] matricePoids = new int [x][y]; 
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                test2[i][j]=0;
                matricePoids[i][j]=0;
            }
        }

        int cointx;
        int cointy;

        // Ajout des patchs dans la matrice 
        for (int k = 0; k < ListePatch.size(); k++) {
            for (int l=0; l<s;s++){
                for (int m=0; m<s; m++){
                    cointx = ListePatch.get(k).positionX;
                    cointy = ListePatch.get(k).positionY;
                    int [][] patch = new int[s][s];
                    patch = ListePatch.get(k).matrix;
                    test2[cointx+l][cointy+m] += patch[l][m];
                    // Ajout du poids pour chaque élément de la matrice 
                    matricePoids[cointx][cointy] += 1;
                }
            }
        }
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                System.out.println(matricePoids[i][j]);
            }
            System.out.println(newLine);
        }

    }    
}
