package com.antonio.hundirlaflota;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
    class GitHubEmail {
        private String email;
        private boolean verified;
        private boolean primary;
        private String visibility;

    } 