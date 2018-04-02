package nl.nanda.web.listener;

import nl.nanda.service.TransferService;
import nl.nanda.web.event.TransferEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * This Transfer Listener service is calling the service to do the work (crud
 * operatios).
 *
 */
@Component
public class TransferEventListener implements
        ApplicationListener<TransferEvent> {

    @Autowired
    private TransferService transferService;

    @Override
    public void onApplicationEvent(final TransferEvent transferEvent) {
        transferService.doTransfer(transferEvent.getTransfer());

    }

}
