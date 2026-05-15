package repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = initializeSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory initializeSessionFactory() {
        // Citeste configuratia din hibernate.cfg.xml
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // In caz de eroare, distrugem registrul
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Eroare la initializarea Hibernate: " + e.getMessage());
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}