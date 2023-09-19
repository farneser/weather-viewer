package com.farneser.weatherviewer.helpers.factory;

import com.farneser.weatherviewer.dto.LoginDto;
import com.farneser.weatherviewer.dto.RegisterDto;
import com.farneser.weatherviewer.helpers.parser.ParameterParser;
import jakarta.servlet.http.HttpServletRequest;

public abstract class UserDtoFactory {
    public static RegisterDto getRegister(HttpServletRequest request) {
        var registerDto = new RegisterDto();

        registerDto.setUsername(ParameterParser.getString(request, "username"));
        registerDto.setPassword(ParameterParser.getString(request, "password"));
        registerDto.setConfirmPassword(ParameterParser.getString(request, "confirmPassword"));

        return registerDto;
    }

    public static LoginDto getLogin(HttpServletRequest request) {
        var loginDto = new LoginDto();

        loginDto.setUsername(ParameterParser.getString(request, "username"));
        loginDto.setPassword(ParameterParser.getString(request, "password"));

        return loginDto;
    }
}
