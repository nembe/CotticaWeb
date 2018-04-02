package nl.nanda.web.listener;

import nl.nanda.service.TransferService;
import nl.nanda.web.event.AccountEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * This Account Listener service is calling the service to do the work (crud
 * operatios).
 *
 */
@Component
public class AccountEventListener implements ApplicationListener<AccountEvent> {

    @Autowired
    private TransferService transferService;

    @Override
    public void onApplicationEvent(final AccountEvent accountEvent) {
        transferService.saveAccount(accountEvent.getAccount());

    }
}