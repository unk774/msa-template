package ru.imageprocessing.aws_storage.service.s3;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class S3FileMetaData {

    private final String fileId;
    private final long size;
    private final String format;
    private final Instant lastModified;

}
