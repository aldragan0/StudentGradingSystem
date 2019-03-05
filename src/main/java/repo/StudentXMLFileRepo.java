package repo;

import domain.Student;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import utils.DataConvert;
import validator.Validator;

public class StudentXMLFileRepo extends AbstractXMLFileRepo<String, Student> {
    /**
     * @param validator validator(validates entities)
     * @param filename  filename associated with repo
     */
    public StudentXMLFileRepo(Validator<Student> validator, String filename) {
        super(validator, filename);
    }

    @Override
    protected Student getEntityFrom(String line) {
        return null;
    }

    @Override
    protected Student createEntityFromElement(Element elem) {
        return DataConvert.studentFromElement(elem);
    }

    @Override
    protected Element createElementFromEntity(Document document, Student entity) {
        return DataConvert.elementFromStudent(document, entity);
    }
}
