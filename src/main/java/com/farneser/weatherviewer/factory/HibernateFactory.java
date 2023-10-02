package com.farneser.weatherviewer.factory;

import com.farneser.weatherviewer.exceptions.InternalServerException;
import com.farneser.weatherviewer.models.Location;
import com.farneser.weatherviewer.models.Session;
import com.farneser.weatherviewer.models.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public abstract class HibernateFactory {

    private final static Logger logger = Logger.getLogger(HibernateFactory.class.getName());
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            build();
        }

        return sessionFactory;
    }

    public static void build() {
        logger.info("session factory == null");

        try {

            var databaseProperties = new Properties();

            var fisUrl = Objects.requireNonNull(HibernateFactory.class.getClassLoader().getResource("database.properties")).toURI().getPath();

            System.out.println(fisUrl);

            databaseProperties.load(new FileInputStream(fisUrl));

            logger.info("started creating configuration");

            var configuration = new Configuration()
                    .configure("hibernate.cfg.xml");

            configuration.setProperty("hibernate.connection.url", databaseProperties.getProperty("hibernate.connection.url"));
            configuration.setProperty("hibernate.connection.username", databaseProperties.getProperty("hibernate.connection.username"));
            configuration.setProperty("hibernate.connection.password", databaseProperties.getProperty("hibernate.connection.password"));

            if (!databaseProperties.getProperty("use_environment").isEmpty() && databaseProperties.getProperty("use_environment").equalsIgnoreCase("TRUE")) {

                if (System.getenv().containsKey("DATABASE_URL")) {
                    configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://" + System.getenv("DATABASE_URL"));
                }

                if (System.getenv().containsKey("DATABASE_USERNAME")) {
                    configuration.setProperty("hibernate.connection.username", System.getenv("DATABASE_USERNAME"));
                }

                if (System.getenv().containsKey("DATABASE_PASSWORD")) {
                    configuration.setProperty("hibernate.connection.password", System.getenv("DATABASE_PASSWORD"));
                }
            }

            logger.info("configuration successfully created");

            logger.info("hibernate.connection.url : " + configuration.getProperty("hibernate.connection.url"));
            logger.info("hibernate.connection.username : " + configuration.getProperty("hibernate.connection.username"));
            logger.info("hibernate.connection.password : " + configuration.getProperty("hibernate.connection.password"));
            logger.info("hibernate.connection.driver_class : " + configuration.getProperty("hibernate.connection.driver_class"));

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

            try {
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
            } catch (IllegalStateException e) {
                throw new InternalServerException("failed to connect the server");
            }
        } catch (Exception e) {
            shutdown();

            logger.warning("failed to create session factory");

            logger.warning(e.getMessage());
            logger.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    public static void shutdown() {
        if (registry != null) {

            logger.info("started destroying registry");

            StandardServiceRegistryBuilder.destroy(registry);

            logger.info("registry successfully destroyed");
        }
    }

    public static void main(String[] args) {
        HibernateFactory.build();
    }
}