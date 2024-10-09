package com.arsoft.reportimage.utils;

import com.arsoft.reportimage.models.ImageParam;
import com.arsoft.reportimage.models.ImagesArray;
import com.arsoft.reportimage.models.ReportParam;
import javafx.util.Pair;
import javaxt.io.Image;

import java.util.ArrayList;
import java.util.List;

// @author Denis Vit
public class ImageConvertor {

    private final int CROP_MAX_PARAM = 13;
    private final int MINIMAL_DIFFERENT_VALUE = 2;
    private final int MAX_WIDTH_LEVEL = 2;
    private final int MIN_WIDTH_LEVEL = -2;
    private final int MAX_HEIGHT_LEVEL = 3;
    private final int MIN_HEIGHT_LEVEL = -1;


    private List<Pair<Float, Float>> analyses(ImagesArray imagesArr, ImageParam imageParam) {
        List<Integer> longWidth = imagesArr.getLongWidth();
        List<Integer> longHeight = imagesArr.getLongHeight();
        if (imageParam.getReportCrop()) {
            for (int i = 0; i < longWidth.size(); i++) {
                Integer lWidth = longWidth.get(i);
                Integer lHeight = longHeight.get(i);
                if (lWidth == null || lHeight == null) continue;
                if (lWidth > MAX_WIDTH_LEVEL) {
                    if (lHeight < MAX_HEIGHT_LEVEL) {
                        Image img = imagesArr.getImages().get(i);
                        int leftCorner = img.getWidth() / (CROP_MAX_PARAM - lWidth - imageParam.getCropForce());
                        int width = img.getWidth() - leftCorner * 2;
                        img.crop(leftCorner, 0, width, img.getHeight());
                        imagesArr.getImages().set(i, img);
                    }
                }
                if (lWidth < MIN_WIDTH_LEVEL) {
                    if (lHeight > MIN_HEIGHT_LEVEL) {
                        Image img = imagesArr.getImages().get(i);
                        int leftCorner = img.getHeight() / (CROP_MAX_PARAM - lHeight - imageParam.getCropForce());
                        int height = img.getHeight() - leftCorner * 2;
                        img.crop(0, leftCorner, img.getWidth(), height);
                        imagesArr.getImages().set(i, img);
                    }
                }

            }
        }
        return null;
    }

    private List<Boolean> maxMin() {
        //for (int i = 0; i < imagesArr.getImages().size(); i++) {
            //  Image img = imagesArr.getImages().get(i);


        //}
        return null;
    }

    private List<Image> convert(ImagesArray imagesArr, ImageParam imageParam) {
        List<Boolean> convertIsMax = maxMin();
        List<Pair<Float, Float>> convertData = analyses(imagesArr, imageParam);

        List<Image> imageListResult = new ArrayList<>();
        for (int i = 0; i < imagesArr.getImages().size(); i++) {
            Image img = imagesArr.getImages().get(i);
            if (imageParam.getReportAverage()==false && imageParam.getReportFullSize()==true) {
                img.resize(imagesArr.getReportWidth().intValue(), imagesArr.getReportHeight().intValue());
            } else if (imageParam.getReportAverage()==true &&  imageParam.getReportFullSize()==false) {
                img.resize(Float.valueOf(imagesArr.mediumWidth()).intValue(), Float.valueOf(imagesArr.mediumHeight()).intValue());
            } else if (imageParam.getReportAverage()==true &&  imageParam.getReportFullSize()==true) {
                img.resize(Float.valueOf(imagesArr.mediumWidth()).intValue(), Float.valueOf(imagesArr.mediumHeight()).intValue());
                img.setWidth(imagesArr.getReportWidth().intValue());
                if (img.getHeight() > imagesArr.getReportHeight()) {
                    img.setHeight(imagesArr.getReportHeight().intValue());
                }
            } else if (imageParam.getReportAverage()==false && imageParam.getReportFullSize()==false) {
                img.setWidth(imagesArr.getReportWidth().intValue());
                if (img.getHeight() > imagesArr.getReportHeight()) {
                    img.setHeight(imagesArr.getReportHeight().intValue());
                }
            }
            if (imageParam.getReportAreaFill()) {
                if (img.getHeight()+MINIMAL_DIFFERENT_VALUE < imagesArr.getReportHeight().intValue() || img.getWidth()+2 < imagesArr.getReportWidth().intValue()) {
                    Image imgFill = new Image(imagesArr.getReportWidth().intValue(), imagesArr.getReportHeight().intValue());
                    if (img.getHeight()+MINIMAL_DIFFERENT_VALUE < imagesArr.getReportHeight().intValue()) {
                        int yValue = (int) Math.ceil((imagesArr.getReportHeight().intValue() - img.getHeight()) / 2);
                        imgFill.addImage(img, 0, yValue, false);

                    }else if (img.getWidth()+MINIMAL_DIFFERENT_VALUE < imagesArr.getReportWidth().intValue()) {
                        int xValue = (int) Math.ceil((imagesArr.getReportWidth().intValue() - img.getWidth()) / 2);
                        imgFill.addImage(img,  xValue, 0, false);

                    }
                    if (imageParam.getReportColor()) {
                        imgFill.setBackgroundColor(imageParam.getReportBackgroundColor().getRed(),imageParam.getReportBackgroundColor().getGreen(),imageParam.getReportBackgroundColor().getBlue());
                    }

                    imageListResult.add(imgFill);
                    continue;
                }
            }
            imageListResult.add(img);
        }
        return imageListResult;
    }


    public List<Image> convertTo(ImagesArray images, ImageParam param) {
        return convert(images,param);
    }



}
