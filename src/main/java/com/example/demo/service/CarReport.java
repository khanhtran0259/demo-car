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
        // Load template (ensure it's in /resources folder)
        InputStream reportStream = getClass().getResourceAsStream("/templates/CarReport.jrxml");

        // Compile .jrxml to .jasper
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(cars);

        // Parameters (if needed)
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "YourApp");

        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export to PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}
