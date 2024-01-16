package co.cstad.sen.utils;

import co.cstad.sen.api.users.User;
import co.cstad.sen.config.TelegramBot;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@AllArgsConstructor
@Slf4j
public class Notifications {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final TelegramBot telegramBot;
    public static String frontendUrl;
    public static String telegramChatId;
    public static String oneSignalAppId;
    public static String oneSignalRestApiKey;
    public static String frontendVerifyUrl;
    public static String frontendNewPasswordUrl;

    @Value("${frontend.verifyUrl}")
    public void setFrontendVerifyUrl(String verifyUrl) {
        frontendVerifyUrl = verifyUrl;
    }

    @Value("${frontend.newPasswordUrl}")
    public void setFrontendNewPasswordUrl(String pwdUrl) {
        frontendNewPasswordUrl = pwdUrl;
    }

    @Value("${frontend.url}")
    public void setFrontendUrl(String url) {
        frontendUrl = url;
    }

    @Value("${notifications.telegram.chatId}")
    public void setTelegramChatId(String chatId) {
        telegramChatId = chatId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Meta <T>{
        private String to;
        private String from;
        private String subject;
        private String templateUrl;
        private T data;
    }


    public void sendEmail(Notifications.Meta<?> meta, User user) {
        /*
         * 1. prepare template
         * 2. Prepare email information
         * 3. send mail
         *
         */
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        // Set template engin
        Context context = new Context();
        context.setVariable("data", meta.data);
        context.setVariable("frontendUrl", frontendUrl);
        context.setVariable("urlVerify",frontendUrl.concat(frontendVerifyUrl) + "?email="+ user.getEmail()+ "&verifyCode=" + user.getVerifiedCode());
        context.setVariable("urlForgotPassword",frontendUrl.concat(frontendNewPasswordUrl)+"?email="+user.getEmail()+"&verifyCode="+user.getVerifiedCode());
        String htmlTemplate = templateEngine.process(meta.templateUrl,context);

        try {
            //  prepare template
            helper.setText(htmlTemplate,true);

            // prepare mail information
            helper.setTo(meta.to);
            helper.setFrom(meta.from);
            helper.setSubject(meta.subject);

            // Send mail
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error sending email.");
        }
    }

    public void sendTelegramBot(User user,String subject,String message){
        if (telegramChatId != null && !telegramChatId.isEmpty()) {
            telegramBot.sendMessageToGroup(user,subject,message);
        }
    }
}
