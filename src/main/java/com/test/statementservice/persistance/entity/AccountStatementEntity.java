package com.test.statementservice.persistance.entity;


import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.persistance.listener.AccountStatementListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "account_statement")
@Entity
@EntityListeners(AccountStatementListener.class)
public class AccountStatementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long documentId;


    @Column(name = "s3_path_key")
    private String s3StatementKey;


    @Column(name = "upload_status")
    @Enumerated(EnumType.STRING)
    private UploadStatusEnum fileUploadStatus;


    @Column(name = "file_name")
    private String statementFileName;

    @Column(name = "deleted")
    private boolean isDeleted;

    @Column(name = "checksum")
    private byte[] statementChecksum;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
