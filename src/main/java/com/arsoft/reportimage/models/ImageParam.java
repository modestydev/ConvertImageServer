package com.arsoft.reportimage.models;

import java.awt.*;


public class ImageParam {
    private Boolean reportAverage;
    private Boolean reportAreaFill;
    private Boolean reportFullSize;
    private Boolean reportCrop;
    private Boolean isReportColor;
    private Color reportBackgroundColor;
    private Integer cropForce;

    public ImageParam() {
        setReportAverage(false);
        setReportAreaFill(true);
        setReportFullSize(false);
        setReportCrop(true);
        setReportColor(true);
        setReportBackgroundColor(new Color(100,100,100,30));
        cropForce = 3;
    }

    public Boolean getReportAverage() {
        return reportAverage;
    }

    public void setReportAverage(Boolean reportAverage) {
        this.reportAverage = reportAverage;
    }

    public Boolean getReportAreaFill() {
        return reportAreaFill;
    }

    public void setReportAreaFill(Boolean reportAreaFill) {
        this.reportAreaFill = reportAreaFill;
    }

    public Boolean getReportFullSize() {
        return reportFullSize;
    }

    public void setReportFullSize(Boolean reportFullSize) {
        this.reportFullSize = reportFullSize;
    }

    public Boolean getReportCrop() {
        return reportCrop;
    }

    public void setReportCrop(Boolean reportCrop) {
        this.reportCrop = reportCrop;
    }

    public Boolean getReportColor() {
        return isReportColor;
    }

    public void setReportColor(Boolean reportColor) {
        isReportColor = reportColor;
    }

    public Color getReportBackgroundColor() {
        return reportBackgroundColor;
    }

    public void setReportBackgroundColor(Color reportBackgroundColor) {
        this.reportBackgroundColor = reportBackgroundColor;
    }


    public Integer getCropForce() {
        return cropForce;
    }

    public void setCropForce(Integer cropForce) {
        this.cropForce = cropForce;
    }
}
