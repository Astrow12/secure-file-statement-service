package com.test.statementservice.s3integration.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(value = "statement.s3-integration")
public class S3IntegrationProperties {

    private SupaBaseS3Config supaBaseS3Config;


    @Data
    public static class SupaBaseS3Config {
        private String endpoint;
        private String region;
        private String accessKey;
        private String secretKey;
    }
}
