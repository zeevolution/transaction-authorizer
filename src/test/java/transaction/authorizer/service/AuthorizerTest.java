package transaction.authorizer.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import transaction.authorizer.model.account.Account;
import transaction.authorizer.model.transaction.Transaction;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class AuthorizerTest {

    @Test
    public void applyAccountAlreadyInitializedRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final Account activeAccount = buildActiveAccount();
        final boolean expectedResult = true;

        // Act
        final boolean actualResult = authorizer.applyAccountAlreadyInitializedRule(Collections
                .singletonList(activeAccount));

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void notApplyAccountAlreadyInitializedRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final boolean expectedResult = false;

        // Act
        final boolean actualResult = authorizer.applyAccountAlreadyInitializedRule(Collections.emptyList());

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void applyCardNotActiveRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final Account inactiveAccount = buildInactiveAccount();
        final boolean expectedResult = true;

        // Act
        final boolean actualResult = authorizer.applyCardNotActiveRule(inactiveAccount);

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void notApplyCardNotActiveRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final Account activeAccount = buildActiveAccount();
        final boolean expectedResult = false;

        // Act
        final boolean actualResult = authorizer.applyCardNotActiveRule(activeAccount);

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void applyInsufficientLimitRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final Transaction transaction = buildTransaction(150);
        final int availableLimit = 100;
        final boolean expectedResult = true;

        // Act
        final boolean actualResult = authorizer.applyInsufficientLimitRule(availableLimit, transaction);

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void notApplyInsufficientLimitRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final Transaction transaction = buildTransaction(100);
        final int availableLimit = 100;
        final boolean expectedResult = false;

        // Act
        final boolean actualResult = authorizer.applyInsufficientLimitRule(availableLimit, transaction);

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void applyHighFrequencySmallIntervalRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final boolean expectedResult = true;

        // Act
        final boolean actualResult = authorizer.applyHighFrequencySmallIntervalRule(new LinkedList<>(Arrays
                .asList(buildTransaction(100), buildTransaction(100), buildTransaction(100))));

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void notApplyHighFrequencySmallIntervalRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final boolean expectedResult = false;

        // Act
        final boolean actualResult = authorizer.applyHighFrequencySmallIntervalRule(new LinkedList<>(Arrays
                .asList(buildTransaction(100), buildTransaction(100))));

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void applyDoubledTransactionRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final boolean expectedResult = true;

        // Act
        final boolean actualResult = authorizer.applyDoubledTransactionRule(buildTransaction(100),
                new LinkedList<>(Arrays.asList(buildTransaction(100))));

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void notApplyDoubledTransactionRuleTest() {
        // Arrange
        final Authorizer authorizer = buildAuthorizer();
        final boolean expectedResult = false;

        // Act
        final boolean actualResult = authorizer.applyDoubledTransactionRule(buildTransaction(100),
                new LinkedList<>(Collections.singletonList(buildTransaction(150))));

        // Assert
        Assertions.assertEquals(expectedResult, actualResult);
    }

    private Authorizer buildAuthorizer() {
        return (account, transaction, transactions) -> false;
    }

    private Account buildActiveAccount() {
        return new Account(true, 100);
    }

    private Account buildInactiveAccount() {
        return new Account(false, 100);
    }

    private Transaction buildTransaction(final int amount) {
        return new Transaction("merchant", amount, ZonedDateTime.now());
    }
}
