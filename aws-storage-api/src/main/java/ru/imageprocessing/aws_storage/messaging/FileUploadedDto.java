package ru.imageprocessing.aws_storage.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileUploadedDto {
    private String login;
    private String url;
    private String presignedUrl;
}
