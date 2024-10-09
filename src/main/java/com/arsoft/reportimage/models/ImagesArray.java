package com.arsoft.reportimage.models;


import javaxt.io.Image;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// Load and analise for array of image
// @author Denis V.
@Component
public class ImagesArray {

    public static final int LEVEL1_VALUE = 1;
    public static final double LEVEL2_VALUE = 1.6;
    public static final double LEVEL3_VALUE = 2.1;
    public static final double LEVEL4_VALUE = 2.4;
    public static final double LEVEL1_NEGATIVE_VALUE = 0.5;
    public static final double LEVEL2_NEGATIVE_VALUE = 0.2;
    public static final double LEVEL3_NEGATIVE_VALUE = 0.1;

    private List<Image> images = new ArrayList<>();

    private Float reportWidth = 600f;
    private Float reportHeightMin = 600f;
    private Float reportHeightMax = 600f;
    private Float reportRate = 1.2f;

    public void add(Image image) {
        getImages().add(image);
    }

    public void clear() {
        if (isNotNull()) {
            images.clear();
        }
    }

    public boolean isNotNull() {
        if (getImages() !=null)
            if (getImages().size()>0)
                return true;
        return false;
    }

    public float mediumWidth() {
        float medium = 0;
        for (Image img: getImages()) {
            medium+=img.getWidth();
        }
        medium = medium/ getImages().size();
        return medium;
    }

    public float mediumHeight() {
        float medium = 0;
        for (Image img: getImages()) {
            medium+=img.getHeight();
        }
        medium = medium/ getImages().size();
        return medium;
    }

    public Float avaredCWH() {


        return null;
    }

    public List<Integer> getLongWidth() {
        List<Integer> counts = new ArrayList<>();
        for (int i = 0; i < getImages().size(); i++) {
            Image img = getImages().get(i);
            float def = (float)img.getWidth()/ getReportWidth();
            if ( def> 1.4f || def < 0.5f) {
                int addDef = 1;
                if (def > LEVEL1_VALUE) addDef = 2;
                if (def > LEVEL2_VALUE) addDef = 3;
                if (def > LEVEL3_VALUE) addDef = 4;
                if (def > LEVEL4_VALUE) addDef = 5;
                if (def < LEVEL1_NEGATIVE_VALUE) addDef = -2;
                if (def < LEVEL2_NEGATIVE_VALUE) addDef = -3;
                if (def < LEVEL3_NEGATIVE_VALUE) addDef = -4;
                counts.add(addDef);
            } else {
                counts.add(1);
            }
        }

        return counts;
    }

    public List<Integer> getLongHeight() {
        List<Integer> counts = new ArrayList<>();
        for (int i = 0; i < getImages().size(); i++) {
            Image img = getImages().get(i);
            float def = (float)img.getHeight()/ getReportHeight();
            float def1 = (float)img.getHeight()/reportHeightMax;
            if ( def> 1.4f || def < 0.5f) {
                int addDef = 1;
                if (def1 > LEVEL1_VALUE) addDef = 2;
                if (def1 > LEVEL2_VALUE) addDef = 3;
                if (def1 > LEVEL3_VALUE) addDef = 4;
                if (def1 > LEVEL4_VALUE) addDef = 5;
                if (def < LEVEL1_NEGATIVE_VALUE) addDef = -2;
                if (def < LEVEL2_NEGATIVE_VALUE) addDef = -3;
                if (def < LEVEL3_NEGATIVE_VALUE) addDef = -4;
                counts.add(addDef);
            } else {
                counts.add(1);
            }
        }

        return counts;
    }

    public List<Image> getImages() {
        return images;
    }

    public ImagesArray setImages(List<Image> images) {
        this.images = images;
        return this;
    }

    public void setParams(ReportParam reportParams) {
        if (reportParams!=null) {
            if (reportParams.getImgWidth() > 0)
                reportWidth = Float.valueOf(reportParams.getImgWidth());
            if (reportParams.getImgHeight() > 0)
                reportHeightMin = Float.valueOf(reportParams.getImgHeight());
                reportHeightMax = Float.valueOf(reportParams.getImgHeight())+10;
            if (reportParams.getImgRate() > 0 )
                reportRate = Float.valueOf(reportParams.getImgRate());
        }
    }

    public Float getReportHeight() {
        return reportHeightMin;
    }

    public void setReportHeightMin(Float reportHeightMin) {
        this.reportHeightMin = reportHeightMin;
    }

    public Float getReportWidth() {
        return reportWidth;
    }

    public void setReportWidth(Float reportWidth) {
        this.reportWidth = reportWidth;
    }
}
