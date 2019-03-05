package repo;

import domain.Account;
import domain.AccountType;
import exceptions.ValidationException;
import org.junit.Before;
import org.junit.Test;
import validator.AccountValidator;

import static org.junit.Assert.*;

public class AccountFileRepoTest {
    private CrudRepository<String, Account> repo;

    @Before
    public void setRepo(){
        repo = new AccountXMLFileRepo(new AccountValidator(),
                "src/test/resources/accountTest.xml");
    }

    @Test
    public void save(){
        Account account = new Account("david", "password",
                AccountType.TEACHER);
        Account invalidAccount1 = new Account("", "",
                AccountType.STUDENT);
        assertEquals(repo.findAll().size(), 5);
        try{
            repo.save(invalidAccount1);
            fail();
        } catch (ValidationException ex){
            assertTrue(true);
        }
        try{
            repo.save(null);
            fail();
        } catch (IllegalArgumentException ex){
            assertTrue(true);
        }
        Account returnedAcc = repo.save(account);
        assertNull(returnedAcc);

        returnedAcc = repo.save(account);
        assertNotNull(returnedAcc);
    }

    @Test
    public void delete(){
        Account account = new Account("david", "password",
                AccountType.TEACHER);
        Account dummyAcc = new Account("____", "____",
                AccountType.STUDENT);
        Account response = repo.delete(dummyAcc.getID());
        assertNull(response);
        try{
            repo.delete(null);
            fail();
        }catch (IllegalArgumentException ex){
            assertTrue(true);
        }
        Account returnedEntity = repo.delete(account.getID());
        assert(returnedEntity != null);
    }
}