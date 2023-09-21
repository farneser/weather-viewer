package com.farneser.weatherviewer.helpers.factory;

import com.farneser.weatherviewer.dto.LoginDto;
import com.farneser.weatherviewer.dto.RegisterDto;
import com.farneser.weatherviewer.exceptions.ParamNotExistsException;
import com.farneser.weatherviewer.exceptions.PasswordsNotTheSameException;
import com.farneser.weatherviewer.helpers.parser.ParameterParser;
import jakarta.servlet.http.HttpServletRequest;

public abstract class UserDtoFactory {
    public static RegisterDto getRegister(HttpServletRequest request) throws ParamNotExistsException, PasswordsNotTheSameException {
        var registerDto = new RegisterDto();

        var loginDto = getLogin(request);

        registerDto.setUsername(loginDto.getUsername());

        registerDto.setPassword(loginDto.getPassword());

        var confirmPassword = ParameterParser.getString(request, "confirmPassword");

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new ParamNotExistsException("confirmPassword");
        }

        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new PasswordsNotTheSameException();
        }

        registerDto.setConfirmPassword(confirmPassword);

        return registerDto;
    }

    public static LoginDto getLogin(HttpServletRequest request) throws ParamNotExistsException {
        var loginDto = new LoginDto();

        var username = ParameterParser.getString(request, "username");

        if (username == null || username.isEmpty()) {
            throw new ParamNotExistsException("username");
        }

        loginDto.setUsername(username);

        var password = ParameterParser.getString(request, "password");

        if (password == null || password.isEmpty()) {
            throw new ParamNotExistsException("password");
        }

        loginDto.setPassword(password);

        return loginDto;
    }
}
