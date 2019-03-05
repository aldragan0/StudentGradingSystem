package repo;

import java.util.List;

public interface PagedCrudRepository<ID, E> extends CrudRepository<ID, E> {


    /**
     * @return list of entities defining the next page in the repo
     */
    List<E> getNextPage();

    /**
     * @return list of entities defining the prev page in the repo
     */
    List<E> getPrevPage();

    /**
     * check if the repo has a prev page
     * @return true - the page isn't the first page
     *         false - otherwise
     */
    boolean hasPrevPage();

    /**
     * check if the repo has a next page
     * @return true - the page isn't the final page
     *         false - otherwise
     */
    boolean hasNextPage();

    /**
     * set the repoPage to a pageNumber
     * @param pageNumber the page of the repo
     */
    void setPageNumber(int pageNumber);

    /**
     * @return current page number
     */
    int getPageNumber();

    /**
     * set the number of elements in a page
     * @param numEntities number of items in a page
     */
    void setEntitiesPerPage(int numEntities);
}
