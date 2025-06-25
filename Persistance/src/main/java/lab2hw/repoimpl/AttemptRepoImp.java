package lab2hw.repoimpl;

import lab2hw.Attempt;
import lab2hw.AttemptRepo;
import lab2hw.Player;
import lab2hw.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.internal.Collections;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttemptRepoImp implements AttemptRepo {

    private static final Logger logger = LogManager.getLogger(PlayerRepository.class);

    @Override
    public List<Attempt> findAll() {
        logger.info("Returnez toate incercarile");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Attempt ", Attempt.class).list();
        }
    }

    @Override
    public Attempt save(Attempt attempt) {
        logger.info("Salvez incercarea: {}", attempt.getId());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(attempt);
            transaction.commit();
            return attempt;
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
           logger.info("Salvare esuata!");
        }
        return null;
    }

    @Override
    public List<Attempt> findByPlayerId(Long id){
        logger.info("Returnez toate incercarile");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Attempt a WHERE a.player.id = :playerId", Attempt.class)
                    .setParameter("playerId", id)
                    .getResultList();
        }catch (Exception e){
            logger.info("Returnare esuata!");
            return List.of();
        }
    }

    @Override
    public List<Attempt> findByGameId(Long id){
        logger.info("Returnez toate incercarile");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Attempt a WHERE a.game.id = :gameId", Attempt.class)
                    .setParameter("gameId", id)
                    .getResultList();
        }catch (Exception e){
            logger.info("Returnare esuata!");
        }
        return List.of();
    }
}
