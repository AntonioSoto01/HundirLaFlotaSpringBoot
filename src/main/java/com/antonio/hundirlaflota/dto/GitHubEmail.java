package com.antonio.hundirlaflota.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class GitHubEmail {
    private String email;
    private boolean verified;
    private boolean primary;
    private String visibility;

}