package com.test.statementservice.mapper;


import com.test.statementservice.enums.UploadStatusEnum;
import com.test.statementservice.model.dto.AccountStatementDto;
import com.test.statementservice.model.dto.DocumentDto;
import com.test.statementservice.persistance.entity.AccountStatementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatementMapper {


    AccountStatementEntity convertToStatementEntity(AccountStatementDto accountStatementDto);


    @Mapping(source = "statementFileName", target = "fileName")
    @Mapping(source = "documentId", target = "documentId")
    DocumentDto convertToDocumentDto(AccountStatementEntity accountStatementEntity);
    List<DocumentDto> convertToListOfDocumentDto(List<AccountStatementEntity> accountStatementEntities);

    default AccountStatementDto createAccountStatementDto(String key, UploadStatusEnum status, String fileName, byte[] statementCheckSum, String userId) {

        return AccountStatementDto.builder()
                .userId(userId)
                .s3StatementKey(key)
                .statementFileName(fileName)
                .fileUploadStatus(status)
                .statementChecksum(statementCheckSum)
                .isDeleted(false)
                .build();

    }
}
