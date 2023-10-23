package com.picpaysimplificado.picpaysimplificado.dtos;

import com.picpaysimplificado.picpaysimplificado.domain.user.User;

import java.math.BigDecimal;

public record TransactionDto(BigDecimal amount, Long senderId, Long receiverId) {
}
