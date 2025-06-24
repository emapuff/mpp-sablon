package lab2hw.repoimpl;

import lab2hw.Player;
import lab2hw.PlayerInterface;
import lab2hw.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;


import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class PlayerRepository implements PlayerInterface {

    private static final Logger logger = LogManager.getLogger(PlayerRepository.class);
    @Override
    public Optional<Player> findPlayerByNickname(String nickname){
        logger.info("Caut jucătorul cu nickname-ul: {}", nickname);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Player player = session
                    .createQuery("FROM Player WHERE nickname = :nickname", Player.class)
                    .setParameter("nickname", nickname)
                    .uniqueResult();
            return Optional.ofNullable(player);
        }
    }

    @Override
    public Optional<Player> findPlayerById(Long id){
        logger.info("Caut jucătorul cu id-ul: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Player player = session
                    .createQuery("FROM Player WHERE id = :id", Player.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(player);
        }
    }

    @Override
    public List<Player> findAll() {
        logger.info("Returnez toti jucatorii");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Player ", Player.class).list();
        }
    }

    @Override
    public Player save(Player player) {
        logger.info("Salvez jucătorul: {}", player.getNickname());
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(player);
            transaction.commit();
            return player;
        }catch (Exception e){
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
        return null;
    }
}
