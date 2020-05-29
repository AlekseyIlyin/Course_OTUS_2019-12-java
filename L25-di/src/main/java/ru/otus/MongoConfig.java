package ru.otus;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.repository.MongoInitDb;
import ru.otus.repository.UserRepository;

@Configuration
@ComponentScan(basePackages = { "ru.otus.**" })
@PropertySource("classpath:app.properties")
public class MongoConfig {
  @Autowired
  private Environment env;

  @Value("${mongo.url}")
  private String hostDb;

  @Value("${mongo.dbName}")
  private String dbName;

  @Bean
  public MongoClient getMongoClient(String url) {
    return MongoClients.create(url);
  }

  @Bean
  public MongoOperations mongoOperations() {

    MongoOperations mongoOperations = null;
    try {
      mongoOperations = new MongoTemplate(getMongoClient(hostDb), dbName);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mongoOperations;
  }

  @Bean(initMethod = "init")
  public MongoInitDb createMongoDbInitializer(UserRepository userRepository) {
    return new MongoInitDb(new MongoTemplate(getMongoClient(hostDb), dbName));
  }


}
