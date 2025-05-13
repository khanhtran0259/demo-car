package com.example.demo.controller;

import com.example.demo.DTO.response.CarResponse;
import com.example.demo.service.CarReport;
import com.example.demo.service.implement.CarServiceImpl;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/export")
public class ReportController {
    private final CarServiceImpl carServiceImpl;
    private final CarReport carReport;
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> exportCarReport() throws JRException {
        List<CarResponse> cars = carServiceImpl.getSpecificCars();

        byte[] pdfReport = carReport.generateCarReport(cars);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("car_report.pdf").build());

        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }

}
