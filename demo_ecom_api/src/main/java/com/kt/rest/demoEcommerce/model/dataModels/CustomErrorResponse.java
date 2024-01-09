package com.kt.rest.demoEcommerce.model.dataModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomErrorResponse {
    private String statusText;
    private HttpStatus status;

}
