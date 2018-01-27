package nl.nanda.web;

import java.util.List;

import nl.nanda.account.Account;
import nl.nanda.service.TransferService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * A controller handling requests for CRUD operations on Accounts and their
 * Beneficiaries.
 */
@Controller
public class AccountController {

    // private final Logger logger = Logger.getLogger(getClass());

    private final TransferService transferService;

    @Autowired
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

}