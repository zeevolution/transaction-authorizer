package transaction.authorizer.consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

@ExtendWith(MockitoExtension.class)
public class AuthorizerConsumerTest {

    @InjectMocks
    private AuthorizerConsumer authorizerConsumer;

    @Test
    public void processAccountTest() {
        // Arrange
        final boolean activeCard = true;
        final int availableLimit = 100;
        final String expectedOutput = "{\"account\": {\"active-card\": "+ activeCard +
                ", \"available-limit\": " + availableLimit + "}, \"violations\": []}\n";

        // Act
        final String actualOutput = authorizerConsumer.processAccount(activeCard, availableLimit);

        // Assert
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void processTransactionWithAccountTest() {
        // Arrange
        final String merchant = "merchant";
        final int amount = 100;
        final ZonedDateTime time = ZonedDateTime.parse("2019-02-13T11:00:00.000Z");
        final String expectedOutput = "account-not-initialized";

        // Act
        final String actualOutput = authorizerConsumer.processTransaction(merchant, amount, time);

        // Assert
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void processTransactionWithNoAccountTest() {
        // Arrange
        final boolean activeCard = true;
        final int availableLimit = 100;
        final int amount = 50;
        final ZonedDateTime time = ZonedDateTime.parse("2019-02-13T11:00:00.000Z");
        final String merchant = "merchant";
        final String expectedOutput =
                "{\"account\": {\"active-card\": "+ activeCard +", \"available-limit\": "
                        + (availableLimit - amount) + "}, \"violations\": []}\n";

        authorizerConsumer.processAccount(activeCard, availableLimit);

        // Act
        final String actualOutput = authorizerConsumer.processTransaction(merchant, amount, time);

        // Assert
        Assertions.assertEquals(expectedOutput, actualOutput);
    }
}
