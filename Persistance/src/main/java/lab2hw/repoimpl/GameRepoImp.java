package lab2hw.repoimpl;

import lab2hw.Game;
import lab2hw.GameRepo;
import lab2hw.Player;
import lab2hw.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameRepoImp implements GameRepo {

    private static final Logger logger = LogManager.getLogger(PlayerRepository.class);

    @Override
    public List<Game> findAll() {
        logger.info("Returnez toate sesiunile de joc");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Game ", Game.class).list();
        }
    }

    @Override
    public Game save(Game game) {
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
    public Game modifyFinal(Game game){
        logger.info("Salvez sesiunea de joc: {}", game.getId());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            game.setFinished(true);
            session.update(game);   // Hibernate știe ce s-a modificat
            transaction.commit();
        }
        catch (Exception e){
            if (transaction != null) transaction.rollback();
            logger.info("fail to update joc: {}", game.getId());
        }
        return null;
    }
}
