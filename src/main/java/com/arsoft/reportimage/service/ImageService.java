package com.arsoft.reportimage.service;

import com.arsoft.reportimage.controller.HelloController;
import com.arsoft.reportimage.models.ImageParam;
import com.arsoft.reportimage.models.ImagesArray;
import com.arsoft.reportimage.models.ReportParam;
import com.arsoft.reportimage.utils.ImageConvertor;

import javafx.fxml.Initializable;
import javaxt.io.Image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ImageService implements Initializable {

    private final ReportParam defaultReportParam = new ReportParam();

    private final ImageConvertor convertor = new ImageConvertor();


    private List<Image> convert(ImagesArray images, ImageParam imageParam) {
        return convertor.convertTo(images , imageParam);
    }

    public ImagesArray loadImages(List<String> pathes) {
        List<javaxt.io.Image> sourceImages = new ArrayList<>();
        if (pathes!=null && !pathes.isEmpty())
            for (String path: pathes)
                if (path.contains("\\"))
                    sourceImages.add(new javaxt.io.Image(path));
                else    sourceImages.add(new javaxt.io.Image(HelloController.class.getClassLoader().getResourceAsStream(path)));
        ImagesArray imgArray = new ImagesArray();
        imgArray.setImages(sourceImages);
        return imgArray;
    }

    public ImagesArray addConvertToLast(ImagesArray images, Image inputImage) {
        // TODO Add image to process convert of param from last convert action;

        return null;
    }

    public ImagesArray convertForParams(ImagesArray images, ReportParam reportParam, ImageParam imageParam) {
        images.setParams(reportParam);
        List<Image> converted = convert(images, imageParam);
        ImagesArray result = new ImagesArray();
        result.setImages(converted);
        result.setParams(reportParam);
        return result;
    }

    public boolean saveAllImages(ImagesArray images, String destination) {
        if (images==null) return false;
        if (images.getImages().isEmpty()) return false;
        for (int i = 0; i < images.getImages().size(); i++) {
            Image img = images.getImages().get(i);
            try {
                img.saveAs(destination + "\\"+"imageFile"+i+".png");
            }catch (Exception ex) {
                System.out.println("Image by code = "+i+" not save and many errors!!!");
            }
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Properties params = new Properties();
        try {
            System.out.println("Initialize param");
            params.load(this.getClass().getClassLoader().getResourceAsStream("image-param.inf"));
        }catch (FileNotFoundException ext) {
            System.out.println("Error load file "+ext.toString());
        } catch (IOException e) {
            System.out.println("Error load file "+e.toString());
        }
        defaultReportParam.setImgWidth(Integer.parseInt(params.getProperty("reportWidth")));
        defaultReportParam.setImgHeight(Integer.parseInt(params.getProperty("reportHeight")));
        defaultReportParam.setImgRate(Float.parseFloat(params.getProperty("reportRate")));
        System.out.println("Add report param");

    }

    public ResponseEntity<ByteArrayResource>  downloadConvertImage(String filename) {
        List<String> pathes = new ArrayList<>();
            pathes.add(filename);

        ImagesArray imagesArray = this.loadImages(pathes);
        System.out.println("3 Load images");
        ImageParam imageParam = new ImageParam();
        ImagesArray convertedImages = this.convertForParams(imagesArray, defaultReportParam, imageParam);
        System.out.println("5 Convert images");
        for (javaxt.io.Image imgConverted :convertedImages.getImages() ) {
            if (imgConverted != null) {
                int lastDelimiterIndex = 0;
                try {
                    lastDelimiterIndex = filename.lastIndexOf("\\");
                }catch (Exception e) {}
                if (lastDelimiterIndex==0)
                    lastDelimiterIndex = filename.lastIndexOf("/");
                if (lastDelimiterIndex<0)
                    lastDelimiterIndex = 0;
                filename = filename.substring(lastDelimiterIndex);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8))
                        .contentType(
                                 MediaType.parseMediaType(MediaType.IMAGE_PNG_VALUE))
                        .body(new ByteArrayResource(imgConverted.getByteArray()));
            }
        }
        return null;
    }

    public ResponseEntity<ByteArrayResource> downloadByParams(String path, ReportParam reportParam, ImageParam imageParam) {
        List<String> pathes = new ArrayList<>();
        pathes.add(path);
        ImagesArray imagesArray = this.loadImages(pathes);
        Path p = Paths.get(path);
        ImagesArray convertedImages = this.convertForParams(imagesArray, reportParam, imageParam);
        for (javaxt.io.Image imgConverted :convertedImages.getImages() ) {
            if (imgConverted != null) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + URLEncoder.encode(p.getFileName().toString(), StandardCharsets.UTF_8))
                        .contentType(
                                MediaType.parseMediaType(MediaType.IMAGE_PNG_VALUE))
                        .body(new ByteArrayResource(imgConverted.getByteArray()));
            }
        }
        return null;
    }
}
