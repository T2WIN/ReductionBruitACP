 public static void methodeGlobale(Image image,String fichier) {
   ArrayList<Patch> listePatch = image.extractionPatch(image.getNoisedMatrix());
   int[][] matricePatchs = image.vectorPatch(listePatch);
   ACP acp = new ACP(matricePatchs);
   acp.MoyCov();
   acp.DoACP();
   acp.Proj();
   double[][] alpha = acp.getVcontrib();
   double[] meanVector = acp.getMoyCov();

   int choixSeuil = chooseSeuil();
   int choixSeuillage = chooseSeuillage();
   // Modification de la matrice alpha par les seuillages
   if (choixSeuil == 1) {
      double  threshold = seuillage.getVisuShrink();
      if (choixSeuillage == 1) {
         for (int i = 0; i < alpha.length; i++) {
            for (int j = 0; j < alpha[1].length; j++) {
               double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha[i][j]);
               alpha[i][j] = hardThresholdingResult;
            }
         }
      }
      if (choixSeuillage == 2) {
         for (int i = 0; i < alpha.length; i++) {
            for (int j = 0; j < alpha[1].length; j++) {
               double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha[i][j]);
               alpha[i][j] = softThresholdingResult;
            }    
         }
      }
   }
   if (choixSeuil == 2) {
      double  threshold = seuillage.getBayesShrink();
      if (choixSeuillage == 1) {
         for (int i = 0; i < alpha.length; i++) {
            for (int j = 0; j < alpha[1].length; j++) {
               double hardThresholdingResult = seuillage.HardThresholding(threshold, alpha[i][j]);
               alpha[i][j] = hardThresholdingResult;
            }
         }
      }
      if (choixSeuillage == 2) {
         for (int i = 0; i < alpha.length; i++) {
            for (int j = 0; j < alpha[1].length; j++) {
               double softThresholdingResult = seuillage.SoftThresholding(threshold, alpha[i][j]);
               alpha[i][j] = softThresholdingResult;                        
            }
         }       
      }
      for (int k = 0; k < listePatch.size(); k++) {
         double [] somme = new double[taille*taille];
         Patch patch = listePatch.get(k);
         double [] patchVect2 = new double[taille*taille];
         //Pour chaque (alpha(i), u(i)), on applique la formule
         for (int j = 0; j < alpha[0].length; j++) {
            for (int i = 0; i <alpha[0].length; i++) {
               somme[j] += alpha[k][i] * u[i][j];
            }
         }
         for (int i=0; i<taille*taille; i++){
         //On décentre les vecteurs
            patchVect2[i] = meanVector[i] + somme[i];
         }
         double[][] patchMatrixDouble = patch.intoMatrix(patchVect2);
         int[][] patchMatrixEntier = new int[patchMatrixDouble.length][patchMatrixDouble[0].length];
                  
         for (int indexC = 0; indexC<patchMatrixEntier.length; indexC++) {
            for (int indexL = 0; indexL<patchMatrixEntier[0].length; indexL++) {
               patchMatrixEntier[indexC][indexL] = (int) patchMatrixDouble[indexC][indexL];      
            }    
         }
         patch.setMatrix(patchMatrixEntier);
         listePatch.set(k, patch);
      }
              
      int[][] assemblerMatrice = image.assemblagePatch(listePatch, l, c);
      BufferedImage imagefinale = Image.createImageFromMatrix(assemblerMatrice);
      Image.createfile(imagefinale, fichier);
   }
 }

