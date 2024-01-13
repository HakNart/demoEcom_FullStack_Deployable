package com.kt.rest.demoEcommerce.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private T payload;
    private String message;

    private ApiResponse() {

    }
    public Builder<T> builder() {
        return new Builder<T>();
    }
    private static class Builder<T> {
        private final ApiResponse<T> apiResponse = new ApiResponse<>();

        public Builder<T> success(T payload) {
            apiResponse.status = "success";
            apiResponse.payload = payload;
            return this;
        }

        public Builder<T> fail(String message) {
            apiResponse.status = "fail";
            apiResponse.message = message;
            return this;
        }

        public Builder<T> error(String message) {
            apiResponse.status = "error";
            apiResponse.message = message;
            return this;
        }

        public ApiResponse<T> build() {
            return apiResponse;
        }
    }
}
