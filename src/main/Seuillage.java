package src.main;



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
        int L = image.getMatrix().size()*image.getMatrix().get(0).size();
        visuShrink = image.getSigma()*Math.sqrt(2*Math.log(L));
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

    public int seuilB() {
        double variance=5;
        double ecartType = Math.sqrt(max((Math.pow(variance,2)-Math.pow(image.getSigma(),2)) , 0));
        bayesShrink = Math.pow(image.getSigma(),2)/ecartType;
        return 0;
    }


}
