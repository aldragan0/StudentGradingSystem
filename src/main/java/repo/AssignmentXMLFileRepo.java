package repo;

import domain.Assignment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.DataConvert;
import validator.Validator;

public class AssignmentXMLFileRepo extends AbstractXMLFileRepo<String, Assignment> {
    /**
     * @param validator validator(validates entities)
     * @param filename  filename associated with repo
     */
    public AssignmentXMLFileRepo(Validator<Assignment> validator, String filename) {
        super(validator, filename);
    }

    @Override
    protected Assignment getEntityFrom(String line) {
        return null;
    }

    @Override
    protected Assignment createEntityFromElement(Element elem) {
        return DataConvert.assignmentFromElement(elem);
    }

    @Override
    protected Element createElementFromEntity(Document document, Assignment entity) {
        return DataConvert.elementFromAssignment(document, entity);
    }
}
