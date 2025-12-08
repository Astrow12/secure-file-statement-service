package com.test.statementservice.model.response;

import com.test.statementservice.model.dto.DocumentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatementsResponse {

    private List<DocumentDto> documentDtoList;
}
