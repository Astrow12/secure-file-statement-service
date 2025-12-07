package com.test.statementservice.model.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import java.util.UUID;

@Data
@Builder
public class AccountStatementDto {
    private String statementUrl;
    private String statementFileName;
    private boolean isDeleted;
    private String userId;
    private byte[] statementChecksum;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccountStatementDto that = (AccountStatementDto) o;
        return Objects.equals(statementChecksum, that.statementChecksum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statementChecksum);
    }

}
