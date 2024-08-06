package com.antonio.hundirlaflota.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailData {
    private String subject;
    private String message;
    private String email;
    private String token;
    private String path;
}
