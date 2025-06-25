package lab2hw;

import java.util.Optional;

public interface ConfigInterface extends Repository<Long, Configuration>{
    //TODO modificare eventual
    Optional<Configuration> getConfigurationById(long id);

    Configuration updateConfiguration(Configuration configuration);
}
