package com.kt.rest.demoEcommerce.models.dataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomErrorResponse {
    private String statusText;
    private HttpStatus status;

}
