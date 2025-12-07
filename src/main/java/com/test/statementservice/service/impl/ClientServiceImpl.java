package com.test.statementservice.service.impl;

import com.test.statementservice.exception.ClientException;
import com.test.statementservice.model.response.ClientDataResponse;
import com.test.statementservice.persistance.entity.AccountStatementEntity;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Value("${statement.s3-integration.signed-expiration}")
    private Duration expirationDate;

    private final AccountStatementRepository accountStatementRepository;

}
