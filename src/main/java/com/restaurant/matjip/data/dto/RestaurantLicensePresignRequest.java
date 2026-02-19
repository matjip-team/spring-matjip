package com.restaurant.matjip.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class RestaurantLicensePresignRequest {

    @NotBlank(message = "File name is required")
    private String fileName;

    @NotBlank(message = "Content type is required")
    @Pattern(regexp = "^(application/pdf|image/.*)$", message = "Only PDF or image files are allowed")
    private String contentType;
}
