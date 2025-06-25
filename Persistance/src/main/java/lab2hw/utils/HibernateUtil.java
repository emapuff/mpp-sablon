package lab2hw.utils;

import lab2hw.Attempt;
import lab2hw.Game;
import lab2hw.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = createNewSessionFactory();
        }
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        try {
            Configuration config = new Configuration();
            config.configure();
            config.addAnnotatedClass(lab2hw.Player.class);
            config.addAnnotatedClass(lab2hw.Game.class);
            config.addAnnotatedClass(lab2hw.Attempt.class);
            config.addAnnotatedClass(lab2hw.Configuration.class);

            return config.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Eroare la crearea SessionFactory");
        }
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
