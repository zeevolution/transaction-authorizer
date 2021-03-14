package transaction.authorizer.service.account;


import transaction.authorizer.model.account.Account;
import transaction.authorizer.model.transaction.Transaction;
import transaction.authorizer.service.Authorizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AccountService {

    private static final AccountService INSTANCE = new AccountService();

    private final List<Account> accounts = new ArrayList<>();
    private final LinkedList<String> violations = new LinkedList<>();

    private AccountService() {}

    public static AccountService getInstance() { return INSTANCE; }

    public String processAccount(final boolean activeCard, final int availableLimit) {
        final Account account = new Account(activeCard, availableLimit);

        if (authorizer.authorize(account, null, null)) {
            accounts.add(account);
        }

        final String accountOutput = output();

        violations.clear();
        return accountOutput;
    }

    public Account getAccount(int index) {
        return accounts.get(index);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    private String output() {
        return "{\"account\": {\"active-card\": " + getAccount(0).isActiveCard()
                + ", \"available-limit\": " + getAccount(0).getAvailableLimit()
                + "}, \"violations\": " + violations.toString() + "}\n";
    }

    private final Authorizer authorizer = new Authorizer() {
        @Override
        public boolean authorize(Account account, Transaction transaction, LinkedList<Transaction> transactions) {
            if (this.applyAccountAlreadyInitializedRule(accounts)) {
                violations.add("account-already-initialized");
                return false;
            }

            return true;
        }
    };

}
