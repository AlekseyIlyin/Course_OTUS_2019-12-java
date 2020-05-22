package ru.otus;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.domain.AddressDataSet;
import ru.otus.domain.PhoneDataSet;
import ru.otus.domain.User;
import ru.otus.hibernate.DbCreater;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.repostory.UserRepository;

@Configuration
@ComponentScan
public class DatabaseConfig {
    @Bean
    public SessionFactory createSessionFactory() {
        return HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class, AddressDataSet.class, PhoneDataSet.class);
    }

    @Bean(initMethod = "init")
    public DbCreater createDbInitializer(UserRepository userRepository) {
        return new DbCreater(userRepository);
    }
}