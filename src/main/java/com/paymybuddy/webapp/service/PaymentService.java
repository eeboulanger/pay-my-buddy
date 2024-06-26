package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.*;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.security.IAuthenticationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    private IAuthenticationService authenticationService;
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
    public void transferMoney(MoneyTransferDTO moneyTransferDTO) throws PaymentException {
        //Get authenticated user id
        User user = authenticationService.getCurrentUser();

        User receiver = userService.getUserById(moneyTransferDTO.getReceiverId()).orElseThrow(() -> {
            logger.error("Failed to find receiver: No user with id " + moneyTransferDTO.getReceiverId() + " was found.");
            return new PaymentException("Le bénéficiaire n'a pas été trouvé");
        });

        //Throws exception
        validateTransferDetails(user, receiver, moneyTransferDTO.getAmount());

        //Create transaction
        double amount = moneyTransferDTO.getAmount();
        Timestamp date = Timestamp.from(Instant.now());
        Transaction transaction = new Transaction(amount, moneyTransferDTO.getDescription(),
                date, user, receiver);

        userService.saveUser(user);
        receiver.getAccount().credit(amount);
        userService.saveUser(receiver);
        transactionService.saveTransaction(transaction);
    }

    /**
     * Validates the amount and that the receiver is part of the users connections
     */
    private void validateTransferDetails(User user, User receiver, double amount) throws PaymentException {
        logger.debug("Validate transfer details; sender: " + user + " receiver: " + receiver
                + " amount: " + amount);

        //Check if funds are sufficient
        try {
            user.getAccount().debit(amount);
        } catch (PaymentException exception) {
            logger.error("Failed to save transaction from user {} to receiver {} with the amount {} : {}",
                    user.getId(), receiver.getId(), amount, exception.getMessage(), exception);
            throw exception;
        }
        //Check that receiver is one of the users connections
        if (!user.getConnections().contains(receiver)) {
            logger.error("Failed to transfer money. The receiver must be added as a user connection first.");
            throw new PaymentException("La relation doit être ajoutée d'abord");
        }

        //assert email is different the sender's email
        if (user.getEmail().equals(receiver.getEmail())) {
            logger.error("Failed to transfer money. The receiver can't be the same as the sender");
            throw new PaymentException("Vous ne pouvez pas transférer de l'argent à vous même");
        }
    }

    /**
     * Get users previous transactions and identify those with user as receiver
     *
     * @return a list of transactions from and to the user order by date. The list can be empty
     */
    @Override
    public Optional<List<Transaction>> getCurrentUserTransactions() {
        User user = authenticationService.getCurrentUser();
        List<Transaction> transactions = transactionService.getUserTransactions(user.getId());
        if (transactions != null) {
            transactions.stream().filter(transaction ->
                            transaction.getReceiver().getEmail().equals(user.getEmail()))
                    .forEach(transaction -> transaction.getReceiver().setUsername("Me"));
        }
        return Optional.ofNullable(transactions);
    }

    /**
     * Gets the list of user connections to whom the user can transfer money. The list can be empty
     */
    @Override
    public Optional<List<User>> getUserConnections() {
        return Optional.ofNullable(authenticationService.getCurrentUser().getConnections());
    }
}
