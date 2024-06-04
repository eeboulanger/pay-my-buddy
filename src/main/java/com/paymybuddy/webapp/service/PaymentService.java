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
        Authentication authUser = authenticationFacade.getAuthentication();
        User user = userService.getUserByEmail(authUser.getName()).orElseThrow(() -> {
            logger.error("Failed to find authenticated user: No user with email " + authUser.getName() + " was found.");
            return new UsernameNotFoundException("Authenticated user not found.");
        });

        User receiver = userService.getUserById(moneyTransferDTO.getReceiverId()).orElseThrow(() -> {
            logger.error("Failed to find receiver: No user with id " + moneyTransferDTO.getReceiverId() + " was found.");
            return new PaymentException("Receiver not found");
        });

        validateTransferDetails(user, receiver, moneyTransferDTO.getAmount());

        executeTransfer(moneyTransferDTO, user, receiver);
    }

    /**
     * Validates the amount, the authenticated user, that the funds are sufficient and that the receiver is part of the users connections
     * @throws PaymentException
     */
    private void validateTransferDetails(User user, User receiver, double amount) throws PaymentException {
        if (amount <= 0) {
            logger.error("The amount must be greater than 0.");
            throw new PaymentException("Amount not valid");
        }
        //Check that amount isn't greater than current balance
        if (user.getAccount().getBalance() < amount) {
            logger.error("Failed to transfer money. The amount is higher than the current available balance.");
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
     * @param moneyTransferDTO
     * @throws PaymentException
     */
    private void executeTransfer(MoneyTransferDTO moneyTransferDTO, User user, User receiver) throws PaymentException {
        double amount = moneyTransferDTO.getAmount();


        Timestamp date = Timestamp.from(Instant.now());
        Transaction transaction = new Transaction(amount, moneyTransferDTO.getDescription(),
                date, user, receiver);
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
}
