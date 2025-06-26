package lab2hw.repoimpl;

import lab2hw.*;
import lab2hw.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ConfigGameImp implements ConfigInterface {

    private static final Logger logger = LogManager.getLogger(PlayerRepository.class);

    @Override
    public List<Configuration> findAll() {
        logger.info("Returnez toate sesiunile de joc");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Configuration ", Configuration.class).list();
        }
    }

    @Override
    public Configuration save(Configuration game) {
        logger.info("Salvez sesiunea de joc: {}", game.getId());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(game);
            transaction.commit();
            return game;
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Configuration> getConfigurationById(long id){
        logger.info("Returnez tot sesiunile de joc: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Configuration c = session
                    .createQuery("FROM Configuration WHERE id = :id", Configuration.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(c);
        }
    }

    @Override
    public Configuration updateConfiguration(Configuration configuration){
        logger.info("update la configuration: {}", configuration.getId());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Configuration managed = session.get(Configuration.class, configuration.getId());
            if (managed == null) {
                throw new IllegalArgumentException(
                        "Nu există Configuration cu id=" + configuration.getId()
                );
            }

            managed.setValues(configuration.getValues());
            tx.commit();
            return managed;
        }
    }


}
