package nl.nanda.web.event;

import nl.nanda.account.Account;

import org.springframework.context.ApplicationEvent;

/**
 * Event that holds the Account object that we can past to the Account listener.
 *
 */
public class AccountEvent extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -3485187697443835190L;
    private final Account account;

    public AccountEvent(final Object source, final Account account) {
        super(source);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

}
