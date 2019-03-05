package repo;

import domain.HasID;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import validator.Validator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public abstract class AbstractXMLFileRepo<ID extends Comparable<ID>, E extends HasID<ID>> extends AbstractFileRepo<ID, E> {
    /**
     * @param validator validator(validates entities)
     * @param filename  filename associated with repo
     */
    AbstractXMLFileRepo(Validator<E> validator, String filename) {
        super(validator, filename);
    }


    protected abstract E createEntityFromElement(Element elem);
    protected abstract Element createElementFromEntity(Document document, E entity);

    /**
     * store current repo state in a file
     */
    @Override
    final protected void storeToFile(){
        try{
            //make the new document
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .newDocument();

            //add the root node to the document
            Element root = document.createElement("List");
            document.appendChild(root);

            //for each element in the repo add a new node as a child to the root
            findAll().forEach(entity->{
                Element element = createElementFromEntity(document, entity);
                root.appendChild(element);
            });

            //write document to file
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(super.filename));

        } catch (ParserConfigurationException
                 | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * load repo items from file
     */
    @Override
    final protected void loadFromFile(){
        try{
            //get the xml doc
            Document document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(super.filename);

            //get the root element of the doc
            Element root = document.getDocumentElement();

            //get all the items in the root
            NodeList entityElements = root.getChildNodes();

            //for each transform into an entity and store to repo
            for(int i = 0; i < entityElements.getLength(); ++i){
                Node element =  entityElements.item(i);
                if(element.getNodeType() == Node.ELEMENT_NODE) {
                    E entity = createEntityFromElement((Element)element);
                    super.save(entity);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
