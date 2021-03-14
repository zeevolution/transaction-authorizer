package transaction.authorizer.model.transaction;

import java.time.ZonedDateTime;

public final class Transaction {

    private final String merchant;
    private final ZonedDateTime time;
    private final int amount;

    public Transaction(final String merchant, final int amount, final ZonedDateTime time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    public String getMerchant() {
        return merchant;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public int getAmount() {
        return amount;
    }
}
