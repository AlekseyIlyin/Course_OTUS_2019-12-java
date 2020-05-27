package ru.otus;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
@ComponentScan
public class MongoConfig {
  private static final String CONFIG_NAME = "realm.properties";
  private static final String MONGO_HOST = "mongodb://localhost:27017";
  private static final String MONGO_DB_NAME = "users_db";

  private String mongoEnv;

  @Bean
  public MongoClient getMongoClient(String url) {
    return MongoClients.create(url);
  }

  @Bean
  public MongoTemplate mongoTemplate() {
/*
    Properties dbProperties = getDbProperties();
    String hostDb = null;
    String dbName = null;
    if (dbProperties != null) {
      hostDb = dbProperties.getProperty("MongoURL");
      dbName = dbProperties.getProperty("MongoDbName");
    } else {
*/
    String hostDb = MONGO_HOST;
    String dbName = MONGO_DB_NAME;
//    }

    MongoTemplate mongoTemplate = null;
    try {
      mongoTemplate = new MongoTemplate(getMongoClient(hostDb), dbName);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return mongoTemplate;
  }

  private Properties getDbProperties() {
    ClassLoader classLoader = MongoConfig.class.getClassLoader();
    File file = new File(classLoader.getResource(CONFIG_NAME).getFile());
    Properties properties = new Properties();
          try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
          } catch (IOException e) {
            properties = null;
          }
    return properties;
  }


}
