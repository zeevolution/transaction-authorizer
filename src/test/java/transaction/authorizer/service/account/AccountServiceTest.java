package transaction.authorizer.service.account;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Test
    public void processActiveAccountTest() {
        // Arrange
        final boolean activeCard = true;
        final int availableLimit = 100;
        final String expectedOutput = "{\"account\": {\"active-card\": "+ activeCard +", \"available-limit\": " +
                + availableLimit + "}, \"violations\": []}\n";

        // Act
        final String actualOutput = accountService.processAccount(activeCard, availableLimit);

        // Assert
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void processInactiveAccountTest() {
        // Arrange
        final boolean activeCard = false;
        final int availableLimit = 100;
        final String expectedOutput = "{\"account\": {\"active-card\": "+ activeCard +", \"available-limit\": " +
                + availableLimit + "}, \"violations\": []}\n";

        // Act
        final String actualOutput = accountService.processAccount(activeCard, availableLimit);

        // Assert
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void processMultipleAccountsTest() {
        // Arrange
        final boolean activeCard = true;
        final int availableLimit = 100;
        final String expectedFirstOutput = "{\"account\": {\"active-card\": "+ activeCard +", \"available-limit\": " +
                + availableLimit + "}, \"violations\": []}\n";
        final String expectedSecondOutput = "{\"account\": {\"active-card\": "+ activeCard +", \"available-limit\": " +
                + availableLimit + "}, \"violations\": [account-already-initialized]}\n";
        final String expectedThirdOutput = "{\"account\": {\"active-card\": "+ activeCard +", \"available-limit\": " +
                + availableLimit + "}, \"violations\": [account-already-initialized]}\n";

        // Act
        final String actualFirstOutput = accountService.processAccount(activeCard, availableLimit);
        final String actualSecondOutput = accountService.processAccount(activeCard, availableLimit);
        final String actualThirdOutput = accountService.processAccount(activeCard, availableLimit);

        // Assert
        Assertions.assertEquals(expectedFirstOutput, actualFirstOutput);
        Assertions.assertEquals(expectedSecondOutput, actualSecondOutput);
        Assertions.assertEquals(expectedThirdOutput, actualThirdOutput);
    }
}
