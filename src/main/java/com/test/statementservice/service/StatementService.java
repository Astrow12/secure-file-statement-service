package com.test.statementservice.service;

import com.test.statementservice.model.response.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StatementService {

    DocumentResponse uploadAccountStatement(String fileOwner, MultipartFile statementFile);

}
