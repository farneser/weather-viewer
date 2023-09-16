package com.farneser.weatherviewer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class RegisterDto {
    private String username;
    private String password;
    private String confirmPassword;
}
