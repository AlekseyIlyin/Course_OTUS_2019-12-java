package ru.otus.configs;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import ru.otus.repository.MongoDbInitializer;
import ru.otus.repository.UserRepository;

@Configuration
@ComponentScan(basePackages = "ru.otus")
@PropertySource("classpath:app.properties")
public class MongoConfig {

  @Value("${mongo.url}")
  private String hostDb;

  @Value("${mongo.dbName}")
  private String dbName;

  @Bean
  public MongoClient getMongoClient() {
    return MongoClients.create(hostDb);
  }

  @Bean
  public MongoOperations mongoOperations() {

    MongoOperations mongoOperations = null;
    try {
      mongoOperations = new MongoTemplate(getMongoClient(), dbName);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mongoOperations;
  }

  @Bean(initMethod = "init")
  public MongoDbInitializer createMongoDbInitializer(UserRepository userRepository) {
    return new MongoDbInitializer(mongoOperations());
  }

  public @Bean MongoDatabaseFactory mongoDatabaseFactory() {
    return new SimpleMongoClientDatabaseFactory(getMongoClient(), dbName);
  }
}
