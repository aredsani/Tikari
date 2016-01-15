package Utils.GeometricObjects;

/**
 * Created by Sanidhya Gupta on 15-01-2016.
 */
public class Line {
    //Line of the form Y = mX +c
    public double m; // slope of the Line
    public double c; // Y intercept of the line

    private double getC() {
        return c;
    }

    private void setC(double aC) {
        c = aC;
    }

    private double getM() {
        return m;
    }

    private void setM(double aM) {
        m = aM;
    }


    public Line(double aSlope, double aintercept) {
        m = aSlope;
        c = aintercept;
    }


}
