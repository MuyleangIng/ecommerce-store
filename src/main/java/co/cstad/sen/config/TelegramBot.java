package co.cstad.sen.config;

import co.cstad.sen.api.users.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${notifications.telegram.bot.id}")
    private String botId;
    @Value("${notifications.telegram.bot.username}")
    private String botUsername;
    @Value("${notifications.telegram.bot.token}")
    private String botToken;
    @Value("${notifications.telegram.chatId}")
    private String botChatId;

    @Override
    public void onUpdateReceived(Update update) {
        log.info("Update received: {}", update.getMessage().getChatId());
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botId + ":" + botToken;
    }

    public void sendMessageToGroup(User user, String subject, String messageText) {
        SendMessage sender = new SendMessage();
        sender.setChatId(botChatId);
        sender.setParseMode(ParseMode.HTML);
        sender.enableHtml(true);
        String ms = "<b>📩From: "+user.getUsername().toUpperCase()+"</b>\n";
        ms += "<b>📋Subject: "+subject+"</b>\n";
        ms+="--------------------------------\n\n";
        ms+=messageText;
        ms+="\n--------------------------------\n";
        ms+="<i>Message sent by "+ this.getBotName() +"</i>";
        sender.setText(ms);
        try {
            execute(sender);
        } catch (TelegramApiException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Error while sending message to group: "+ e.getMessage());
        }
    }

    private String getBotName() {
        String[] str = botUsername.split("_");
        return String.join(" ",str).toUpperCase();
    }
}
