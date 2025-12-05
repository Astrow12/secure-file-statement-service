package com.test.statementservice.service.impl;

import com.test.statementservice.exception.ClientException;
import com.test.statementservice.model.request.StatementRequest;
import com.test.statementservice.model.response.MessageResponse;
import com.test.statementservice.model.response.DocumentResponse;
import com.test.statementservice.persistance.entity.AccountStatementEntity;
import com.test.statementservice.persistance.repository.AccountStatementRepository;
import com.test.statementservice.service.StatementService;
import com.test.statementservice.web.UserStore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private final AccountStatementRepository accountStatementRepository;

    private final UserStore userStore;

    @PersistenceContext
    private EntityManager entityManager;
}
