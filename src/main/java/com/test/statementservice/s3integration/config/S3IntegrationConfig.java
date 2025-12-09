package com.test.statementservice.s3integration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(value = S3IntegrationProperties.class)
@RequiredArgsConstructor
public class S3IntegrationConfig {


    private final S3IntegrationProperties supaBaseS3Properties;

    @Bean
    public S3Client supabaseS3Client() {
        return S3Client.builder()
                .region(Region.of(supaBaseS3Properties.getSupabaseS3Config().getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(supaBaseS3Properties.getSupabaseS3Config().getAccessKey(), supaBaseS3Properties.getSupabaseS3Config().getSecretKey())
                        )
                )
                .endpointOverride(URI.create(supaBaseS3Properties.getSupabaseS3Config().getEndpoint()))
                .forcePathStyle(true)
                .build();
    }

    @Bean
    public S3Presigner supabaseS3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(supaBaseS3Properties.getSupabaseS3Config().getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        supaBaseS3Properties.getSupabaseS3Config().getAccessKey(),
                                        supaBaseS3Properties.getSupabaseS3Config().getSecretKey()
                                )
                        )
                )
                .endpointOverride(URI.create(supaBaseS3Properties.getSupabaseS3Config().getEndpoint()))
                //Used for supabase as it does not use virtual hosted style like aws so causes SSL issues from cloudflare
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true)
                        .build())
                .build();
    }
}

