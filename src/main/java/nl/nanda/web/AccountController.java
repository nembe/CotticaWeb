package nl.nanda.web;

import java.net.URI;
import java.util.List;

import nl.nanda.account.Account;
import nl.nanda.exception.SvaException;
import nl.nanda.exception.SvaNotFoundException;
import nl.nanda.service.TransferService;
import nl.nanda.web.event.AccountEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriTemplate;

/**
 * A controller handling requests for CRUD operations on Accounts.
 */
@Controller
public class AccountController {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private TransferService transferService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public AccountController() {
    }

    public AccountController(final TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Provide a list of all accounts.
     */
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public @ResponseBody List<Account> accountSummary() {
        return transferService.findAllAccounts();
    }

    /**
     * Provide the details of an account with the given id.
     */
    @RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
    public @ResponseBody Account accountDetails(@PathVariable final Long id) {
        return transferService.getAccount(id);
    }

    /**
     * Creates a new Account, setting its URL as the Location header on the
     * response.
     */
    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    // 201
    public HttpEntity<String> createAccount(
            @RequestBody final Account newAccount,
            @Value("#{request.requestURL}") final StringBuffer url) {

        final Long account = newAccount.getEntityId();
        startAccountSavingEvent(newAccount);
        // final Long account = transferService.saveAccount(newAccount);
        return entityWithLocation(url, account);
    }

    private HttpEntity<String> entityWithLocation(final StringBuffer url,
            final Object resourceId) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(getLocationForChildResource(url, resourceId));
        return new HttpEntity<String>(headers);
    }

    private void startAccountSavingEvent(final Account newAccount) {
        final AccountEvent event = new AccountEvent(this, newAccount);
        eventPublisher.publishEvent(event);
    }

    /**
     * Maps IllegalArgumentExceptions to a 409 Conflict HTTP status code.
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SvaException.class)
    public void handleConflict(final Exception ex) {
        logger.error("Exception is: ", ex);
    }

    /**
     * Maps DataIntegrityViolationException to a 404 Not Found HTTP status code.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleViolation(final Exception ex) {
        logger.error("Exception is: ", ex);
    }

    /**
     * Maps SvaNotFoundException to a 404 Not Found HTTP status code.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SvaNotFoundException.class)
    public void handleNotFound(final Exception ex) {
        logger.error("Exception is: ", ex);
    }

    /**
     * determines URL of child resource based on the full URL of the given
     * request, appending the path info with the given childIdentifier using a
     * UriTemplate.
     */
    private URI getLocationForChildResource(final StringBuffer url,
            final Object childIdentifier) {
        final UriTemplate template = new UriTemplate(url.append("/{childId}")
                .toString());
        return template.expand(childIdentifier);
    }

}