package com.arsoft.reportimage.controller;

import com.arsoft.reportimage.models.ImageParam;
import com.arsoft.reportimage.models.ImagesArray;
import com.arsoft.reportimage.models.ReportParam;
import com.arsoft.reportimage.service.ImageService;
import com.arsoft.reportimage.models.ImageModel;
import com.arsoft.reportimage.utils.ImageToFXImage;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import  javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    private final ImageService service = new ImageService();
    private final ImageModel model = new ImageModel();
    private DirectoryChooser directoryChooser;
    private File selected;

    @FXML
    private CheckBox checkBox1 ;

    @FXML
    private CheckBox checkBox2 ;

    @FXML
    private CheckBox checkBox3 ;

    @FXML
    private CheckBox checkBox4 ;

    @FXML
    private CheckBox checkBox5 ;

    @FXML
    private Label welcomeText;

    @FXML
    private ColorPicker backColor;

    @FXML
    private TextField cropForce;


    @FXML
    protected void onHelloButtonClick() {
        //welcomeText.setText("Welcome to JavaFX Application!");
        Image image = new Image("picture1.png");

        System.out.println("1 Start");
        Properties params = new Properties();
        try {
            params.load(this.getClass().getClassLoader().getResourceAsStream("image-param.inf"));
        }catch (FileNotFoundException ext) {
            System.out.println("Error load file "+ext.toString());
        } catch (IOException e) {
            System.out.println("Error load file "+e.toString());
        }

        service.initialize(null,null);


        List<String> pathes = new ArrayList<>();

        if (selected!=null) {
            String[] files = selected.list((lamFolder, lamName) -> lamName.matches( "([^\"]+?)(?:.jpg|.gif|.png)"));
            if(files == null) {
                System.out.println("Stuff wrongly: no matching files found.");
            } else {
                for(String file : files) {
                    System.out.println("HOORAY: I found this "+ file);
                    pathes.add(selected.getAbsolutePath()+ "\\" + file.toString());
                }

            }
        } else {
            pathes.add("picture1.png");
            pathes.add("picture2.png");
            pathes.add("picture3.png");
        }
        ReportParam reportPrm = new ReportParam();
        reportPrm.setImgWidth(Integer.parseInt(params.getProperty("reportWidth")));
        reportPrm.setImgHeight(Integer.parseInt(params.getProperty("reportHeight")));
        reportPrm.setImgRate(Float.parseFloat(params.getProperty("reportRate")));

        System.out.println("2 Add report param");
        ImagesArray imagesArray = service.loadImages(pathes);
        // Add listener
        model.getImagesArray().addListener((ListChangeListener<? super javaxt.io.Image>) change -> {
            System.out.println("Changes obs list");
        });
        //model.getImagesArray().clear();
        //model.getImagesArray().addAll(imagesArray.getImages());
        System.out.println("3 Load images");

        ImageParam imageParam = new ImageParam();
        imageParam.setReportFullSize(checkBox1.isSelected());
        imageParam.setReportAverage(checkBox2.isSelected());
        imageParam.setReportAreaFill(checkBox3.isSelected());
        imageParam.setReportCrop(checkBox4.isSelected());
        imageParam.setReportColor(checkBox5.isSelected());
        imageParam.setReportBackgroundColor(new Color((float) backColor.getValue().getRed(),(float) backColor.getValue().getGreen(),(float) backColor.getValue().getBlue(), (float) backColor.getOpacity()));
        System.out.println("33 Param images");
        model.getImgConvertParam().set(imageParam);


        System.out.println("4 Add property");
        ImagesArray convertedImages = service.convertForParams(imagesArray, reportPrm, model.getImgConvertParam().get());
        System.out.println("5 Convert images");
        for (javaxt.io.Image imgConverted :convertedImages.getImages() ) {
            if (imgConverted!=null) {
                System.out.println("6 To FXImage in view");
                ImageView imageView = ImageToFXImage.viewImage(imgConverted);
                ((VBox)welcomeText.getParent()).getChildren().add(imageView);
            }
        }
        System.out.println("7 Set model converted images");
        model.getImagesArray().clear();
        model.getImagesArray().addAll(convertedImages.getImages());
        System.out.println("End action...");

    }

    @FXML
    public void saveButtonClick(ActionEvent actionEvent) {
        try{
            service.saveAllImages(new ImagesArray().setImages(model.getImagesArray()),"e:\\PROJECT\\IdeaProject\\AR_ConvertImages\\output");
        }catch (Exception e) {
            System.out.println("Error save image "+e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Start init function - Color picker Init");
        //STYLE_CLASS_BUTTON
        backColor.setValue(new javafx.scene.paint.Color(0.5,0.5, 0.5, 0.4));
        cropForce.setStyle("background-color: #aaaaaa");
        //backColor.getStyleClass().add("button");
        directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("output"));

    }

    public void openButtonClick(ActionEvent actionEvent) {
        File selectedDirectory = directoryChooser.showDialog(cropForce.getScene().getWindow());
        if (selectedDirectory!=null) {
            selected = selectedDirectory;
            System.out.println(selectedDirectory.getAbsolutePath());
        }
    }
}