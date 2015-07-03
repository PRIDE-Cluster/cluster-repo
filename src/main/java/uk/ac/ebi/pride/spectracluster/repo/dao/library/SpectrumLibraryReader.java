package uk.ac.ebi.pride.spectracluster.repo.dao.library;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import uk.ac.ebi.pride.spectracluster.repo.dao.utils.RowMapperFactory;
import uk.ac.ebi.pride.spectracluster.repo.model.SpectrumLibraryDetail;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Reader for getting spectral library related details
 *
 * @author Rui Wang
 * @version $Id$
 */
public class SpectrumLibraryReader implements ISpectrumLibraryReadDao {

    private final JdbcTemplate template;

    public SpectrumLibraryReader(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public String getLatestSpectrumLibraryVersion() {
        final String QUERY = "SELECT DISTINCT release_version FROM spectral_library WHERE release_date = (SELECT MAX(release_date) FROM spectral_library)";

        return template.queryForObject(QUERY, String.class);
    }

    @Override
    public List<SpectrumLibraryDetail> getLatestSpectrumLibraries() {
        final String latestSpectralLibraryVersion = getLatestSpectrumLibraryVersion();

        final String QUERY = "SELECT * FROM spectral_library WHERE release_version = ?";

        return template.query(QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, latestSpectralLibraryVersion);
            }
        }, RowMapperFactory.getSpectrumLibraryDetailRowMapper());
    }

    @Override
    public List<SpectrumLibraryDetail> getAllSpectrumLibraries() {
        final String QUERY = "SELECT * FROM spectral_library";

        return template.query(QUERY, RowMapperFactory.getSpectrumLibraryDetailRowMapper());
    }
}
