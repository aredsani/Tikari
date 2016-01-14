import Utils.GeometricObjects.Point_2D;

/**
 * Created by polojushravan on 1/8/2016.
 */
public class True_Position {
    private double X;
    private double Y;

    public True_Position(Calibrator cal, Point_2D loc){
        X = (cal.getC()[1] * loc.getI() + cal.getC()[3] * loc.getJ() + cal.getC()[5]) / (1 - cal.getC()[7] * loc.getI() - cal.getC()[8] * loc.getJ());
        Y = (cal.getC()[2] * loc.getI() + cal.getC()[4] * loc.getJ() + cal.getC()[6]) / (1 - cal.getC()[7] * loc.getI() - cal.getC()[8] * loc.getJ()) - cal.getC()[0];
    }
    public double getX(){
        return X;
    }

    public double getY(){
        return Y;
    }
}
