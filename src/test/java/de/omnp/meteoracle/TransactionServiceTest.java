package de.omnp.meteoracle;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.omnp.meteoracle.application.port.out.ScanReflection;
import de.omnp.meteoracle.application.port.out.ScanSender;
import de.omnp.meteoracle.application.service.TransactionService;
import de.omnp.meteoracle.domain.vda4994.Scan;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ScanSender sender;

    @Mock
    private ScanReflection reflection;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        // We test the REAL service with MOCKED ports
        transactionService = new TransactionService(sender, reflection);
    }

    @Test
    void shouldTriggerUpdateWhenObjectIsFound() {
        // 1. Arrange
        Scan scan = new Scan();
        scan.setPackageId("EXISTING_ID");
        
        // Stubbing: Reflection finds an existing object address
        when(reflection.reflectTransactions("EXISTING_ID")).thenReturn("iota_address_123");
        // Stubbing: Update succeeds
        when(sender.updateTransaction(eq(scan), eq("iota_address_123"))).thenReturn(true);

        // 2. Act
        boolean result = transactionService.checkIn(scan);

        // 3. Assert
        assertTrue(result);
        // Verify specifically that update was called, NOT send
        verify(sender, times(1)).updateTransaction(any(), anyString());
        verify(sender, never()).sendTransaction(any());
    }

    @Test
    void shouldTriggerNewTransactionWhenNoObjectIsFound() {
        // 1. Arrange
        Scan scan = new Scan();
        scan.setPackageId("NEW_ID");

        // Stubbing: Reflection returns null (not found)
        when(reflection.reflectTransactions("NEW_ID")).thenReturn(null);
        // Stubbing: New transaction succeeds
        when(sender.sendTransaction(scan)).thenReturn(true);

        // 2. Act
        boolean result = transactionService.checkIn(scan);

        // 3. Assert
        assertTrue(result);
        // Verify specifically that send was called, NOT update
        verify(sender, times(1)).sendTransaction(any());
        verify(sender, never()).updateTransaction(any(), anyString());
    }

    @Test
    void shouldReturnFalseWhenSenderFails() {
        // 1. Arrange
        Scan scan = new Scan();
        when(reflection.reflectTransactions(any())).thenReturn(null);
        when(sender.sendTransaction(any())).thenReturn(false);

        // 2. Act
        boolean result = transactionService.checkIn(scan);

        // 3. Assert
        assertFalse(result);
    }
}
