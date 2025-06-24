package lab2hw;

import lab2hw.utils.HibernateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//TODO aici erau ceva componente
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})

public class Main {
    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        SpringApplication.run(Main.class, args);

    }

    @Bean(name = "props")
    public Properties getProperties() {
        Properties props = new Properties();
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream("server.properties")) {
            if (fis != null) {
                props.load(fis);
            } else {
                System.err.println("server.properties not found in classpath.");
            }
        } catch (IOException e) {
            System.err.println("Error loading server.properties: " + e);
        }
        return props;
    }
}
