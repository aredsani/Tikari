import Example.CannyEdgeExample;
import Utils.CannyEdgeDetector;
import Utils.GeometricObjects.Point_2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Sanidhya Gupta on 08-01-2016.
 */
public class FindFourPoints {

    private static Vector<Point_2D> getPointsManually(BufferedImage aImage) throws InterruptedException {
        Vector<Point_2D> myPoints = new Vector<>();
        class CustomMouseListener implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent e) {
                myPoints.add(new Point_2D(e.getX(), e.getY()));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }
        JFrame myFrame = new JFrame();
        myFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {

            }
        });
        double viewScaleFactor = 0.3;
        myFrame.setSize((int) (aImage.getWidth() * viewScaleFactor), (int) (aImage.getHeight() * viewScaleFactor));
        JPanel panel = new JPanel();
        panel.add(new JLabel(new ImageIcon(aImage.getScaledInstance((int) (aImage.getWidth() * viewScaleFactor), (int) (aImage.getHeight() * viewScaleFactor), 1))));
        panel.addMouseListener(new CustomMouseListener());
        myFrame.add(panel);
        myFrame.setVisible(true);
        while (myFrame.isVisible())
            Thread.sleep(1000);
        for (int i = 0; i < myPoints.size(); i++) {
            myPoints.set(i, new Point_2D((int) (myPoints.get(i).getI() / viewScaleFactor), (int) (myPoints.get(i).getJ() / viewScaleFactor)));
        }
        return myPoints;
    }

    private static Vector<Point_2D> getPointsByCannyEdge(BufferedImage aImage) throws InterruptedException, IOException {
        return getPointsByReducingImage(aImage);
    }

    private static Vector<Point_2D> getPointsByReducingImage(BufferedImage aImage) throws InterruptedException, IOException {
        Vector<Point_2D> myPoints = new Vector<>();
        JFrame myFrame = new JFrame();
        myFrame.setTitle("Click on 1st point");
        class CustomMouseListener implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent e) {
                myPoints.add(new Point_2D(e.getX(), e.getY()));
                myFrame.setTitle(String.format("Click on %dth point", myPoints.size() + 1));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        }

        myFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {

            }
        });
        double viewScaleFactor = 0.3;
        myFrame.setSize((int) (aImage.getWidth() * viewScaleFactor), (int) (aImage.getHeight() * viewScaleFactor));
        JPanel panel = new JPanel();
        panel.add(new JLabel(new ImageIcon(aImage.getScaledInstance((int) (aImage.getWidth() * viewScaleFactor), (int) (aImage.getHeight() * viewScaleFactor), 1))));
        panel.addMouseListener(new CustomMouseListener());
        myFrame.add(panel);
        myFrame.setVisible(true);
        while (myFrame.isVisible())
            Thread.sleep(1000);
        int smallHeight = Math.abs(myPoints.get(1).getJ() - myPoints.get(0).getJ());
        smallHeight = (int) (smallHeight / viewScaleFactor);
        int smallWidth = Math.abs(myPoints.get(1).getI() - myPoints.get(0).getI());
        smallWidth = (int) (smallWidth / viewScaleFactor);

        BufferedImage myImage = new BufferedImage(smallWidth, smallHeight, aImage.getType());
        for (int i = 0; i < myImage.getWidth(); i++) {
            for (int j = 0; j < myImage.getHeight(); j++) {
                myImage.setRGB(i, j, aImage.getRGB((int) (myPoints.get(0).getI() / viewScaleFactor) + i, (int) (myPoints.get(0).getJ() / viewScaleFactor) + j));
            }
        }
        ImageIO.write(myImage, "JPG", new File("ShravanPagalHai1.jpg"));
        myFrame.dispose();
        myFrame.setSize(myImage.getWidth(), myImage.getHeight());
        myFrame.removeAll();
        panel = new JPanel();
        panel.add(new JLabel(new ImageIcon(myImage)));
//        panel.addMouseListener(new CustomMouseListener());
        myFrame.add(panel);
        myFrame.setVisible(true);


        while (myFrame.isVisible())
            Thread.sleep(1000);
        BufferedImage myImage1 = Utils.ImageUtils.detectEdges(myImage);
        ImageIO.write(myImage1, "JPG", new File("ShravanPagalHai.jpg"));

        return null;
    }


    protected static Vector<Point_2D> getPointCoordinates(BufferedImage aImage) throws InterruptedException, IOException {
//        return getPointsManually(aImage);
        return getPointsByCannyEdge(aImage);
    }
}
