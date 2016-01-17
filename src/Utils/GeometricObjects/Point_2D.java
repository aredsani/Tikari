package Utils.GeometricObjects;

/**
 * Created by polojushravan on 1/8/2016.
 */
public class Point_2D {
    private int i;
    private int j;

    public Point_2D(int x0, int y0) {
        setI(x0);
        setJ(y0);
    }

    public Point_2D(TwoDPoint aTwoDPoint) {
        setI((int) aTwoDPoint.x);
        setJ((int) aTwoDPoint.y);
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}
