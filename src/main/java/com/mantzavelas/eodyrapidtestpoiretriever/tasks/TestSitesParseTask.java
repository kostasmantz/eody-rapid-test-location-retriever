package com.mantzavelas.eodyrapidtestpoiretriever.tasks;

import com.mantzavelas.eodyrapidtestpoiretriever.scrape.EodySiteParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class TestSitesParseTask {

    private final EodySiteParser siteParser;

    @Autowired
    public TestSitesParseTask(EodySiteParser siteParser) {
        this.siteParser = siteParser;
    }

    @Scheduled(fixedRate = 43200000, initialDelay = 60000)
    public void parseTestSites() {
        log.info("Starting eody rapid test sites parsing at " + Instant.now().toString());
        siteParser.parse();

        log.info("Ended eody rapid test sites parsing at " + Instant.now().toString());
    }
}
