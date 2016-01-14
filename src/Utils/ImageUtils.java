package Utils;

import Example.CannyEdgeExample;

import java.awt.image.BufferedImage;

/**
 * Created by Sanidhya Gupta on 08-01-2016.
 */
public class ImageUtils {
//    public static BufferedImage scaleImage(BufferedImage aImage){
//
//    }
    public static BufferedImage detectEdges(BufferedImage aImage){
        CannyEdgeDetector myCED = new CannyEdgeDetector();
        myCED.setSourceImage(aImage);
        myCED.process();
        BufferedImage myEdgeImage = myCED.getEdgesImage();
        CannyEdgeExample.ArrayData inputData = CannyEdgeExample.getArrayDataFromBufferedImage(myEdgeImage);
        int max = inputData.getMax();
        BufferedImage outputImage = new BufferedImage(inputData.width, inputData.height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < inputData.height; y++) {
            for (int x = 0; x < inputData.width; x++) {
                int n = Math.min((int) Math.round(inputData.get(x, y) * 255.0 / max), 255);
                outputImage.setRGB(x, inputData.height - 1 - y, (n << 16) | (n << 8) | 0x90 | -0x01000000);
            }
        }
        return outputImage;
    }

    public static BufferedImage getCannyEdgeImage(BufferedImage aSourceImage){
        CannyEdgeDetector myCED = new CannyEdgeDetector();
        myCED.setSourceImage(aSourceImage);
        myCED.process();
        BufferedImage myEdgeImage = myCED.getEdgesImage();
        return myEdgeImage;
    }
}
