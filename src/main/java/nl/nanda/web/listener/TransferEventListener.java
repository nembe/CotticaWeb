package nl.nanda.web.listener;

import nl.nanda.service.TransferService;
import nl.nanda.web.event.TransferEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TransferEventListener implements
        ApplicationListener<TransferEvent> {

    @Autowired
    private TransferService transferService;

    @Override
    public void onApplicationEvent(final TransferEvent transferEvent) {
        transferService.saveTransfer(transferEvent.getTransfer());

    }

}
