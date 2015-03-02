package uk.ac.ebi.pride.spectracluster.repo.dao.library;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import uk.ac.ebi.pride.spectracluster.repo.model.SpectralLibraryDetail;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader for getting spectral library related details
 *
 * @author Rui Wang
 * @version $Id$
 */
public class SpectralLibraryReader implements ISpectralLibraryReadDao {

    private final JdbcTemplate template;

    public SpectralLibraryReader(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public String getLatestSpectralLibraryVersion() {
        final String QUERY = "SELECT DISTINCT release_version FROM spectral_library WHERE release_date = (SELECT MAX(release_date) FROM spectral_library)";
        return template.queryForObject(QUERY, String.class);
    }

    @Override
    public List<SpectralLibraryDetail> getLatestSpectralLibraries() {
        final List<SpectralLibraryDetail> spectralLibraryDetails = new ArrayList<SpectralLibraryDetail>();

        final String latestSpectralLibraryVersion = getLatestSpectralLibraryVersion();

        final String QUERY = "SELECT * FROM spectral_library WHERE release_version = ?";

        template.query(QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, latestSpectralLibraryVersion);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                SpectralLibraryDetail spectralLibraryDetail = new SpectralLibraryDetail();

                spectralLibraryDetail.setVersion(rs.getString("release_version"));
                spectralLibraryDetail.setReleaseDate(rs.getDate("release_date"));
                spectralLibraryDetail.setTaxonomyId(rs.getLong("taxonomy_id"));
                spectralLibraryDetail.setSpeciesName(rs.getString("species_name"));
                spectralLibraryDetail.setSpeciesScientificName(rs.getString("species_scientific_name"));
                spectralLibraryDetail.setNumberOfSpectra(rs.getLong("number_of_spectra"));
                spectralLibraryDetail.setFileSize(rs.getLong("file_size"));
                spectralLibraryDetail.setFileName(rs.getString("file_name"));

                spectralLibraryDetails.add(spectralLibraryDetail);
            }
        });

        return spectralLibraryDetails;
    }

    @Override
    public List<SpectralLibraryDetail> getAllSpectralLibraries() {
        final List<SpectralLibraryDetail> spectralLibraryDetails = new ArrayList<SpectralLibraryDetail>();

        final String QUERY = "SELECT * FROM spectral_library";

        template.query(QUERY, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                SpectralLibraryDetail spectralLibraryDetail = new SpectralLibraryDetail();

                spectralLibraryDetail.setVersion(rs.getString("release_version"));
                spectralLibraryDetail.setReleaseDate(rs.getDate("release_date"));
                spectralLibraryDetail.setTaxonomyId(rs.getLong("taxonomy_id"));
                spectralLibraryDetail.setSpeciesName(rs.getString("species_name"));
                spectralLibraryDetail.setSpeciesScientificName(rs.getString("species_scientific_name"));
                spectralLibraryDetail.setNumberOfSpectra(rs.getLong("number_of_spectra"));
                spectralLibraryDetail.setFileSize(rs.getLong("file_size"));
                spectralLibraryDetail.setFileName(rs.getString("file_name"));

                spectralLibraryDetails.add(spectralLibraryDetail);
            }
        });

        return spectralLibraryDetails;
    }
}
