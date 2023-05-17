package src.main;
import java.util.*;

public class Seuillage {

    private Integer visuShrink;
    private Integer bayesShrink;
    private Image image;

    public int getvisuShrink() {
        return this.visuShrink;
    }

     public void setvisuShrink(Integer visuShrink) {

        this.visuShrink = visuShrink;
    }

    public int getbayesShrink() {
        return this.bayesShrink;
    }

     public void setbayesShrink(Integer bayesShrink) {
        this.bayesShrink = bayesShrink;
    }
    public int HardThresholding(int threshold, int alpha) {

        if (threshold >= Math.abs(alpha)) {
            return 0;
        }
        else {
            return alpha;
        }
    }

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

    public int seuilV() {
        int L = image.getMatrix().length*image.getMatrix()[0].length;
        visuShrink = image.sigmaMath.sqrt(2Math.log(L));
        return 0;
    }
    public float max(float a, float b) {
        if (a<=b){
            return b;
        }
        else {
            return a;
        }
    }

    public int seuilB() {
        float variance;
        float ecartType = Math.sqrt(max((Math.pow(variance,2)-Math.pow(image.sigma,2),0)));
        bayesShrink = Math.pow(image.sigma,2)/ecartType;
        return 0;
    }


}
