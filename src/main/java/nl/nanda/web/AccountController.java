package nl.nanda.web;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import nl.nanda.account.Account;
import nl.nanda.domain.CrunchifyRandomNumber;
import nl.nanda.exception.AnanieException;
import nl.nanda.service.TransferService;
import nl.nanda.transaction.Transaction;
import nl.nanda.transfer.Transfer;
import nl.nanda.web.event.AccountEvent;
import nl.nanda.web.event.TransferEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
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

//TODO: Test when the string is not a UUID (IllegalArgumentException).
/**
 * A controller handling requests for CRUD operations on Accounts in the AAT
 * application.
 */
@Controller
public class AccountController {

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
    public @ResponseBody List<String> accountSummary() {
        return transferService.findAllAccounts().stream().map(Account::getName).collect(Collectors.toList());

    }

    /**
     * Provide a list of all transfers.
     */
    @RequestMapping(value = "/transfers", method = RequestMethod.GET)
    public @ResponseBody List<Transfer> transferSummary() {
        return transferService.findAllTransfers();
    }

    /**
     * Provide the details of an account with the given id.
     */
    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    public @ResponseBody Account accountDetails(@PathVariable final String id) {
        return transferService.getAccount(id);
    }

    /**
     * Provide the details of an owner of a account by name.
     */
    @RequestMapping(value = "/owner/{name}", method = RequestMethod.GET)
    public @ResponseBody Account accountOwnerDetails(@PathVariable final String name) {
        return transferService.findAccountByName(name);
    }

    /**
     * Find the transactions of an account with the account UUID..
     */
    @RequestMapping(value = "/transaction/{id}", method = RequestMethod.GET)
    public @ResponseBody List<Transaction> findTransactionByOwnerDetails(@PathVariable final String id) {
        return transferService.findTransactionByAccount(id);
    }

    /**
     * Provide the details of an transfer with the given id.
     */
    @RequestMapping(value = "/transfer/{id}", method = RequestMethod.GET)
    public @ResponseBody Transfer transferDetails(@PathVariable final String id) {
        return transferService.findTransferById(Integer.valueOf(id));
    }

    /**
     * Creates a new Account, setting its URL as the Location header on the
     * response. RequestBody = instantieert Object op basis van instance
     * variabelen en niet via Constructor.
     */
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public HttpEntity<String> createAccount(@RequestBody final Account newAccount, @Value("#{request.requestURL}") final StringBuffer url) {

        final UUID accountUUID = UUID.randomUUID();
        newAccount.setAccountUUID(accountUUID);
        startAccountSavingEvent(newAccount);
        return entityWithLocation(url, accountUUID);
    }

    /**
     * Starts a Transfer, setting its URL as the Location header on the
     * response.
     */
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public HttpEntity<String> startTransfer(@RequestBody final Transfer newTransfer, @Value("#{request.requestURL}") final StringBuffer url) {

        newTransfer.setEntityId(CrunchifyRandomNumber.generateRandomNumber());
        startTransferEvent(newTransfer);
        return entityWithLocation(url, newTransfer.getEntityId());
    }

    /**
     * Creating a URL than the clien can lookup the status of the request.
     * 
     * @param url
     * @param resourceId
     * @return
     */
    private HttpEntity<String> entityWithLocation(final StringBuffer url, final Object resourceId) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setLocation(getLocationForChildResource(url, resourceId));
        return new HttpEntity<String>(headers);
    }

    /**
     * Creating an Account in the AAT application.
     * 
     * @param newAccount
     */
    private void startAccountSavingEvent(final Account newAccount) {
        final AccountEvent event = new AccountEvent(this, newAccount);
        eventPublisher.publishEvent(event);
    }

    /**
     * Starting an Transfer in the AAT application.
     * 
     */
    private void startTransferEvent(final Transfer newTransfer) {
        final TransferEvent event = new TransferEvent(this, newTransfer);
        eventPublisher.publishEvent(event);
    }

    /**
     * Maps AnanieException.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AnanieException.class)
    public void handleConflict(final Exception ex) {
    }

    /**
     * Maps ConstraintViolationException to a HTTP status code.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleViolation(final Exception ex) {
    }

    /**
     * Maps IllegalArgumentException when given string is not a UUID.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleNotUuidString(final Exception ex) {
    }

    /**
     * determines URL of child resource based on the full URL of the given
     * request, appending the path info with the given childIdentifier using a
     * UriTemplate.
     */
    private URI getLocationForChildResource(final StringBuffer url, final Object childIdentifier) {
        final UriTemplate template = new UriTemplate(url.append("/{childId}").toString());
        return template.expand(childIdentifier);
    }

}