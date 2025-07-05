package com.sherry.accounts_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class ResponseDto {
    private String statusCode;
    private String statusMsg;

    public ResponseDto(String statusMsg, String statusCode) {
        this.statusMsg = statusMsg;
        this.statusCode = statusCode;
    }
}
