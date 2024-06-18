package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
import com.paymybuddy.webapp.security.IAuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private IAuthenticationService authenticationService;
    @Mock
    private Authentication authentication;
    @Mock
    private ITransactionService transactionService;
    @Mock
    private IUserService userService;
    @InjectMocks
    private PaymentService paymentService;

    private final User receiver = new User();
    private final Account account = new Account();
    private final User user = new User();
    private final MoneyTransferDTO dto = new MoneyTransferDTO();

    @BeforeEach
    public void setUp() {
        receiver.setAccount(new Account());
        receiver.setId(2);
        account.setBalance(15.00);
        user.setAccount(account);
        user.setEmail("john@mail.com");
        user.setId(1);
        user.getConnections().add(receiver);


        dto.setAmount(10.00);
        dto.setDescription("Reimbursement");
        dto.setReceiverId(2);
    }

    @Test
    @DisplayName("Given the amount, user connection and description are valid then transfer money")
    public void transferMoneyTest() throws PaymentException {
        when(authenticationService.getCurrentUser()).thenReturn(user);
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));

        paymentService.transferMoney(dto);

        verify(authenticationService, times(1)).getCurrentUser();
        verify(userService, times(1)).getUserById(2);
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Authenticated user not found should throw exception")
    public void transferMoney_whenAuthenticatedUserNotFound_thenThrowException() {
        when(authenticationService.getCurrentUser()).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () ->
                paymentService.transferMoney(dto));

        verify(authenticationService, times(1)).getCurrentUser();
        verify(transactionService, never()).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Given the amount exceeds the current balance, when transfer money, then throw exception")
    public void givenFundsAreInsufficient_whenTransferMoney_thenThrowException() {
        account.setBalance(5.00);
        user.setAccount(account);

        when(authenticationService.getCurrentUser()).thenReturn(user);
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));

        assertThrows(PaymentException.class, () ->
                paymentService.transferMoney(dto));

        verify(authenticationService, times(1)).getCurrentUser();
        verify(userService, times(1)).getUserById(2);
        verify(transactionService, never()).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("When receiver is not a user connection should throw an exception")
    public void givenReceiverNotAConnection_whenTransferMoney_shouldThrowException() {
        user.getConnections().remove(receiver);

        when(authenticationService.getCurrentUser()).thenReturn(user);
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));

        assertThrows(PaymentException.class, () ->
                paymentService.transferMoney(dto));

        verify(authenticationService, times(1)).getCurrentUser();
        verify(userService, times(1)).getUserById(2);
        verify(transactionService, never()).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Saving transaction to database fails, should throw an exception")
    public void saveTransactionFails_whenTransferMoney_shouldThrowException() {
        when(authenticationService.getCurrentUser()).thenReturn(user);
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));
        doThrow(PermissionDeniedDataAccessException.class).when(transactionService).saveTransaction(any(Transaction.class));

        assertThrows(PaymentException.class, () ->
                paymentService.transferMoney(dto));

        verify(authenticationService, times(1)).getCurrentUser();
        verify(userService, times(1)).getUserById(2);
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Given the user has a list of connections then get user connections should return the list")
    public void getUserConnectionsTest() {
        when(authenticationService.getCurrentUser()).thenReturn(user);

        Set<User> result = paymentService.getUserConnections().orElse(Collections.emptySet());

        verify(authenticationService).getCurrentUser();
        assertNotNull(result);
        assertTrue(result.contains(receiver));
    }

    @Test
    @DisplayName("Given there are no transactions for the user then return empty list")
    public void getUserTransactions_whenNoTransactionsTest() {
        List<Transaction> transactions = null;

        when(authenticationService.getCurrentUser()).thenReturn(user);
        when(transactionService.getUserTransactions(user.getId())).thenReturn(transactions);

        Optional<List<Transaction>> result = paymentService.getUserTransactions();

        verify(authenticationService).getCurrentUser();
        verify(transactionService, times(1)).getUserTransactions(user.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Given there are transactions for the user then return list")
    public void getUserTransactionsTest() {
        Transaction transaction = new Transaction();
        transaction.setReceiver(user);
        transaction.setSender(user);
        List<Transaction> transactions = List.of(transaction);

        when(authenticationService.getCurrentUser()).thenReturn(user);
        when(transactionService.getUserTransactions(user.getId())).thenReturn(transactions);

        Optional<List<Transaction>> result = paymentService.getUserTransactions();

        verify(authenticationService).getCurrentUser();
        verify(transactionService, times(1)).getUserTransactions(user.getId());
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }
}
