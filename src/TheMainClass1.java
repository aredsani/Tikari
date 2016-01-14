import Utils.GeometricObjects.Point_2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Sanidhya Gupta on 08-01-2016.
 */
public class TheMainClass1 {
    private void calibrate() throws IOException, InterruptedException {
        String path = String.format("res/Sample/cal1.jpg");
        File input = new File(path);
        BufferedImage myImage = ImageIO.read(input);
//        double viewScaleFactor = 0.5;
        Vector<Point_2D> myTwoDPoints = FindFourPoints.getPointCoordinates(myImage);
        System.out.println("myTwoDPoints.size() = " + myTwoDPoints.size());
        for (int i = 0; i < myTwoDPoints.size(); i++) {
            System.out.println("myTwoDPoints.get(i) = " + myTwoDPoints.get(i).getI() + "  " + myTwoDPoints.get(i).getJ());
        }
        Vector<Point_2D> myPoint_2Ds = new Vector<>();
        for (int i = 0; i < 4; i++) {
            myPoint_2Ds.add(myTwoDPoints.get(i));
        }
        Calibrator cal_coeffs=new Calibrator(myPoint_2Ds,210);
        True_Position position= new True_Position(cal_coeffs,myTwoDPoints.get(4));
        True_Position myTrue_position = new True_Position(cal_coeffs,myTwoDPoints.get(5));
        double myDist = Math.sqrt(Math.pow(position.getX() - myTrue_position.getX(),2)+ Math.pow(position.getY() - myTrue_position.getY(),2));
        System.out.println("myDist = " + myDist);
    }

    private void execute() throws IOException {
        try {
            calibrate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        TheMainClass1 myTheMainClass1 = new TheMainClass1();
        myTheMainClass1.execute();

    }
}
