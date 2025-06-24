package lab2hw;

import lab2hw.repoimpl.PlayerRepository;
import lab2hw.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public interface AttemptRepo extends Repository<Long, Attempt>{

    List<Attempt> findByPlayerId(Long id);

}
