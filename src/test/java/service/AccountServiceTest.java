package service;

import domain.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repo.AccountXMLFileRepo;
import repo.CrudRepository;
import validator.AccountValidator;

import static org.junit.Assert.*;

public class AccountServiceTest {
    private AccountService service;

    @Before
    public void setService(){
        CrudRepository<String, Account> repository = new AccountXMLFileRepo(new AccountValidator(),
                    "src/test/resources/accountTest.xml");
        service = new AccountService(repository);
    }

    @Test
    public void getAll() {
        assertEquals(service.getAll().size(), 6);
        service.getAll().forEach(Assert::assertNotNull);
    }

    @Test
    public void accountExists() {
        Account account = new Account("mihai", "password");
        Account response = service.accountExists(account);
        assertNull(response);
        Account existentAccount = new Account("teacher1", "teacher1");
        response = service.accountExists(existentAccount);
        assertNotNull(response);
    }

    @Test
    public void addAccount() {
        Account account = new Account("petru", "parola");
        Account response = service.addAccount(account);
        assertNull(response);
        account = new Account("petru", "parola");
        response = service.addAccount(account);
        assertNotNull(response);
        try{
            service.addAccount(null);
            fail();
        } catch (IllegalArgumentException ex){
            assertTrue(true);
        }
    }

    @Test
    public void removeAccount() {
        Account account = new Account("petru", "parola");
        Account response = service.removeAccount(account);
        assertNotNull(response);
        response = service.removeAccount(account);
        assertNull(response);
        try{
            service.removeAccount(null);
            fail();
        } catch (IllegalArgumentException ex){
            assertTrue(true);
        }
    }
}