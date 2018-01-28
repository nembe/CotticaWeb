package nl.nanda.web;

import java.math.BigDecimal;
import java.net.URI;

import nl.nanda.account.Account;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

public class AccountClientTest {
    /**
     * server URL ending with the servlet mapping on which the application can
     * be reached.
     */
    private static final String BASE_URL = "http://localhost:8080";

    private final RestTemplate restTemplate = new RestTemplate();
    private Monitor monitor = null;

    // @Test
    public void getAccount() {
        final String url = BASE_URL + "/accounts/{accountId}";
        final Account account = restTemplate
                .getForObject(url, Account.class, 0);
        System.out.println("getAccount " + account.getName());
    }

    @Test
    public void createAccount() {
        final String url = BASE_URL + "/accounts";

        final Account account = new Account(BigDecimal.valueOf(1000.50),
                BigDecimal.valueOf(20.00), "Eddie");
        monitor = MonitorFactory.start("JavaMonitor");
        final URI newAccountLocation = restTemplate.postForLocation(url,
                account);
        monitor.stop();

        // final Account retrievedAccount = restTemplate.getForObject(
        // newAccountLocation, Account.class);
        System.out.println("1 createAccount " + newAccountLocation.getPath());
        // System.out.println("2 createAccount " + retrievedAccount.getName());
        System.out.println("3 createAccount " + monitor);
    }

    // @Test
    public void listAccounts() {
        final String url = BASE_URL + "/accounts";

        final ResponseEntity<Account[]> responseEntity = restTemplate
                .getForEntity(url, Account[].class);
        final Account[] accounts = responseEntity.getBody();
        final MediaType contentType = responseEntity.getHeaders()
                .getContentType();
        final HttpStatus statusCode = responseEntity.getStatusCode();

        for (int i = 0; i < accounts.length; i++) {
            System.out.println("listAccounts " + accounts[i].getName());
        }

    }
}
