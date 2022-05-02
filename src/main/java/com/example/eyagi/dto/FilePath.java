package com.example.eyagi.dto;

import lombok.Data;

@Data
public class FilePath {

    private String originFileS3;
    private String cutFileS3;
    private String originFileS3Url;
    private String cutFileS3Url;
}
