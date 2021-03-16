package transaction.authorizer.service.transaction;

import transaction.authorizer.model.account.Account;
import transaction.authorizer.model.transaction.Transaction;
import transaction.authorizer.service.Authorizer;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class TransactionService {

    private static final TransactionService INSTANCE = new TransactionService();
    private static final BinaryOperator<Integer> SUBTRACT_LIMIT = (limit, amount) -> limit - amount;

    private static final String INSUFFICIENT_LIMIT_RULE = "insufficient-limit";

    private final LinkedList<Transaction> transactions = new LinkedList<>();
    private final LinkedList<String> violations = new LinkedList<>();
    private int currentAvailableLimit = 0;

    public TransactionService() {}

    public static TransactionService getInstance() { return INSTANCE; }

    public String processTransaction(final Account account, final String merchant,
                                     final int amount, final ZonedDateTime time) {
        if (Optional.ofNullable(account).isEmpty()) {
            return "account-not-initialized";
        } else {
            final Transaction transaction = new Transaction(merchant, amount, time);

            if (authorizer.authorize(account, transaction, transactions)) {
                transactions.add(new Transaction(merchant, amount, time));
            }

            final String transactionOutput = output(account,
                    transactions.isEmpty() && violations.contains(INSUFFICIENT_LIMIT_RULE)
                            ? account.getAvailableLimit() : currentAvailableLimit);

            violations.clear();
            return transactionOutput;
        }
    }

    private String output(final Account account, final int availableLimit) {
        return "{\"account\": {\"active-card\": " + account.isActiveCard()
                + ", \"available-limit\": " + availableLimit
                + "}, \"violations\": " + violations.toString() + "}\n";
    }

    private final Authorizer authorizer = new Authorizer() {
        @Override
        public boolean authorize(final Account account, final Transaction transaction,
                                 final LinkedList<Transaction> transactions) {
            final int availableLimit = transactions.isEmpty()
                    ? account.getAvailableLimit() : currentAvailableLimit;
            boolean shouldAuthorize = true;

            if (this.applyCardNotActiveRule(account)) {
                violations.add("card-not-active");
                shouldAuthorize = false;
            }

            if (this.applyInsufficientLimitRule(availableLimit, transaction)) {
                violations.add(INSUFFICIENT_LIMIT_RULE);
                shouldAuthorize = false;
            }

            if (this.applyHighFrequencySmallIntervalRule(transactions)) {
                violations.add("high-frequency-small-interval");
                shouldAuthorize = false;
            }

            if (this.applyDoubledTransactionRule(transaction, transactions)) {
                violations.add("doubled-transaction");
                shouldAuthorize = false;
            }

            if (shouldAuthorize && transactions.isEmpty()) {
                currentAvailableLimit = SUBTRACT_LIMIT.apply(availableLimit, transaction.getAmount());
            } else if (shouldAuthorize) {
                currentAvailableLimit = SUBTRACT_LIMIT.apply(availableLimit, transaction.getAmount());
            }

            return shouldAuthorize;
        }
    };
}
