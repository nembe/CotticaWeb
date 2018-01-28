package nl.nanda.web.listener;

import nl.nanda.service.TransferService;
import nl.nanda.web.event.AccountEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AccountEventListener implements ApplicationListener<AccountEvent> {

    @Autowired
    private TransferService transferService;

    @Override
    public void onApplicationEvent(final AccountEvent accountEvent) {
        System.out.println("AccountEventListener "
                + accountEvent.getAccount().getEntityId());
        transferService.saveAccount(accountEvent.getAccount());

    }
}