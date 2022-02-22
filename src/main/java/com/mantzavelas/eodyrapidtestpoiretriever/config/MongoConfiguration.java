package com.mantzavelas.eodyrapidtestpoiretriever.config;

import com.mantzavelas.eodyrapidtestpoiretriever.repositories.TestSiteRepository;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = {TestSiteRepository.class})
@EnableScheduling
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    @Value("${mongodb.user}")
    private String username;
    @Value("${mongodb.password}")
    private String password;

    @Override
    protected String getDatabaseName() {
        return "eody-sites";
    }

    @Override
    public MongoClient reactiveMongoClient() {
        String mongoUser = "eody-sites-user";

        MongoCredential credential = MongoCredential.createCredential(mongoUser, username, password.toCharArray());
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress("localhost", 27017))))
            .credential(credential)
            .build();

        return MongoClients.create(settings);
    }

}
