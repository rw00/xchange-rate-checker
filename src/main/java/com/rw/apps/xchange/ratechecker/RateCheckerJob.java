package com.rw.apps.xchange.ratechecker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class RateCheckerJob {
    private final OnStartAppRunner basicRunner;

    @Scheduled(cron = "0 0 * * * *", zone = "Europe/Amsterdam")
    public void run() {
        try {
            basicRunner.run(null);
        } catch (Exception e) {
            log.error("Error running scheduled job", e);
        }
    }
}
