package com.mantzavelas.eodyrapidtestpoiretriever.config;

import com.mantzavelas.eodyrapidtestpoiretriever.repositories.TestSiteRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = {TestSiteRepository.class})
@EnableScheduling
public class ApplicationConfig {

    @Bean
    public WebFluxConfigurer corsConfigurer() {
        return new WebFluxConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("*")
                    .maxAge(3600);
            }
        };
    }
}
