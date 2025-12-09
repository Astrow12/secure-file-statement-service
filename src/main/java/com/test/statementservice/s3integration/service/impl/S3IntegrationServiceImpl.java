package com.test.statementservice.s3integration.service.impl;

import com.test.statementservice.s3integration.config.S3IntegrationProperties;
import com.test.statementservice.s3integration.exception.S3IntegrationException;
import com.test.statementservice.s3integration.service.S3IntegrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3IntegrationServiceImpl implements S3IntegrationService {

    private final S3Client supabaseS3Client;
    private final S3Presigner supabaseS3Presigner;
    @Value("${statement.s3-integration.bucket}")
    private String bucketName;

    public void uploadPdfToS3(String key, byte[] bytes) {
        log.info("Uploading pdf with key: {}", key);
        try {
            supabaseS3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(MediaType.APPLICATION_PDF_VALUE)
                            .build(),
                    RequestBody.fromBytes(bytes));
        } catch (S3Exception ex) {
            log.error("Exception occurred while uploading pdf", ex);
            throw new S3IntegrationException(HttpStatus.resolve(ex.statusCode()), ex.getMessage());
        } catch (Exception ex) {
            log.error("Internal exception occurred while uploading pdf", ex);
            throw new S3IntegrationException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    public boolean deletePdfOnS3(String key) {
        log.info("Deleting pdf with key: {}", key);
        boolean deleted = false;
        try {
            supabaseS3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build());
            deleted = true;
        } catch (S3Exception ex) {
            log.error("Exception occurred while deleting pdf", ex);
            throw new S3IntegrationException(HttpStatus.resolve(ex.statusCode()), ex.getMessage());
        } catch (Exception ex) {
            log.error("Internal exception occurred while deleting pdf", ex);
            throw new S3IntegrationException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return deleted;
    }

    public String generateS3SignedUrl(String key, Duration expiry) {
        log.info("Generating signed url with key: {}", key);
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            PresignedGetObjectRequest presigned = supabaseS3Presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .signatureDuration(expiry)
                            .getObjectRequest(getObjectRequest)
                            .build()
            );
            return presigned.url().toString();
        } catch (S3Exception ex) {
            log.error("Exception occurred while generating signed url", ex);
            throw new S3IntegrationException(HttpStatus.resolve(ex.statusCode()), ex.getMessage());
        } catch (Exception ex) {
            log.error("Internal exception occurred while generating signed url", ex);
            throw new S3IntegrationException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
