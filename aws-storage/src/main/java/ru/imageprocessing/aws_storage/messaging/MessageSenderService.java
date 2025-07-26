package ru.imageprocessing.aws_storage.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSenderService {

    private final StreamBridge streamBridge;

    public void sendFileUploaded(FileUploadedDto dto) {
        streamBridge.send("upload-out-0", dto);
    }
}