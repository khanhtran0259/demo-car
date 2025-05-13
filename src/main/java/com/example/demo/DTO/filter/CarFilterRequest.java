package com.example.demo.DTO.filter;

import lombok.Data;

@Data
public class CarFilterRequest {
    private Long brandId;
    private String mfg;
    private Long price;
    private String owner;
    private Boolean isAdmin;

    private int page = 0;
    private int size = 10;

}
