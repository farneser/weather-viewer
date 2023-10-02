package com.farneser.weatherviewer.factory;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
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

    public static WebContext buildWebContext(HttpServletRequest req, HttpServletResponse resp, ServletContext servletContext) {
        var application = JakartaServletWebApplication.buildApplication(servletContext);
        var webExchange = application.buildExchange(req, resp);

        return new WebContext(webExchange);
    }
}
