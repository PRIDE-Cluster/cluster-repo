package uk.ac.ebi.pride.spectracluster.repo.utils.paging;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Jose A Dianes
 *         <p/>
 *         All the credit goes to http://codefutures.com/spring-jdbc-pagination/
 */
public class PaginationHelper<E> {

    /**
     * This method was the original implementation and do two queries to fetch the data.
     * @param jt The JDBCTemplate used to fech the data
     * @param sqlCountRows SQL to count the total number of elements in the query
     * @param sqlFetchRows SQL query to be fech
     * @param args
     * @param pageNo Page Number to be fetch
     * @param pageSize Page Size, Number of Elements to retrieve
     * @param rowMapper The mapper of each element from the raw data to the original objects
     * @return
     */
    public Page<E> fetchPage(
            final JdbcTemplate jt,
            final String sqlCountRows,
            final String sqlFetchRows,
            final Object args[],
            final int pageNo,
            final int pageSize,
            final RowMapper<E> rowMapper) {

        // determine how many rows are available
        final long rowCount = jt.queryForObject(sqlCountRows, Long.class, args);

        // calculate the number of pages
        int pageCount = (int) (rowCount / pageSize);
        if (rowCount > pageSize * pageCount) {
            pageCount++;
        }

        // create the page object
        final Page<E> page = new Page<E>();
        page.setPageNumber(pageNo);
        page.setPagesAvailable(pageCount);

        // fetch a single page of results
        final int startRow = (pageNo - 1) * pageSize;
        jt.query(
                sqlFetchRows,
                args,
                new ResultSetExtractor() {
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        final List pageItems = page.getPageItems();
                        int currentRow = 0;
                        while (rs.next() && currentRow < startRow + pageSize) {
                            if (currentRow >= startRow) {
                                pageItems.add(rowMapper.mapRow(rs, currentRow));
                            }
                            currentRow++;
                        }
                        return page;
                    }
                });
        return page;
    }

    /**
     * This method allows to do only one query to retrieve the data if and not query is need to retrieve the
     * count because was retrieve for the first time.
     * @param jt
     * @param rowCount
     * @param sqlFetchRows
     * @param args
     * @param pageNo
     * @param pageSize
     * @param rowMapper
     * @return
     */
    public Page<E> fetchPage(
            final JdbcTemplate jt,
            final long rowCount,
            final String sqlFetchRows,
            final Object args[],
            final int pageNo,
            final int pageSize,
            final RowMapper<E> rowMapper) {

        // determine how many rows are available
        // calculate the number of pages
        int pageCount = (int) (rowCount / pageSize);
        if (rowCount > pageSize * pageCount) {
            pageCount++;
        }

        // create the page object
        final Page<E> page = new Page<E>();
        page.setPageNumber(pageNo);
        page.setPagesAvailable(pageCount);

        // fetch a single page of results
        final int startRow = (pageNo - 1) * pageSize;
        jt.query(
                sqlFetchRows,
                args,
                new ResultSetExtractor() {
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        final List pageItems = page.getPageItems();
                        int currentRow = 0;
                        while (rs.next() && currentRow < startRow + pageSize) {
                            if (currentRow >= startRow) {
                                pageItems.add(rowMapper.mapRow(rs, currentRow));
                            }
                            currentRow++;
                        }
                        return page;
                    }
                });
        return page;
    }

}
