package com.test.statementservice.persistance.entity;


import com.test.statementservice.persistance.listener.AccountStatementListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(schema = "statementsdb", name = "account_statement")
@Entity
@EntityListeners(AccountStatementListener.class)
public class AccountStatementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long documentId;


    @Column(name = "signed_statement_url")
    private String statementUrl;


    @Column(name = "file_name")
    private String statementFileName;

    @Column(name = "deleted")
    private boolean isDeleted;

    @Lob //For mapping as binary obj -> bytea
    @Column(name = "checksum", unique = true)
    private byte[] statementChecksum;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;
}
