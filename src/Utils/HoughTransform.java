package Utils;

import Utils.GeometricObjects.Line;
import Utils.GeometricObjects.Point_2D;
import Utils.GeometricObjects.TwoDPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by Sanidhya Gupta on 11-01-2016.
 */
public class HoughTransform {
    public static ArrayData houghTransform(ArrayData inputData, int thetaAxisSize, int rAxisSize, int minContrast) {
        int width = inputData.width;
        int height = inputData.height;
        int maxRadius = (int) Math.ceil(Math.hypot(width, height));
        int halfRAxisSize = rAxisSize >>> 1;
        ArrayData outputData = new ArrayData(thetaAxisSize, rAxisSize);
        // x output ranges from 0 to pi
        // y output ranges from -maxRadius to maxRadius
        double[] sinTable = new double[thetaAxisSize];
        double[] cosTable = new double[thetaAxisSize];
        for (int theta = thetaAxisSize - 1; theta >= 0; theta--) {
            double thetaRadians = theta * Math.PI / thetaAxisSize;
            sinTable[theta] = Math.sin(thetaRadians);
            cosTable[theta] = Math.cos(thetaRadians);
        }

        for (int y = height - 1; y >= 0; y--) {
            for (int x = width - 1; x >= 0; x--) {
                if (inputData.contrast(x, y, minContrast)) {
                    for (int theta = thetaAxisSize - 1; theta >= 0; theta--) {
                        double r = cosTable[theta] * x + sinTable[theta] * y;
                        int rScaled = (int) Math.round(r * halfRAxisSize / maxRadius) + halfRAxisSize;
                        outputData.accumulate(theta, rScaled, 1);
                    }
                }
            }
        }
        return outputData;
    }

    public static class ArrayData {
        public final int[] dataArray;
        public final int width;
        public final int height;

        public ArrayData(int width, int height) {
            this(new int[width * height], width, height);
        }

        public ArrayData(int[] dataArray, int width, int height) {
            this.dataArray = dataArray;
            this.width = width;
            this.height = height;
        }

        public int get(int x, int y) {
            return dataArray[y * width + x];
        }

        public void set(int x, int y, int value) {
            dataArray[y * width + x] = value;
        }

        public void accumulate(int x, int y, int delta) {
            set(x, y, get(x, y) + delta);
        }

        public boolean contrast(int x, int y, int minContrast) {
            int centerValue = get(x, y);
            for (int i = 8; i >= 0; i--) {
                if (i == 4)
                    continue;
                int newx = x + (i % 3) - 1;
                int newy = y + (i / 3) - 1;
                if ((newx < 0) || (newx >= width) || (newy < 0) || (newy >= height))
                    continue;
                if (Math.abs(get(newx, newy) - centerValue) >= minContrast)
                    return true;
            }
            return false;
        }

        //        public int getMax() {
//            int max = dataArray[0];
//            for (int i = width * height - 1; i > 0; i--)
//                if (dataArray[i] > max)
//                    max = dataArray[i];
//            return max;
//        }
        public int getMax() {
            int max = 0;
            Point_2D myPoint_2D = new Point_2D(0, 0);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (max < get(i, j)) {
                        myPoint_2D.setI(i);
                        myPoint_2D.setJ(j);
                        max = get(i, j);
                    }

                }
            }
            return max;
        }

        public Point_2D getMaxIndex() {
            int max = 0;
            Point_2D myPoint_2D = new Point_2D(0, 0);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (max < get(i, j)) {
                        myPoint_2D.setI(i);
                        myPoint_2D.setJ(j);
                        max = get(i, j);
                    }

                }
            }
            return myPoint_2D;
        }

        public void cleanSurroundings(Point_2D aPoint_2D, int windowSize) {
//            for (int i = width * height - 1; i > 0; i--)
//                if ((i!=aArrayIndex)&&(Math.abs(i-aArrayIndex)<windowSize) ) {
//                    dataArray[i] =0;
//                }
            int minI = aPoint_2D.getI() - windowSize;
            if (minI < 0)
                minI = 0;
            int maxI = aPoint_2D.getI() + windowSize;
            if (maxI > width)
                maxI = width;
            int minJ = aPoint_2D.getJ() - windowSize;
            if (minJ < 0)
                minJ = 0;
            int maxJ = aPoint_2D.getJ() + windowSize;
            if (maxJ > height)
                maxJ = height;
            for (int i = minI; i < maxI; i++) {
                for (int j = minJ; j < maxJ; j++) {
                    set(i, j, 0);
                }
            }
        }
    }

    public static ArrayData getArrayDataFromImage(String filename) throws IOException {
        return getArrayDataFromBufferedImage(ImageIO.read(new File(filename)));
    }

    public static ArrayData getArrayDataFromBufferedImage(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        int[] rgbData = inputImage.getRGB(0, 0, width, height, null, 0, width);
        ArrayData arrayData = new ArrayData(width, height);
        // Flip y axis when reading image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgbValue = rgbData[y * width + x];
                rgbValue = (int) (((rgbValue & 0xFF0000) >>> 16) * 0.30 + ((rgbValue & 0xFF00) >>> 8) * 0.59 + (rgbValue & 0xFF) * 0.11);
                arrayData.set(x, height - 1 - y, rgbValue);
            }
        }
        return arrayData;
    }

    public static void writeOutputImage(String filename, ArrayData arrayData) throws IOException {
        int max = arrayData.getMax();
        BufferedImage outputImage = new BufferedImage(arrayData.width, arrayData.height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < arrayData.height; y++) {
            for (int x = 0; x < arrayData.width; x++) {
                int n = Math.min((int) Math.round(arrayData.get(x, y) * 255.0 / max), 255);
                outputImage.setRGB(x, arrayData.height - 1 - y, (n << 16) | (n << 8) | 0x90 | -0x01000000);
            }
        }
        ImageIO.write(outputImage, "PNG", new File(filename));
        return;
    }

    public static void draw(int r, double theta, BufferedImage image, int color) throws IOException, InterruptedException {

        int height = image.getHeight();
        int width = image.getWidth();

        // During processing h_h is doubled so that -ve r values
        int houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2;

        // Find edge points and vote in array
        float centerX = width / 2;
        float centerY = height / 2;

        // Draw edges in output array
        double tsin = Math.sin(theta);
        double tcos = Math.cos(theta);

        if (theta < Math.PI * 0.25 || theta > Math.PI * 0.75) {
            // Draw vertical-ish lines
            for (int y = 0; y < height; y++) {
                int x = (int) ((((r - houghHeight) - ((y - centerY) * tsin)) / tcos) + centerX);
                if (x < width && x >= 0) {
                    image.setRGB(x, y, color);
                }
            }
        } else {
            // Draw horizontal-sh lines
            for (int x = 0; x < width; x++) {
                int y = (int) ((((r - houghHeight) - ((x - centerX) * tcos)) / tsin) + centerY);
                if (y < height && y >= 0) {
                    image.setRGB(x, y, color);
                }
            }
        }
        ImageIO.write(image, "PNG", new File("something.PNG"));
        Thread.sleep(1000);

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String path = String.format("res/Example/Hough.jpg");
        BufferedImage mySourceImage = ImageIO.read(new File(path));

        BufferedImage myEdgeImage = Utils.ImageUtils.getCannyEdgeImage(mySourceImage);
        //below is the houghTransformPart
        ArrayData inputData = getArrayDataFromBufferedImage(myEdgeImage);
        int minContrast = 200;
        int ThetaAxis = 400;
        int rAxis = 800;
        ArrayData outputData = houghTransform(inputData, ThetaAxis, 2 * rAxis, minContrast);

        //below is Line extraction Part

        Vector<Point_2D> myLines = new Vector<>();
        int nLines = 4;
        for (int i = 0; i < nLines; i++) {
            myLines.add(outputData.getMaxIndex());
            outputData.cleanSurroundings(myLines.get(i), 30);
        }
        //below is the line drawing part
        Graphics2D myGraphics2D = mySourceImage.createGraphics();

        BasicStroke bs = new BasicStroke(3);
        myGraphics2D.setStroke(bs);
        myGraphics2D.setColor(Color.RED);
        int maxRadius = (int) Math.ceil(Math.hypot(mySourceImage.getWidth(), mySourceImage.getHeight()));

        Vector<Line> myLinesMC = new Vector<>(); // Line of the form y = mx + c in image coordinates where I is m, and J is c

        for (int i = 0; i < nLines; i++) {
            int Line1;
            int Line2;
            if (myLines.get(i).getI() != 0 && ((1.0 * myLines.get(i).getI() / ThetaAxis) != 0.5)) {
                Line1 = (int) (((myLines.get(i).getJ() - rAxis) * ((1.0 * maxRadius) / rAxis)) / Math.cos(((1.0 * myLines.get(i).getI() * Math.PI)) / ThetaAxis));
                Line2 = (int) (((myLines.get(i).getJ() - rAxis) * ((1.0 * maxRadius) / rAxis)) / Math.sin(((1.0 * myLines.get(i).getI() * Math.PI)) / ThetaAxis));
            } else if (myLines.get(i).getI() == 0) {
                Line1 = (int) (((myLines.get(i).getJ() - rAxis) * (1.0 * maxRadius)) / rAxis);
                Line2 = 999999999;
            } else {
                Line1 = 999999999;
                Line2 = (int) (((myLines.get(i).getJ() - rAxis) * (1.0 * maxRadius)) / rAxis);
            }
            if (Line1 < 0 && Line2 > 0) {
                myGraphics2D.drawLine((int) -1.0 * Line1 * (mySourceImage.getHeight() - Line2) / Line2, 0, 0, mySourceImage.getHeight() - Line2);
                double m = 1.0 * (mySourceImage.getHeight() - Line2) / (1.0 * Line1 * (mySourceImage.getHeight() - Line2) / Line2);
                double c = -m * -1.0 * Line1 * (mySourceImage.getHeight() - Line2) / Line2;
                myLinesMC.add(new Line(m, c));
            } else if (Line2 < 0 && Line1 > 0) {
                myGraphics2D.drawLine(Line1, mySourceImage.getHeight(), (int) 1.0 * Line1 * (mySourceImage.getHeight() - Line2) / (-Line2), 0);
                double m = 1.0 * (-mySourceImage.getHeight()) / ((1.0 * Line1 * (mySourceImage.getHeight() - Line2) / (-Line2)) - Line1);
                double c = 1.0 * mySourceImage.getHeight() - (m * Line1);
                myLinesMC.add(new Line(m, c));
            } else if (Line2 < 0 && Line1 < 0) {
                System.out.println("Something unexpected happened in Drawing hough Lines");
            } else {
                myGraphics2D.drawLine(Line1, mySourceImage.getHeight(), 0, mySourceImage.getHeight() - Line2);
                double m = 1.0 * ((mySourceImage.getHeight() - Line2) - (mySourceImage.getHeight())) / ((0) - (Line1));
                double c = mySourceImage.getHeight() - (m * Line1);
                myLinesMC.add((new Line(m, c)));
            }

        }
        for (int i = 0; i < myLinesMC.size(); i++) {
            System.out.println(i + " m = " + myLinesMC.get(i).m + " c = " + myLinesMC.get(i).c);
        }

        myGraphics2D.dispose();
        ImageIO.write(mySourceImage, "jpg", new File("res/Example/CannyEdgeOutput.jpg"));
        return;
    }
}
