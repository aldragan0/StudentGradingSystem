package domain;

import org.junit.Test;

import static org.junit.Assert.*;

public class AccountTest {
    private Account account = new Account("david", "password", AccountType.STUDENT);

    @Test
    public void getID() {
        assertEquals(account.getID(), account.getAccountName()
                + account.getPassword());
    }

    @Test
    public void getAccountName() {
        assertEquals(account.getAccountName(), "david");
    }

    @Test
    public void setAccountName() {
        account.setAccountName("lucas");
        assertEquals(account.getAccountName(), "lucas");
    }

    @Test
    public void getPassword() {
        assertEquals(account.getPassword(), "password");
    }

    @Test
    public void setPassword() {
        account.setPassword("pass");
        assertEquals(account.getPassword(), "pass");
    }

    @Test
    public void getAccountType() {
        assertEquals(account.getAccountType(), AccountType.STUDENT);
    }

    @Test
    public void setAccountType() {
        account.setAccountType(AccountType.TEACHER);
        assertEquals(account.getAccountType(), AccountType.TEACHER);
    }

    @Test
    public void equals() {
        Account account = new Account("stella", "pass",
                AccountType.SECRETARY);
        Account accountCopy = new Account("stella", "pass",
                AccountType.TEACHER);
        Account account1 = new Account("maria", "pass",
                AccountType.TEACHER);
        assertEquals(account, accountCopy);
        assertNotEquals(account, account1);
    }
}