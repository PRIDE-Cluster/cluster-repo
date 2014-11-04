package uk.ac.ebi.pride.spectracluster.repo.utils.paging;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jose A Dianes
 *
 * All the credit goes to http://codefutures.com/spring-jdbc-pagination/
 */
public class Page<E> {

    private int pageNumber;
    private int pagesAvailable;
    private List<E> pageItems = new ArrayList<E>();

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPagesAvailable(int pagesAvailable) {
        this.pagesAvailable = pagesAvailable;
    }

    public void setPageItems(List<E> pageItems) {
        this.pageItems = pageItems;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPagesAvailable() {
        return pagesAvailable;
    }

    public List<E> getPageItems() {
        return pageItems;
    }
}