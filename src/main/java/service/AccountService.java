package service;

import domain.Account;
import repo.CrudRepository;
import utils.DataConvert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

public class AccountService implements Service<Account> {
    private CrudRepository<String, Account> repo;

    public AccountService(CrudRepository<String, Account> repo){
        this.repo = repo;
    }

    /**
     * @return all entities in the repository
     */
    @Override
    public Collection<Account> getAll() {
        return repo.findAll();
    }

    /**
     * @param message byte-array representation of a message
     * @return hexadecimal String representing the digest of the message
     */
    private String hashMessage(String message){
        byte[] messageBytes = message.getBytes();
        try{
            MessageDigest messageDigest;
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashedMessage = messageDigest.digest(messageBytes);
            return DataConvert.bytesToHex(hashedMessage);
        } catch (NoSuchAlgorithmException ex) {
            System.err.println("AccountService:-- NO SUCH ALGORITHM EXISTS");
            return null;
        }
    }

    /**
     * checks if an account exists in the database
     * @param account account containing a plaintext password
     * @return account - if an account with those credentials exists
     *         null - otherwise
     */
    public Account accountExists(Account account){
        String hashedPassword = hashMessage(account.getPassword());
        account.setPassword(hashedPassword);
        return repo.findOne(account.getID());
    }

    /**
     * add an account
     * @param account account to be added to the repo
     * @return null - the account was added
     *         account - the account already exists
     */
    public Account addAccount(Account account){
        if(account == null) throw new IllegalArgumentException();
        String hashedPassword = hashMessage(account.getPassword());
        account.setPassword(hashedPassword);
        return repo.save(account);
    }

    /**
     * remove an account
     * @param account account to be deleted from the repo
     * @return account - the account was deleted
     *         null - the account does not exist
     */
    public Account removeAccount(Account account){
        if(account == null) throw new IllegalArgumentException();
        String hashedPassword = hashMessage(account.getPassword());
        account.setPassword(hashedPassword);
        return repo.delete(account.getID());
    }
}
