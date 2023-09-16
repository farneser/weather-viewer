package com.farneser.weatherviewer.helpers.factory;

import com.farneser.weatherviewer.models.Location;
import com.farneser.weatherviewer.models.Session;
import com.farneser.weatherviewer.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;
import java.util.logging.Logger;


public class HibernateFactory {

    private final static Logger logger = Logger.getLogger(HibernateFactory.class.getName());
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {

            logger.info("session factory == null");

            try {

                logger.info("started creating configuration");

                var configuration = new Configuration()
                        .configure("hibernate.cfg.xml");

                var serverIp = "localhost";

                if (System.getenv().containsKey("IP_ENV_VARIABLE")) {
                    serverIp = System.getenv("IP_ENV_VARIABLE");
                }

                var serverPort = "5432";

                if (System.getenv().containsKey("PORT_ENV_VARIABLE")) {
                    serverPort = System.getenv("PORT_ENV_VARIABLE");
                }

                var serverDbName = "weather-viewer";

                if (System.getenv().containsKey("WEATHER_DB_NAME_ENV_VARIABLE")) {
                    serverDbName = System.getenv("WEATHER_DB_NAME_ENV_VARIABLE");
                }

                var jdbcConnectionString = "jdbc:postgresql://" + serverIp + ":" + serverPort + "/" + serverDbName;

                configuration.setProperty("hibernate.connection.url", jdbcConnectionString);

                var serverUsername = "postgres";

                if (System.getenv().containsKey("USERNAME_ENV_VARIABLE")) {
                    serverUsername = System.getenv("USERNAME_ENV_VARIABLE");
                }

                var serverPassword = "postgres";

                if (System.getenv().containsKey("PASSWORD_ENV_VARIABLE")) {
                    serverPassword = System.getenv("PASSWORD_ENV_VARIABLE");
                }

                configuration.setProperty("hibernate.connection.username", serverUsername);
                configuration.setProperty("hibernate.connection.password", serverPassword);

                logger.info("configuration successfully created");

                logger.info("hibernate.connection.url : " + jdbcConnectionString);
                logger.info("hibernate.connection.username : " + serverUsername);
                logger.info("hibernate.connection.password : " + serverPassword);

                logger.info("started creating registry");

                registry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .configure()
                        .build();

                logger.info("registry successfully created");

                logger.info("started creating sources");

                var sources = new MetadataSources(registry)
                        .addAnnotatedClass(User.class)
                        .addAnnotatedClass(Session.class)
                        .addAnnotatedClass(Location.class);

                logger.info("sources successfully created");

                logger.info("started creating metadata");

                var metadata = sources
                        .getMetadataBuilder()
                        .build();

                logger.info("metadata successfully created");

                logger.info("started creating session factory");

                sessionFactory = metadata
                        .getSessionFactoryBuilder()
                        .build();

                logger.info("session factory successfully created");

            } catch (Exception e) {
                shutdown();

                logger.warning("failed to create session factory");

                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        return sessionFactory;
    }

    public static void shutdown() {
        if (registry != null) {

            logger.info("started destroying registry");

            StandardServiceRegistryBuilder.destroy(registry);

            logger.info("registry successfully destroyed");
        }
    }
}