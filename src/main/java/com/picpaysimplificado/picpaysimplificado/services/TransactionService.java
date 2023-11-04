package com.picpaysimplificado.picpaysimplificado.services;

import com.picpaysimplificado.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionDto;
import com.picpaysimplificado.picpaysimplificado.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TransactionService {

//    @Autowired
//    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthorizartionService authService;


    public Transaction createTransaction(TransactionDto transactionDTO) throws Exception {
        User sender = this.userService.findById(transactionDTO.senderId());
        User receiver = this.userService.findById(transactionDTO.receiverId());

        userService.validateTransaction(sender, transactionDTO.amount());
        boolean isAuthorized = this.authService.authorizeTransaction(sender, transactionDTO.amount());
        if(!isAuthorized){
            throw new Exception("Transaction not allowed.");
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDTO.amount());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.amount()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.amount()));

        this.transactionRepository.save(transaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);

//        notificationService.sendNotification(sender, "Transaction sent!");
//        notificationService.sendNotification(receiver, "Transaction received!");

        return transaction;
    }


}
