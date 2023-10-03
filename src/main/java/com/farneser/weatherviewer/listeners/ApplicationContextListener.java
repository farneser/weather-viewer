package com.farneser.weatherviewer.listeners;

import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.factory.ApiUriFactory;
import com.farneser.weatherviewer.factory.HibernateFactory;
import com.farneser.weatherviewer.factory.ThymeleafFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.logging.Logger;

@WebListener("/")
public class ApplicationContextListener implements ServletContextListener {
    private final Logger logger = Logger.getLogger(ApplicationContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Start");

        HibernateFactory.build();

        try {
            ApiUriFactory.build(ApiUriFactory.getApiKey());
        } catch (InternalServerException e) {
            logger.warning("failed to build api url factory. cannot find api key");
        }

        ThymeleafFactory.build(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Starting closing hibernate session");

        HibernateFactory.getSessionFactory().close();

        logger.info("Hibernate session successfully closed");

        logger.info("Destroying the server");
    }
}