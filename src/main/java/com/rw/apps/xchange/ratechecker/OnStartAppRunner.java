package com.rw.apps.xchange.ratechecker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class OnStartAppRunner implements ApplicationRunner {
    private final RateCheckerService rateCheckerService;
    private final TelegramBotSender botSender;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (rateCheckerService.check()) {
            String file = rateCheckerService.saveGraph();

            botSender.sendPhoto(file);
        }
    }
}
