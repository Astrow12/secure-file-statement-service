package com.test.statementservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UploadStatusEnum {

    PENDING("Pending not uploaded to s3 bucket"),
    UPLOADED("Uploaded successfully to s3 bucket");

    private final String uploadStatusDescription;
}
