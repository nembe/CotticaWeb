package nl.nanda.web.event;

import nl.nanda.transfer.Transfer;

import org.springframework.context.ApplicationEvent;

/**
 * Event that holds the Transfer object that we can past to the Transfer
 * listener.
 *
 */
public class TransferEvent extends ApplicationEvent {

    /**
     * 
     */
    private static final long serialVersionUID = 2558920868852196603L;
    private Transfer transfer;

    public TransferEvent(final Object source, final Transfer transfer) {
        super(source);
        this.setTransfer(transfer);
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(final Transfer transfer) {
        this.transfer = transfer;
    }

}
