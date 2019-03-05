package repo;

import domain.Grade;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.DataConvert;
import validator.Validator;

public class GradeXMLFileRepo extends AbstractXMLFileRepo<String, Grade> {
    /**
     * @param validator validator(validates entities)
     * @param filename  filename associated with repo
     */
    public GradeXMLFileRepo(Validator<Grade> validator, String filename) {
        super(validator, filename);
    }

    @Override
    protected Grade getEntityFrom(String line) {
        return null;
    }

    @Override
    protected Grade createEntityFromElement(Element elem) {
        return DataConvert.gradeFromElement(elem);
    }

    @Override
    protected Element createElementFromEntity(Document document, Grade entity) {
        return DataConvert.elementFromGrade(document, entity);
    }
}
