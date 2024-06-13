package com.paymybuddy.webapp.service;

import com.paymybuddy.webapp.dto.MoneyTransferDTO;
import com.paymybuddy.webapp.exception.PaymentException;
import com.paymybuddy.webapp.exception.UserNotFoundException;
import com.paymybuddy.webapp.model.Account;
import com.paymybuddy.webapp.model.Transaction;
import com.paymybuddy.webapp.model.User;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock
    private IAuthenticationFacade authenticationFacade;
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
    public void transferMoneyTest() throws PaymentException, UserNotFoundException {
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(authentication.getName())).thenReturn(Optional.of(user));
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));

        paymentService.transferMoney(dto);

        verify(authenticationFacade, times(1)).getAuthentication();
        verify(userService, times(1)).getUserByEmail(authentication.getName());
        verify(userService, times(1)).getUserById(2);
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Authenticated user not found should throw exception")
    public void transferMoney_whenAuthenticatedUserNotFound_thenThrowException() {
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(authentication.getName())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                paymentService.transferMoney(dto));

        verify(authenticationFacade, times(1)).getAuthentication();
        verify(userService, times(1)).getUserByEmail(authentication.getName());
        verify(transactionService, never()).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Given the amount exceeds the current balance, when transfer money, then throw exception")
    public void givenFundsAreInsufficient_whenTransferMoney_thenThrowException() {
        account.setBalance(5.00);
        user.setAccount(account);

        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(authentication.getName())).thenReturn(Optional.of(user));
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));

        assertThrows(PaymentException.class, () ->
                paymentService.transferMoney(dto));

        verify(authenticationFacade, times(1)).getAuthentication();
        verify(userService, times(1)).getUserByEmail(authentication.getName());
        verify(userService, times(1)).getUserById(2);
        verify(transactionService, never()).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("When receiver is not a user connection should throw an exception")
    public void givenReceiverNotAConnection_whenTransferMoney_shouldThrowException() {
        user.getConnections().remove(receiver);

        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(authentication.getName())).thenReturn(Optional.of(user));
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));

        assertThrows(PaymentException.class, () ->
                paymentService.transferMoney(dto));

        verify(authenticationFacade, times(1)).getAuthentication();
        verify(userService, times(1)).getUserByEmail(authentication.getName());
        verify(userService, times(1)).getUserById(2);
        verify(transactionService, never()).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Saving transaction to database fails, should throw an exception")
    public void saveTransactionFails_whenTransferMoney_shouldThrowException() {
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(authentication.getName())).thenReturn(Optional.of(user));
        when(userService.getUserById(2)).thenReturn(Optional.of(receiver));
        doThrow(PermissionDeniedDataAccessException.class).when(transactionService).saveTransaction(any(Transaction.class));

        assertThrows(PaymentException.class, () ->
                paymentService.transferMoney(dto));

        verify(authenticationFacade, times(1)).getAuthentication();
        verify(userService, times(1)).getUserByEmail(authentication.getName());
        verify(userService, times(1)).getUserById(2);
        verify(transactionService, times(1)).saveTransaction(any(Transaction.class));
    }

    @Test
    @DisplayName("Given the user has a list of connections then get user connections should return the list")
    public void getUserConnectionsTest() {
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userService.getUserByEmail(authentication.getName())).thenReturn(Optional.of(user));

        Set<User> result = paymentService.getUserConnections();

        verify(authenticationFacade).getAuthentication();
        verify(userService).getUserByEmail(authentication.getName());
        assertNotNull(result);
        assertTrue(result.contains(receiver));
    }
}
