package ru.imageprocessing.aws_storage.service.s3;

import java.io.InputStream;

public interface S3Downloader {

    public void download(InputStream inputStream);
}
