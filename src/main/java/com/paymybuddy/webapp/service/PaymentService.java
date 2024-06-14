package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.*;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PaymentService implements IPaymentService {
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private IUserService userService;
    @Autowired
    private ITransactionService transactionService;

    /**
     * Transfers money from the authenticated users account to one of the user connections, if the amount doesn't exceed the user's current balance
     *
     * @param moneyTransferDTO contains the user to whom the money is transferred, the amount to be transferred
     *                         and the user's description of the transfer
     */
    @Override
    @Transactional
    public void transferMoney(MoneyTransferDTO moneyTransferDTO) throws UsernameNotFoundException, PaymentException {
        //Get authenticated user id
        User user = getCurrentUser();

        User receiver = userService.getUserById(moneyTransferDTO.getReceiverId()).orElseThrow(() -> {
            logger.error("Failed to find receiver: No user with id " + moneyTransferDTO.getReceiverId() + " was found.");
            return new PaymentException("Receiver not found");
        });

        validateTransferDetails(user, receiver, moneyTransferDTO.getAmount());

        executeTransfer(moneyTransferDTO, user, receiver);
    }

    private User getCurrentUser() {
        Authentication authUser = authenticationFacade.getAuthentication();
        return userService.getUserByEmail(authUser.getName()).orElseThrow(() -> {
            logger.error("Failed to find authenticated user: No user with email " + authUser.getName() + " was found.");
            return new UsernameNotFoundException("Authenticated user not found."); //Force login if not found
        });
    }

    /**
     * Validates that the funds are sufficient and that the receiver is part of the users connections
     */
    private void validateTransferDetails(User user, User receiver, double amount) throws PaymentException {
        logger.debug("Validate transfer details; sender: " + user + " receiver: " + receiver
                + " amount: " + amount);

        //Check that amount isn't greater than current balance
        if (user.getAccount().getBalance() < amount) {
            logger.error("Failed to transfer money. The amount is higher than the current available balance." +
                    "Amount: " + amount + " balance: " + user.getAccount().getBalance());
            throw new PaymentException("Funds are insufficient");
        }
        //Check that receiver is one of the users connections
        if (!user.getConnections().contains(receiver)) {
            logger.error("Failed to transfer money. The receiver must be added as a user connection first.");
            throw new PaymentException("User has to be added as connection.");
        }
    }

    /**
     * saves the transaction with a timestamp, debits the authenticated users account and credits the receivers account
     */
    private void executeTransfer(MoneyTransferDTO moneyTransferDTO, User user, User receiver) throws PaymentException {
        //Create transaction
        double amount = moneyTransferDTO.getAmount();
        Timestamp date = Timestamp.from(Instant.now());
        Transaction transaction = new Transaction(amount, moneyTransferDTO.getDescription(),
                date, user, receiver);

        logger.debug("Attempt to execute transfer. Sender: " + user + " receiver: " + receiver
                + " amount: " + amount + " description: " + moneyTransferDTO.getDescription() + " date: " + date);
        try {
            transactionService.saveTransaction(transaction);
            user.getAccount().debit(amount);
            userService.saveUser(user);
            receiver.getAccount().credit(amount);
            userService.saveUser(receiver);
        } catch (DataAccessException e) {
            logger.error("Failed to save transaction from user {} to receiver {} with the amount {} : {}",
                    user.getId(), receiver.getId(), amount, e.getMessage(), e);
            throw new PaymentException("Failed to transfer money: " + e.getMessage());
        }
    }

    /**
     * Get users previous transactions and identify those with user as receiver
     *
     * @return a list of transactions from and to the user order by date
     */
    public Optional<List<Transaction>> getUserTransactions() {
        User user = getCurrentUser();
        List<Transaction> transactions = transactionService.getUserTransactions(user.getId());
        if (transactions != null) {
            transactions.stream().filter(transaction ->
                            transaction.getReceiver().getEmail().equals(user.getEmail()))
                    .forEach(transaction -> transaction.getReceiver().setUsername("Me"));
        }
        return Optional.ofNullable(transactions);
    }

    public Optional<Set<User>> getUserConnections() {
        User user = getCurrentUser();
        return Optional.ofNullable(user.getConnections());
    }
}
