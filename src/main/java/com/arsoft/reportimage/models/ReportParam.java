package com.arsoft.reportimage.models;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ReportParam {
    private int imgWidth;
    private int imgHeight;
    private float imgRate;
    private float gamma;

}
