package transaction.authorizer.consumer;


import transaction.authorizer.model.account.Account;
import transaction.authorizer.service.account.AccountService;
import transaction.authorizer.service.transaction.TransactionService;

import java.time.ZonedDateTime;

public class AuthorizerConsumer {

    private static final AuthorizerConsumer INSTANCE = new AuthorizerConsumer();

    private final TransactionService transactionService = TransactionService.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    private AuthorizerConsumer() {}

    public static AuthorizerConsumer getInstance() { return INSTANCE; }

    public void processAccount(final boolean activeCard, final int availableLimit) {
        System.out.println(accountService.processAccount(activeCard, availableLimit));
    }

    public void processTransaction(final String merchant, final int amount, final ZonedDateTime time) {
        final Account account = accountService.getAccounts().isEmpty() ? null : accountService.getAccount(0);
        System.out.println(transactionService.processTransaction(account, merchant, amount, time));
    }
}
