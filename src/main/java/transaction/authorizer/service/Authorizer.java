package transaction.authorizer.service;


import transaction.authorizer.model.account.Account;
import transaction.authorizer.model.transaction.Transaction;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.LinkedList;

@FunctionalInterface
public interface Authorizer {

    int DEFAULT_MAX_FREQUENCY_TRANSACTION_PER_INTERVAL = 3;
    int DEFAULT_MAX_INTERVAL_IN_SECONDS =  120;

    boolean authorize(final Account account, final Transaction transaction,
                      final LinkedList<Transaction> transactions);

    default boolean applyAccountAlreadyInitializedRule(final Collection<Account> accounts) {
        return accounts.size() > 0;
    }

    default boolean applyCardNotActiveRule(final Account account) {
        return account != null && !account.isActiveCard();
    }

    default boolean applyInsufficientLimitRule(final int availableLimit, final Transaction transaction) {
        return transaction.getAmount() > availableLimit;
    }

    default boolean applyHighFrequencySmallIntervalRule(final LinkedList<Transaction> transactions) {
        if (transactions.size() >= DEFAULT_MAX_FREQUENCY_TRANSACTION_PER_INTERVAL) {
            return differenceInSeconds(
                    transactions.get(transactions.size() - DEFAULT_MAX_FREQUENCY_TRANSACTION_PER_INTERVAL).getTime(),
                    transactions.getLast().getTime()
            ) < DEFAULT_MAX_INTERVAL_IN_SECONDS;
        }

        return false;
    }

    default boolean applyDoubledTransactionRule(final Transaction transaction,
                                                final LinkedList<Transaction> transactions) {
        return transactions.stream()
                .anyMatch(tst -> tst.getAmount() == transaction.getAmount()
                        && tst.getMerchant().equals(transaction.getMerchant())
                        && differenceInSeconds(tst.getTime(), transaction.getTime())
                        < DEFAULT_MAX_INTERVAL_IN_SECONDS);
    }

    default long differenceInSeconds(ZonedDateTime start, ZonedDateTime end) {
        return Duration.between(start, end).getSeconds();
    }
}
