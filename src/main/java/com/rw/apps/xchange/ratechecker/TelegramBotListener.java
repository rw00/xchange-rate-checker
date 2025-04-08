package com.rw.apps.xchange.ratechecker;

import java.util.Optional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
@Slf4j
public class TelegramBotListener extends TelegramLongPollingBot {
    private final RateCheckerService rateCheckerService;
    private final TelegramBotSender botSender;
    @Getter
    private final String botUsername;

    public TelegramBotListener(@Value("${bot.telegram.token}") String botToken,
                               @Value("${bot.telegram.username}") String botUsername,
                               RateCheckerService rateCheckerService,
                               TelegramBotSender botSender) {
        super(botToken);
        this.botUsername = botUsername;
        this.rateCheckerService = rateCheckerService;
        this.botSender = botSender;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Optional<Message> message = Optional.of(update).map(Update::getMessage);
        String text = message.map(Message::getText).orElse("null");
        if ("/check".equals(text)) {
            runCheck();
        }
    }

    private void runCheck() {
        try {
            if (rateCheckerService.check(true)) {
                String file = rateCheckerService.saveGraph();

                botSender.sendPhoto(file);
            }
        } catch (Exception e) {
            try {
                botSender.sendText("Error running check: " + e.getMessage());
            } catch (Exception ex) {
                log.warn("Error sending error message: {}", ex.getMessage());
            }
        }
    }
}
