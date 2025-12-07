package com.test.statementservice.service;

import com.test.statementservice.model.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface StatementService {

    MessageResponse uploadAccountStatement(String fileOwner, MultipartFile statementFile);
}
