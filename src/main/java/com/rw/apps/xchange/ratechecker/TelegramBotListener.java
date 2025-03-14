package com.rw.apps.xchange.ratechecker;

import java.util.Optional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class TelegramBotListener extends TelegramLongPollingBot {
    private final OnStartAppRunner basicRunner;
    @Getter
    private final String botUsername;

    public TelegramBotListener(@Value("${bot.telegram.token}") String botToken, @Value("${bot.telegram.username}") String botUsername, OnStartAppRunner basicRunner) {
        super(botToken);
        this.botUsername = botUsername;
        this.basicRunner = basicRunner;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Optional<Message> message = Optional.of(update).map(Update::getMessage);
        String text = message.map(Message::getText).orElse("null");
        if ("/check".equals(text)) {
            basicRunner.run(null);
        }
    }
}
