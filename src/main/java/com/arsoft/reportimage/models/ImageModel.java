package com.arsoft.reportimage.models;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javaxt.io.Image;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;



@Component
public class ImageModel {

    private final ObservableList<Image> imagesArray = FXCollections.observableArrayList();

    private final SimpleObjectProperty<ImageParam> imgConvertParam = new SimpleObjectProperty<>(new ImageParam());

    public ObservableList<Image> getImagesArray() {
        return imagesArray;
    }

    public SimpleObjectProperty<ImageParam> getImgConvertParam() {
        return imgConvertParam;
    }
}
