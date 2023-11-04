package com.picpaysimplificado.picpaysimplificado.services;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionDto;
import com.picpaysimplificado.picpaysimplificado.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private AuthorizartionService authService;

    @Autowired
    private TransactionService transactionService;
    @Test
    @DisplayName("Should create Transaction successfully when everything is ok")
    void createTransactionSuccess() throws Exception {
        User sender = new User(1L, "maria", "guerra", "99999999901", "maria@gmail.com", "123456", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "joao", "guerra", "99999999802", "joao@gmail.com", "123456", new BigDecimal(10), UserType.COMMON);

        when(userService.findById(1L)).thenReturn(sender);
        when(userService.findById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDto request = new TransactionDto(new BigDecimal(10), 1L, 2L);
        transactionService.createTransaction(request);

        verify(transactionRepository, times(1)).save(any());

        verify(userService, times(1)).saveUser(sender);

        verify(userService, times(1)).saveUser(receiver);
    }

    @Test
    @DisplayName("Should throw Exception when Transaction is not allowed")
    void createTransactionCaseFail() throws Exception {
        User sender = new User(1L, "maria", "guerra", "99999999901", "maria@gmail.com", "123456", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "joao", "guerra", "99999999802", "joao@gmail.com", "123456", new BigDecimal(10), UserType.COMMON);

        when(userService.findById(1L)).thenReturn(sender);
        when(userService.findById(2L)).thenReturn(receiver);

        when(authService.authorizeTransaction(any(), any())).thenReturn(false);

        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDto request = new TransactionDto(new BigDecimal(10), 1L, 2L);
            transactionService.createTransaction(request);
        });

        Assertions.assertEquals("Transaction not allowed.", thrown.getMessage());
    }
}