package ru.imageprocessing.aws_storage.service.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.GetObjectAttributesRequest;
import software.amazon.awssdk.services.s3.model.GetObjectAttributesResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.ObjectAttributes;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    @Value("${s3.service.endpoint:}")
    private String serviceEndpoint;

    @Value("${s3.forcePathStyle:false}")
    private boolean forcePathStyle;

    private final S3Client s3Client;

    public void downloadFileRange(String bucketName, String objectKey, long from, long to, S3Downloader s3Downloader) {
        log.info("downloadFileRange/{}/{}/{}/{}", bucketName, objectKey, from, to);
        try (InputStream inputStream = s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .range(String.format("bytes=%d-%d", from, to))
                .build())) {
            s3Downloader.download(inputStream);
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    public void deleteFile(String bucketName, String objectKey) {
        log.info("deleteFile/{}/{}", bucketName, objectKey);
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build());
    }

    public boolean objectKeyExists(String bucketName, String objectKey) {
        log.info("objectKeyExists/{}/{}", bucketName, objectKey);
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName).key(objectKey)
                    .build());
            return true;
        } catch (NoSuchKeyException noSuchKeyException) {
            return false;
        }
    }

    public void deleteFiles(String bucketName, String... objectKeys) {
        log.info("deleteFiles/{}/{}",bucketName, String.join(",", objectKeys));
        List<ObjectIdentifier> list = Stream.of(objectKeys)
                .map(objectKey -> ObjectIdentifier.builder().key(objectKey).build())
                .toList();
        s3Client.deleteObjects(DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(c -> c.objects(list))
                .build());
        log.info("deleteFiles/deleted/{}/{}",bucketName, String.join(",", objectKeys));
    }

    public void uploadStream(String bucketName, String key,
                                    InputStream inputStream, long contentLength) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                RequestBody.fromInputStream(inputStream, contentLength));
    }

    public long getFileSize(String bucketName, String objectKey) {
        log.info("getFileSize/complete/{}/{}", bucketName, objectKey);
        GetObjectAttributesResponse response = s3Client.getObjectAttributes(GetObjectAttributesRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .objectAttributes(ObjectAttributes.OBJECT_SIZE)
                .build());
        return response.objectSize();
    }

    public S3FileMetaData getFileMetadata(String bucketName, String objectKey) {
        log.info("getFileSize/complete/{}/{}", bucketName, objectKey);
        Instant lastModified = s3Client.headObject(HeadObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build())
                .lastModified();

        return S3FileMetaData.builder()
                .objectKey(objectKey)
                .format(FilenameUtils.getExtension(objectKey))
                .size(getFileSize(bucketName, objectKey))
                .lastModified(lastModified)
                .build();
    }


    public URL getUrl(String bucketName, String objectKey) throws MalformedURLException {
        log.info("S3getUrl/{}/{}", bucketName, objectKey);
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build());
    }

    public URL getPresignedUrl(String bucketName, String objectKey, Long expiresIn) {
        log.info("getPresignedUrl/{}/{}/{}", bucketName, objectKey, expiresIn);

        try (S3Presigner presigner = S3Presigner.builder()
                .region(s3Client.serviceClientConfiguration().region())
                .endpointOverride(URI.create(serviceEndpoint))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(forcePathStyle) // Required for MinIO
                        .build())
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build()) {

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(expiresIn))
                    .getObjectRequest(GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .build())
                    .build();

            return presigner.presignGetObject(presignRequest).url();
        }
    }
}
