package com.example.demo.service;

import com.example.demo.DTO.response.CarResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
@Service
public class CarReport {

    public byte[] generateCarReport(List<CarResponse> cars) throws JRException {
        InputStream reportStream = getClass().getResourceAsStream("/templates/CarReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cars);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "YourApp");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
