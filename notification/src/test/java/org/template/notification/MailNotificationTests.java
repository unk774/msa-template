package org.template.notification;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.template.notification.service.MailNotificationChannel;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.template.notification.api.dto.MetaInfo;
import org.template.notification.api.dto.UniversalNotification;

import java.time.Instant;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class MailNotificationTests {

    @MockitoBean
    private SpringTemplateEngine springTemplateEngine;

    @MockitoBean
    private JavaMailSender mailSender;

    @MockitoBean
    private MimeMessage mimeMessage;

    @Autowired
    private MailNotificationChannel mailNotificationChannel;

    @BeforeEach
    public void initMocks() {
        String expectedResult = "processed template";
        when(springTemplateEngine.process(anyString(), any(IContext.class)))
                .thenReturn(expectedResult);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    public void verifySend() {
        mailNotificationChannel.sendNotification(getUniversalNotification("test"));
        verify(mailSender).send(mimeMessage);
    }

    @Test
    public void verifyNotSend() {
        mailNotificationChannel.sendNotification(getUniversalNotification(null));
        mailNotificationChannel.sendNotification(getUniversalNotification(" "));
        verifyNoInteractions(springTemplateEngine);
        verifyNoInteractions(mailSender);
    }

    private UniversalNotification getUniversalNotification(String email) {
        MetaInfo metaInfo = new MetaInfo();
        metaInfo.setNotificationChannel(MetaInfo.NotificationChannelEnum.EMAIL);
        metaInfo.setSender("noreply@noreply");
        metaInfo.setRecipient(email);
        metaInfo.setDate(Date.from(Instant.now()));
        metaInfo.setSubject("Uploaded file link");
        metaInfo.setTemplateFormat(MetaInfo.TemplateFormatEnum.HTML);
        metaInfo.setTemplateCode("notification-template.html");

        UniversalNotification universalNotification = new UniversalNotification();
        universalNotification.setMetaInfo(metaInfo);
        return universalNotification;
    }
}
