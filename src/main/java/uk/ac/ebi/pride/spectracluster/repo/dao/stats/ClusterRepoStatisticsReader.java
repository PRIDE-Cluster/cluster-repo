package uk.ac.ebi.pride.spectracluster.repo.dao.stats;

import org.springframework.jdbc.core.JdbcTemplate;
import uk.ac.ebi.pride.spectracluster.repo.dao.utils.RowMapperFactory;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusterRepoStatistics;

import javax.sql.DataSource;
import java.util.List;

/**
 * Reader for accessing statistics of the cluster repository
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterRepoStatisticsReader implements IClusterRepoStatisticsReadDao {

    private static final String FIELD_SEPARATOR = "|";

    private final JdbcTemplate template;

    public ClusterRepoStatisticsReader(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<ClusterRepoStatistics> getGeneralStatistics() {

        final String SELECT_QUERY = "SELECT * FROM cluster_statistics WHERE name NOT LIKE '%" + FIELD_SEPARATOR + "%'";

        return template.query(SELECT_QUERY, RowMapperFactory.getClusterRepoStatisticsRowMapper());
    }

    @Override
    public List<ClusterRepoStatistics> getStatisticsByPrefix(String prefix) {

        final String SELECT_QUERY = "SELECT * FROM cluster_statistics WHERE name LIKE '" + prefix + "%" + FIELD_SEPARATOR+ "%'";

        return template.query(SELECT_QUERY, RowMapperFactory.getClusterRepoStatisticsRowMapper());
    }
}
