package com.farneser.weatherviewer.helpers.factory;

import jakarta.servlet.ServletContext;
import lombok.Getter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

public abstract class ThymeleafFactory {
    @Getter
    private static TemplateEngine instance;

    public static void build(ServletContext servletContext) {
        var application = JakartaServletWebApplication.buildApplication(servletContext);
        var templateResolver = new WebApplicationTemplateResolver(application);

        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode("HTML");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        instance = templateEngine;
    }
}
