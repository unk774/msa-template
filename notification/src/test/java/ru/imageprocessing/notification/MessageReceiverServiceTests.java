package ru.imageprocessing.notification;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.imageprocessing.auth.api.dto.UserResponse;
import ru.imageprocessing.aws_storage.messaging.FileUploadedDto;
import ru.imageprocessing.notification.api.dto.UniversalNotification;
import ru.imageprocessing.notification.feign.UserRegistryClient;
import ru.imageprocessing.notification.messaging.MessageReceiverService;
import ru.imageprocessing.notification.service.NotificationChannel;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MessageReceiverServiceTests {

    @MockitoBean
    private UserRegistryClient userRegistryClient;

    @MockitoBean
    private NotificationChannel notificationChannel;

    @Autowired
    private MessageReceiverService messageReceiverService;

    @Test
    public void testUploadMessageProcessing() {
        FileUploadedDto fileUploadedDto = new FileUploadedDto();
        fileUploadedDto.setLogin("testuser");
        fileUploadedDto.setPresignedUrl("http://example.com/file");

        UserResponse user = new UserResponse();
        user.setEmail("test@example.com");
        user.setFirstName("Test");
        user.setLastName("User");

        Message<FileUploadedDto> message = MessageBuilder.withPayload(fileUploadedDto).build();

        when(userRegistryClient.getUserByLogin("testuser"))
                .thenReturn(org.springframework.http.ResponseEntity.ok(user));

        List<NotificationChannel> channels = Collections.singletonList(notificationChannel);
        messageReceiverService = new MessageReceiverService(userRegistryClient, channels);

        messageReceiverService.upload().accept(message);

        verify(userRegistryClient).getUserByLogin("testuser");
        verify(notificationChannel).sendNotification(any(UniversalNotification.class));
    }

    @Test
    public void testUploadMessageWithEmptyEmail() {
        FileUploadedDto fileUploadedDto = new FileUploadedDto();
        fileUploadedDto.setLogin("testuser");

        UserResponse user = new UserResponse();
        user.setEmail("");

        Message<FileUploadedDto> message = MessageBuilder.withPayload(fileUploadedDto).build();

        when(userRegistryClient.getUserByLogin("testuser"))
                .thenReturn(org.springframework.http.ResponseEntity.ok(user));

        messageReceiverService.upload().accept(message);

        verify(userRegistryClient).getUserByLogin("testuser");
        verifyNoInteractions(notificationChannel);
    }
}