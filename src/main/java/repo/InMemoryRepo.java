package repo;

import domain.HasID;
import exceptions.ValidationException;
import validator.Validator;

import java.util.*;

/**
 * @param <ID>-type E must have an attribute of type ID
 * @param <E>-type of entities saved in repository
 */
public class InMemoryRepo<ID extends Comparable<ID>, E extends HasID<ID>> implements PagedCrudRepository<ID, E> {
    private Integer currentPage;
    private int entitiesPerPage = 3;
    private Validator<E> validator;
    private Map<ID, E> items;

    /**
     * @param validator - validator
     */
    public InMemoryRepo(Validator<E> validator){
        this.currentPage = 0;
        this.validator = validator;
        items = new HashMap<>();
    }

    /**
     * @return list of entities - if the repo has a next page
     *         null - otherwise
     */
    @Override
    public List<E> getNextPage() {
        int firstElem = currentPage * entitiesPerPage;
        int lastElem = Math.min((currentPage + 1) * entitiesPerPage, items.size());

        if(!hasNextPage()) return null;

        List<E> pageItems = new ArrayList<>();
        List<E> entities = new ArrayList<>(items.values());
        entities.sort(Comparator.comparing(HasID::getID));

        for(;firstElem < lastElem; ++firstElem){
            pageItems.add(entities.get(firstElem));
        }
        currentPage = Math.min(items.size() + 1 / entitiesPerPage, currentPage + 1);
        return pageItems;
    }

    /**
     * @return list of entities - if the repo has a prev page
     *         null - otherwise
     */
    @Override
    public List<E> getPrevPage() {
        int firstElem = (currentPage - 1) * entitiesPerPage;
        int lastElem = Math.min((currentPage) * entitiesPerPage, items.size());

        if(!hasPrevPage()) return null;

        List<E> pageItems = new ArrayList<>();
        List<E> entities = new ArrayList<>(items.values());
        entities.sort(Comparator.comparing(HasID::getID));

        for(;firstElem < lastElem; ++firstElem){
            pageItems.add(entities.get(firstElem));
        }
        currentPage = Math.max(0, currentPage - 1);
        return pageItems;
    }

    /**
     * @return true - the current page isn't the first page
     *         false - otherwise
     */
    @Override
    public boolean hasPrevPage() {
        return currentPage > 0;
    }

    /**
     * @return true - the current page isn't the last page
     *         false - otherwise
     */
    @Override
    public boolean hasNextPage() {
        return currentPage * entitiesPerPage < items.size();
    }

    /**
     * @param pageNumber the page of the repo
     */
    @Override
    public void setPageNumber(int pageNumber) {
        this.currentPage = pageNumber;
    }

    /**
     * @return number of the current page in the repo
     */
    @Override
    public int getPageNumber(){
        return this.currentPage;
    }

    /**
     * set the number of items that a page contains
     * @param numEntities number of items in a page
     */
    @Override
    public void setEntitiesPerPage(int numEntities) {
        this.entitiesPerPage = numEntities;
    }


    /**
     * @param id-the id of the entity to be returned
     * @return entity
     */
    @Override
    public E findOne(ID id) {
        if(id == null) throw new IllegalArgumentException();
        return items.get(id);
    }

    /**
     * @return all entities
     */
    @Override
    public Collection<E> findAll() {
        return items.values();
    }

    /**
     * save an entity to repo
     * @param entity - entity must be not null
     * @return null if entity was saved, otherwise return the entity
     * @throws IllegalArgumentException if entity is null
     * @throws ValidationException if entity is not valid
     */
    @Override
    public E save(E entity) throws ValidationException {
        if(entity == null) throw new IllegalArgumentException();
        validator.validate(entity);
        if(findOne(entity.getID()) == null){
            items.put(entity.getID(), entity);
            return null;
        }
        return entity;
    }

    /**
     * delete an entity from repo
     * @param id - id must be not null
     * @return entity if it was deleted, else return null
     */
    @Override
    public E delete(ID id) {
        E returnedEntity = findOne(id);
        if(returnedEntity != null){
            items.remove(id);
        }
        return returnedEntity;
    }

    /**
     * update an entity in repo
     * @param entity entity must not be null
     * @return null if entity was updated, else return entity
     * @throws IllegalArgumentException if entity is null
     * @throws ValidationException if entity is not valid
     */
    @Override
    public E update(E entity) {
        if(entity == null) throw new IllegalArgumentException();
        validator.validate(entity);
        if(findOne(entity.getID()) == null){
            return entity;
        }
        items.put(entity.getID(), entity);
        return null;
    }
}