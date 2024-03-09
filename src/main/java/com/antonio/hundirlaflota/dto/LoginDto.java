package com.antonio.hundirlaflota.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginDto {


    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "Formato de email incorrecto")
    private String email;

    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(min = 5, message = "La contraseña debe tener al menos 5 caracteres")
    private String contrasena;
}
