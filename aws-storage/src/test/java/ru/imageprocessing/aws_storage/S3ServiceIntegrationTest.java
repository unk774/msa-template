package ru.imageprocessing.aws_storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestConstructor;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.imageprocessing.aws_storage.service.s3.S3Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
class S3ServiceIntegrationTest {

    @Container
    private static final LocalStackContainer localStack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:latest"))
            .withServices(S3);

    private static final String STORE_BUCKET = "store";
    private static final String OBJECT_KEY = "testfile.txt";

    private final S3Service s3Service;
    private static S3Client s3Client;

    public record TestFile(byte[] content, long size) {
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public S3Client s3Mock() {
            return s3Client;
        }
    }

    @BeforeAll
    static void setUp() {
        s3Client = S3Client.builder()
                .endpointOverride(localStack.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(localStack.getAccessKey(), localStack.getSecretKey())))
                .region(Region.of(localStack.getRegion()))
                .build();

        //create bucket
        s3Client.createBucket(CreateBucketRequest.builder().bucket(STORE_BUCKET).build());
    }

    @AfterAll
    static void tearDown() {
        s3Client.close();
    }

    @AfterEach
    public void cleanUp() {
        if (s3Service.objectKeyExists(OBJECT_KEY)) {
            s3Service.deleteFile(OBJECT_KEY);
        }
    }

    @Test
    void testUploadXDownload() throws IOException {
        var file = getTestFile();
        s3Service.uploadStream(OBJECT_KEY, new ByteArrayInputStream(file.content), file.size);
        assertTrue(s3Service.objectKeyExists(OBJECT_KEY));
        try (InputStream inputStream = s3Service.downloadRange(OBJECT_KEY, 0, file.size)) {
            assertTrue(Arrays.equals(file.content, inputStream.readAllBytes()));
        }
    }

    @Test
    void testDeleteFile() {
        var file = getTestFile();
        s3Service.uploadStream(OBJECT_KEY, new ByteArrayInputStream(file.content), file.size);
        assertTrue(s3Service.objectKeyExists(OBJECT_KEY));
        s3Service.deleteFile(OBJECT_KEY);
        assertFalse(s3Service.objectKeyExists(OBJECT_KEY));
    }

    @Test
    void testFileSize() {
        var file = getTestFile();
        s3Service.uploadStream(OBJECT_KEY, new ByteArrayInputStream(file.content), file.size);
        var size = s3Service.getFileSize(OBJECT_KEY);
        assertEquals(file.size, size);
    }

    @Test
    void testGetFileMetadata() {
        var file = getTestFile();
        s3Service.uploadStream(OBJECT_KEY, new ByteArrayInputStream(file.content), file.size);
        var metadata = s3Service.getFileMetadata(OBJECT_KEY);
        assertNotNull(metadata);
        assertEquals(file.size, metadata.getSize());
        assertEquals(OBJECT_KEY, metadata.getObjectKey());
        assertEquals("txt", metadata.getFormat());
        assertNotNull(metadata.getLastModified());
    }

    @Test
    void testGetUrl() throws MalformedURLException {
        var file = getTestFile();
        s3Service.uploadStream(OBJECT_KEY, new ByteArrayInputStream(file.content), file.size);
        var url = s3Service.getUrl(OBJECT_KEY).toExternalForm();
        assertEquals(localStack.getEndpointOverride(S3) + "/" + STORE_BUCKET + "/" + OBJECT_KEY, url);
    }

    @Test
    void getPresignedUrlSmoke() {
        var file = getTestFile();
        s3Service.uploadStream(OBJECT_KEY, new ByteArrayInputStream(file.content), file.size);
        var url = s3Service.getPresignedUrl(OBJECT_KEY, 120L).toExternalForm();
        assertNotNull(url);
    }

    private TestFile getTestFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(OBJECT_KEY)) {
            byte[] content = inputStream.readAllBytes();
            return new TestFile(content, content.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}