package ru.imageprocessing.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.imageprocessing.notification.api.dto.MetaInfo;
import ru.imageprocessing.notification.api.dto.UniversalNotification;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailNotificationChannel implements NotificationChannel {

    private final SpringTemplateEngine springTemplateEngine;
    private final JavaMailSender mailSender;

    @Override
    public void sendNotification(UniversalNotification universalNotification) {
        if (!MetaInfo.NotificationChannelEnum.EMAIL.equals(universalNotification.getMetaInfo().getNotificationChannel())) {
            return;
        }
        if (universalNotification.getMetaInfo() == null) {
            log.error("mail notification metadata is empty");
            return;
        }
        if (StringUtils.isBlank(universalNotification.getMetaInfo().getRecipient())) {
            log.error("mail notification recipient is empty");
            return;
        }
        try {
            var context = new Context();
            context.setVariables(universalNotification.getData());

            String filledTemplate = springTemplateEngine.process(
                    universalNotification.getMetaInfo().getTemplateCode(),
                    context
            );

            sendEmail(universalNotification.getMetaInfo(), filledTemplate);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendEmail(MetaInfo metaInfo, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setFrom(metaInfo.getSender());
        helper.setTo(metaInfo.getRecipient().split(";"));
        helper.setSubject(metaInfo.getSubject());
        helper.setText(text, metaInfo.getTemplateFormat().equals(MetaInfo.TemplateFormatEnum.HTML));
        mailSender.send(mimeMessage);
    }
}
