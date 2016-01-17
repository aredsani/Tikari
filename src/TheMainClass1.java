import Utils.GeometricObjects.Point_2D;

import javax.imageio.ImageIO;
import java.awt.*;
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
        BufferedImage myCopyofSourceImage = ImageIO.read(new File(path));
//        System.out.println("lol");
        Graphics2D myGraphics2D1 = myCopyofSourceImage.createGraphics();
        BasicStroke bs = new BasicStroke(3);
//        myGraphics2D.setStroke(bs);
        myGraphics2D1.setStroke(bs);
        myGraphics2D1.setColor(Color.BLUE);
        myGraphics2D1.drawLine(myPoint_2Ds.get(0).getI(), myPoint_2Ds.get(0).getJ(), myPoint_2Ds.get(1).getI(), myPoint_2Ds.get(1).getJ());
        myGraphics2D1.drawLine(myPoint_2Ds.get(1).getI(), myPoint_2Ds.get(1).getJ(), myPoint_2Ds.get(2).getI(), myPoint_2Ds.get(2).getJ());
        myGraphics2D1.drawLine(myPoint_2Ds.get(2).getI(), myPoint_2Ds.get(2).getJ(), myPoint_2Ds.get(3).getI(), myPoint_2Ds.get(3).getJ());
        myGraphics2D1.drawLine(myPoint_2Ds.get(3).getI(), myPoint_2Ds.get(3).getJ(), myPoint_2Ds.get(0).getI(), myPoint_2Ds.get(0).getJ());
        myGraphics2D1.dispose();
        ImageIO.write(myCopyofSourceImage, "jpg", new File("res/Sample/Cal1Out.jpg"));

        Calibrator cal_coeffs = new Calibrator(myPoint_2Ds, 210);
//        True_Position position= new True_Position(cal_coeffs,myTwoDPoints.get(4));
//        True_Position myTrue_position = new True_Position(cal_coeffs,myTwoDPoints.get(5));
//        double myDist = Math.sqrt(Math.pow(position.getX() - myTrue_position.getX(),2)+ Math.pow(position.getY() - myTrue_position.getY(),2));
//        System.out.println("myDist = " + myDist);
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
