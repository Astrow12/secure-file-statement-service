package com.test.statementservice.s3integration.service;

import java.time.Duration;

public interface S3IntegrationService {

    void uploadPdfToS3(String key, byte[] bytes);

    String generateS3SignedUrl(String key, Duration expiry);
}
