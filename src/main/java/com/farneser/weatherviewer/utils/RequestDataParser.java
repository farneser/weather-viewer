package com.farneser.weatherviewer.utils;

import com.farneser.weatherviewer.dto.LoginDto;
import com.farneser.weatherviewer.dto.RegisterDto;
import com.farneser.weatherviewer.dto.api.weather.Coordinates;
import com.farneser.weatherviewer.exceptions.ParamNotExistsException;
import com.farneser.weatherviewer.exceptions.PasswordsNotTheSameException;
import jakarta.servlet.http.HttpServletRequest;

public abstract class RequestDataParser {
    public static Coordinates getCoordinates(HttpServletRequest request) {
        var coordinates = new Coordinates();

        coordinates.setLat(ParameterParser.getDoubleParam(request, "lat"));
        coordinates.setLon(ParameterParser.getDoubleParam(request, "lon"));

        return coordinates;
    }

    public static RegisterDto getRegisterDto(HttpServletRequest request) throws ParamNotExistsException, PasswordsNotTheSameException {
        var registerDto = new RegisterDto();

        var loginDto = getLoginDto(request);

        registerDto.setUsername(loginDto.getUsername());
        registerDto.setPassword(loginDto.getPassword());

        var confirmPassword = ParameterParser.getStringParam(request, "confirmPassword");

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new ParamNotExistsException("confirmPassword");
        }

        registerDto.setConfirmPassword(confirmPassword);

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new PasswordsNotTheSameException();
        }

        return registerDto;
    }

    public static LoginDto getLoginDto(HttpServletRequest request) throws ParamNotExistsException {
        var loginDto = new LoginDto();

        var username = ParameterParser.getStringParam(request, "username");

        if (username == null || username.isEmpty()) {
            throw new ParamNotExistsException("username");
        }

        loginDto.setUsername(username);

        var password = ParameterParser.getStringParam(request, "password");

        if (password == null || password.isEmpty()) {
            throw new ParamNotExistsException("password");
        }

        loginDto.setPassword(password);

        return loginDto;
    }
}
