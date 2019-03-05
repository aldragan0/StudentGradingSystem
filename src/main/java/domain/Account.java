package domain;

import java.util.Objects;

public class Account implements HasID<String>{
    private String accountName;
    private String password;
    private AccountType accountType;

    public Account(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
        this.accountType = AccountType.STUDENT;
    }

    public Account(String accountName, String password, AccountType accountType) {
        this.accountName = accountName;
        this.password = password;
        this.accountType = accountType;
    }

    @Override
    public String getID() {
        return this.accountName + this.password;
    }

    @Override
    public void setID(String id) {}

    public String getAccountName(){
        return this.accountName;
    }

    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountName, account.accountName) &&
                Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName, password);
    }

    @Override
    public String toString() {
        return  accountName + " " + password + " "
                + accountType;
    }
}
