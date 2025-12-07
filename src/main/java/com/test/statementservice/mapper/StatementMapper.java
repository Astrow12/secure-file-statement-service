package com.test.statementservice.mapper;


import com.test.statementservice.model.dto.AccountStatementDto;
import com.test.statementservice.persistance.entity.AccountStatementEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatementMapper {


    AccountStatementEntity convertToStatementEntity(AccountStatementDto accountStatementDto);

    default AccountStatementDto createAccountStatementDto(String status, String fileName, String statementUrl, byte[] statementCheckSum, String userId) {

        return AccountStatementDto.builder()
                .userId(userId)
                .statementFileName(fileName)
                .fileUploadStatus(status)
                .statementChecksum(statementCheckSum)
                .isDeleted(false)
                .statementUrl(statementUrl)
                .build();

    }
}
