package transaction.authorizer.service.transaction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import transaction.authorizer.model.account.Account;

import java.time.ZonedDateTime;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void processTransactionWithNullAccountTest() {
        // Arrange
        final String merchant = "merchant";
        final int amount = 100;
        final ZonedDateTime time = ZonedDateTime.now();
        final String expectedOutput = "account-not-initialized";

        // Act
        final String actualOutput = transactionService.processTransaction(null, merchant, amount, time);

        // Assert
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void processTransactionWithActiveAccountTest() {
        // Arrange
        final int amount = 100;
        final String merchant = "merchant";
        final ZonedDateTime time = ZonedDateTime.now();
        final Account account = buildActiveAccount(100);
        final String expectedOutput = "{\"account\": {\"active-card\": true," +
                " \"available-limit\": " + (account.getAvailableLimit() - amount) +
                "}, \"violations\": []}\n";

        // Act
        final String actualOutput = transactionService.processTransaction(account, merchant, amount, time);

        // Assert
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void processTransactionWithInsufficientViolationTest() {
        // Arrange
        final int amount = 100;
        final String merchant = "merchant";
        final ZonedDateTime time = ZonedDateTime.now();
        final Account account = buildActiveAccount(50);
        final String expectedOutput = "{\"account\": {\"active-card\": true, \"available-limit\": " +
                (account.getAvailableLimit()) +"}, \"violations\": [insufficient-limit]}\n";

        // Act
        final String actualOutput = transactionService.processTransaction(account, merchant, amount, time);

        // Assert
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void processTransactionMultipleTransactionsTest() {
        // Arrange
        final int amount = 50;
        final String merchant = "merchant";
        final ZonedDateTime time = ZonedDateTime.now();
        final Account account = buildActiveAccount(150);
        final String firstExpectedOutput = "{\"account\": {\"active-card\": true, \"available-limit\": 100},"
                + " \"violations\": []}\n";
        final String secondExpectedOutput = "{\"account\": {\"active-card\": true, \"available-limit\": 50},"
                + " \"violations\": []}\n";
        final String thirdExpectedOutput = "{\"account\": {\"active-card\": true, \"available-limit\": 0},"
                + " \"violations\": []}\n";

        // Act
        final String actualFirstOutput = transactionService.processTransaction(account, "amazon", amount, time);
        final String actualSecondOutput = transactionService.processTransaction(account, "ebay", amount, time);
        final String actualThirdOutput = transactionService.processTransaction(account, "apple", amount, time);

        // Assert
        Assertions.assertEquals(firstExpectedOutput, actualFirstOutput);
        Assertions.assertEquals(secondExpectedOutput, actualSecondOutput);
        Assertions.assertEquals(thirdExpectedOutput, actualThirdOutput);
    }

    private Account buildActiveAccount(final int availableLimit) {
        return new Account(true, availableLimit);
    }
}
