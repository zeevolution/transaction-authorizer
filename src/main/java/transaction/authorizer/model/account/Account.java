package transaction.authorizer.model.account;

public final class Account {

    private final boolean activeCard;
    private final int availableLimit;

    public Account(final boolean activeCard, final int availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    public boolean isActiveCard() {
        return activeCard;
    }

    public int getAvailableLimit() {
        return availableLimit;
    }
}
