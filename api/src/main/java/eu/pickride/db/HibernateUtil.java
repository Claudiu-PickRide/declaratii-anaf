package eu.pickride.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Hibernate utility class that bootstraps EntityManagerFactory
 * from environment variables.
 */
public class HibernateUtil {

    private static final EntityManagerFactory emf;

    static {
        try {
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASS");

            if (url == null || user == null || pass == null) {
                throw new IllegalStateException("Database environment variables not set (DB_URL, DB_USER, DB_PASS)");
            }

            Map<String, String> props = new HashMap<>();
            props.put("jakarta.persistence.jdbc.driver", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
            props.put("jakarta.persistence.jdbc.url", url);
            props.put("jakarta.persistence.jdbc.user", user);
            props.put("jakarta.persistence.jdbc.password", pass);

            // Hibernate-specific properties
            props.put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
            props.put("hibernate.hbm2ddl.auto", "update"); // auto-create/update schema
            props.put("hibernate.show_sql", "false");
            props.put("hibernate.format_sql", "true");

            emf = Persistence.createEntityManagerFactory("myPU", props);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to initialize Hibernate: " + e.getMessage());
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

