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
import java.util.Properties;
import java.util.logging.Logger;

public abstract class HibernateFactory {

    private final static Logger LOGGER = Logger.getLogger(HibernateFactory.class.getName());
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            build();
        }

        return sessionFactory;
    }

    public static void build() {
        LOGGER.info("session factory == null");
        try {

            var databaseProperties = new Properties();
            var resourceUrl = HibernateFactory.class.getClassLoader().getResource("database.properties");

            LOGGER.info("started creating configuration");

            var configuration = new Configuration()
                    .configure("hibernate.cfg.xml");

            if (resourceUrl != null) {
                databaseProperties.load(new FileInputStream(resourceUrl.toURI().getPath()));
                configuration.setProperty("hibernate.connection.url", databaseProperties.getProperty("hibernate.connection.url"));
                configuration.setProperty("hibernate.connection.username", databaseProperties.getProperty("hibernate.connection.username"));
                configuration.setProperty("hibernate.connection.password", databaseProperties.getProperty("hibernate.connection.password"));
            } else {
                databaseProperties = configuration.getProperties();
            }

            if (databaseProperties.getProperty("use_environment") != null && !databaseProperties.getProperty("use_environment").isEmpty() && databaseProperties.getProperty("use_environment").equalsIgnoreCase("TRUE")) {
                buildConfWithEnv(configuration);
            }

            LOGGER.info("configuration successfully created");
            showDbConfiguration(configuration);
            LOGGER.info("started creating registry");

            registry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .configure()
                    .build();

            LOGGER.info("registry successfully created");
            LOGGER.info("started creating sources");

            var sources = new MetadataSources(registry)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Session.class)
                    .addAnnotatedClass(Location.class);

            LOGGER.info("sources successfully created");

            try {
                buildSessionFactory(sources);
            } catch (IllegalStateException e) {
                throw new InternalServerException("failed to connect the server");
            }
        } catch (Exception e) {
            shutdown();

            LOGGER.warning("failed to create session factory");
            LOGGER.warning(e.getMessage());
            LOGGER.warning(Arrays.toString(e.getStackTrace()));
        }
    }

    private static void buildSessionFactory(MetadataSources sources) {
        LOGGER.info("started creating metadata");

        var metadata = sources
                .getMetadataBuilder()
                .build();

        LOGGER.info("metadata successfully created");
        LOGGER.info("started creating session factory");

        sessionFactory = metadata
                .getSessionFactoryBuilder()
                .build();

        LOGGER.info("session factory successfully created");
    }

    private static void showDbConfiguration(Configuration configuration) {
        LOGGER.info("hibernate.connection.url : " + configuration.getProperty("hibernate.connection.url"));
        LOGGER.info("hibernate.connection.username : " + configuration.getProperty("hibernate.connection.username"));
        LOGGER.info("hibernate.connection.password : " + configuration.getProperty("hibernate.connection.password"));
        LOGGER.info("hibernate.connection.driver_class : " + configuration.getProperty("hibernate.connection.driver_class"));
    }

    private static void buildConfWithEnv(Configuration configuration) {
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

    public static void shutdown() {
        if (registry != null) {

            LOGGER.info("started destroying registry");

            StandardServiceRegistryBuilder.destroy(registry);

            LOGGER.info("registry successfully destroyed");
        }
    }

    public static void main(String[] args) {
        HibernateFactory.build();
    }
}