//package io.incondensable.application.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.core.MongoClientFactoryBean;
//
//import java.util.Objects;
//
///**
// * @author abbas
// */
//@Configuration
//public class MongoDBConfiguration {
//
//    @Value("${spring.data.mongodb.uri}")
//    private String url;
//
//    @Value("${spring.data.mongodb.database}")
//    private String db;
//
//    @Bean
//    public MongoClient mongoClient() {
//        MongoClient client = MongoClients.create(new ConnectionString(url));
//        client.getDatabase(db);
//        return client;
//    }
//
//    @Bean
//    public MongoClientFactoryBean mongodb() {
//        MongoClientFactoryBean factory = new MongoClientFactoryBean();
//        ConnectionString connection = new ConnectionString(url);
//        factory.setConnectionString(connection);
//
//        try {
//            MongoClient client = factory.getObject();
//            Objects.requireNonNull(client).getDatabase(db);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return factory;
//    }
//
//}
