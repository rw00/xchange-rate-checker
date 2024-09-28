package com.rw.apps.xchange.ratechecker;

import java.io.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Profile("!" + Constants.LOCAL_TEST_PROFILE)
public class TelegramBotSender {
    private final DefaultAbsSender botSender;
    private final String channelChatId;

    public TelegramBotSender(@Value("${bot.telegram.token}") String botToken, @Value("${bot.telegram.channel-chat-id}") String channelChatId) {
        botSender = new DefaultAbsSender(new DefaultBotOptions(), botToken) {
        };
        this.channelChatId = channelChatId;
    }

    public void sendText(String messageText) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(channelChatId);
        message.setText(messageText);
        botSender.execute(message);
    }

    public void sendPhoto(String fileName) throws TelegramApiException {
        SendPhoto message = new SendPhoto();
        message.setChatId(channelChatId);
        message.setPhoto(new InputFile(new File(fileName)));
        botSender.execute(message);
    }
}
