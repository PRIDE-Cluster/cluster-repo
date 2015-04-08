package uk.ac.ebi.pride.spectracluster.repo.dao.stats;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusterRepoStatistics;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader for accessing statistics of the cluster repository
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterRepoStatisticsReader implements IClusterRepoStatisticsReadDao {

    private final JdbcTemplate template;

    public ClusterRepoStatisticsReader(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<ClusterRepoStatistics> getGeneralStatistics() {
        final List<ClusterRepoStatistics> clusterRepoStatistics = new ArrayList<ClusterRepoStatistics>();


        final String SELECT_QUERY = "SELECT * FROM cluster_statistics WHERE name NOT LIKE '%-%'";

        template.query(SELECT_QUERY, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ClusterRepoStatistics statistics = new ClusterRepoStatistics();

                statistics.setName(rs.getString("name"));
                statistics.setValue(rs.getLong("value"));

                clusterRepoStatistics.add(statistics);
            }
        });

        return clusterRepoStatistics;
    }
}
