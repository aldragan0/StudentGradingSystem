package repo;

import domain.Account;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.DataConvert;
import validator.Validator;

public class AccountXMLFileRepo extends AbstractXMLFileRepo<String, Account> {
    /**
     * @param validator validator(validates entities)
     * @param filename  filename associated with repo
     */
    public AccountXMLFileRepo(Validator<Account> validator, String filename) {
        super(validator, filename);
    }

    @Override
    protected Account createEntityFromElement(Element elem){
        return DataConvert.accountFromElement(elem);
    }

    @Override
    protected Element createElementFromEntity(Document document, Account entity) {
        return DataConvert.elementFromAccount(document, entity);
    }

    @Override
    protected Account getEntityFrom(String line) {
        return null;
    }
}
