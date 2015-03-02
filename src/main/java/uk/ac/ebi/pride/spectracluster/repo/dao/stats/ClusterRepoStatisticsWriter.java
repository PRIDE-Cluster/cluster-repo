package uk.ac.ebi.pride.spectracluster.repo.dao.stats;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusterRepoStatistics;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Writer for cluster repository statistics
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterRepoStatisticsWriter implements IClusterRepoStatisticsWriteDao {

    private final static Logger logger = LoggerFactory.getLogger(ClusterRepoStatisticsWriter.class);

    private final JdbcTemplate template;

    public ClusterRepoStatisticsWriter(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveStatistics(final List<ClusterRepoStatistics> statistics) {
        logger.debug("Insert a list of statistics into database: {}", statistics.size());

        if (statistics.size() == 0)
            return;

        String INSERT_QUERY = "INSERT INTO cluster_statistics (name, value) VALUES (?, ?)";

        template.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ClusterRepoStatistics stat = statistics.get(i);
                ps.setString(1, stat.getName());
                ps.setString(2, stat.getValue());
            }

            @Override
            public int getBatchSize() {
                return statistics.size();
            }
        });
    }
}
