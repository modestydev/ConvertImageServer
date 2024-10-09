package com.arsoft.reportimage.controller;

import com.arsoft.reportimage.models.ImageParam;
import com.arsoft.reportimage.models.ImagesArray;
import com.arsoft.reportimage.models.ReportParam;
import com.arsoft.reportimage.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
public class ServerController {

    private final ImageService imageService;

    public ServerController(ImageService imageService) {
        this.imageService = imageService;
        this.imageService.initialize(null, null);
    }

    @GetMapping(value = "/report/test")
    private ResponseEntity getTestString() {
        return ResponseEntity.ok("build");
    }

    // Получить тестовый файл картинки или уже обработанный ранее
    @GetMapping(value = "/fileimage/{filename}")
    public ResponseEntity<ByteArrayResource> downloadConvertedImage(@PathVariable String filename) {
        return (imageService.downloadConvertImage(filename));
    }

    // Обработка картинки с наименьшим количеством параметров без сохранения результата
    @PostMapping(value = "/report/image/from_param")
    public ResponseEntity<ByteArrayResource>  downloadConvertedByParams(@RequestParam("path") String path,
                                                                        @RequestBody ReportParam param) {
        return imageService.downloadByParams(path,param, new ImageParam());
    }


    // Обработка картинки с параметрами без сохранения результата
    @PostMapping(value = "/report/image/{reportId}")
    private ResponseEntity<ByteArrayResource> downloadConvertedByAllParams(@RequestParam("path") String path,
                                        @RequestParam("isFullSize") Boolean fillSize,
                                        @RequestParam("isAverage") Boolean average,
                                        @RequestParam("isFillBack") Boolean fillBack,
                                        @RequestParam("isColor") Boolean colors,
                                        @RequestParam("isCrop") Boolean crop,
                                        @RequestParam("width") Integer width,
                                        @RequestParam("height") Integer height,
                                        @PathVariable Long reportId) {

        ReportParam reportParam = new ReportParam();
        if (width!=null && height!=null) {
            reportParam.setImgWidth(width);
            reportParam.setImgHeight(height);
            reportParam.setImgRate((float) width / (float) height);
        }
        ImageParam imageParam = new ImageParam();
        imageParam.setReportFullSize(fillSize);
        imageParam.setReportAverage(average);
        imageParam.setReportCrop(crop);
        imageParam.setReportAreaFill(fillBack);
        imageParam.setReportColor(colors);
        return imageService.downloadByParams(path,reportParam, imageParam);
    }

    // Обработка картинок с сохранением результата
    @PostMapping(value = "/report/images/save")
    public ResponseEntity<String> savedImagesByParams(@RequestParam("paths") String[] paths,
                                                      @RequestParam("destination") String destination,
                                                      @RequestBody ReportParam param) {
        ImagesArray sourceArray = imageService.loadImages(Arrays.stream(paths).toList());
        ImagesArray imagesArray = imageService.convertForParams(sourceArray, param, new ImageParam());
        imageService.saveAllImages(imagesArray, destination);
        return ResponseEntity.ok("All right, convert save to: "+destination);
    }
}
