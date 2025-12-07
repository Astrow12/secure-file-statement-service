package com.test.statementservice.s3integration.service;

import com.test.statementservice.s3integration.exception.S3IntegrationException;

import java.time.Duration;

public interface S3IntegrationService {

    void uploadPdfToS3(String key, byte[] bytes) throws S3IntegrationException;

    String generateS3SignedUrl(String key, Duration expiry) throws  S3IntegrationException;
}
