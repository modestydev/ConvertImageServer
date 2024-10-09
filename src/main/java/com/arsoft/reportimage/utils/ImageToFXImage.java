package com.arsoft.reportimage.utils;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javaxt.io.Image;

import java.awt.image.BufferedImage;

// Image to ImageView from JavaFX show
public class ImageToFXImage {

    public static ImageView viewImage(Image image) {
        BufferedImage bufferedImage = image.getBufferedImage();
        WritableImage wr = null;
        if (bufferedImage != null) {
            wr = new WritableImage(bufferedImage.getWidth(), bufferedImage.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                for (int y = 0; y < bufferedImage.getHeight(); y++) {
                    pw.setArgb(x, y, bufferedImage.getRGB(x, y));
                }
            }
        }
        ImageView imView = new ImageView(wr);
        bufferedImage = null;
        return imView;
    }

}
