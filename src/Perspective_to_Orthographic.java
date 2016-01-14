
import Utils.GeometricObjects.Point_2D;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

/**
 * Created by polojushravan on 1/5/2016.
 */
public class Perspective_to_Orthographic {
    public static void main(String[] args) {

        double length=1; //true length of side of square

        Vector<Point_2D> Cal_Collection= new Vector<>(3);
        /*Cyclic Point Chronology*/
        Cal_Collection.add(0,new Point_2D(814,568));//(0,0)
        Cal_Collection.add(1,new Point_2D(589,529));//(1,0)
        Cal_Collection.add(2,new Point_2D(700,434));//(1,1)
        Cal_Collection.add(3,new Point_2D(909,470));//(0,1)

        /*Calibration*/
        Calibrator cal_coeffs=new Calibrator(Cal_Collection,length);

        /*Post Calibration evaluation*/
        Point_2D loc=new Point_2D(909,470);
        True_Position position= new True_Position(cal_coeffs,loc);

        System.out.println(position.getX() + " " + position.getY());// units same as input length's units
    }
}
