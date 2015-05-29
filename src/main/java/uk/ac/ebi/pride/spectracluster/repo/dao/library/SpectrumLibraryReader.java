package uk.ac.ebi.pride.spectracluster.repo.dao.library;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import uk.ac.ebi.pride.spectracluster.repo.model.SpectrumLibraryDetail;

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
        final List<SpectrumLibraryDetail> spectrumLibraryDetails = new ArrayList<SpectrumLibraryDetail>();

        final String latestSpectralLibraryVersion = getLatestSpectrumLibraryVersion();

        final String QUERY = "SELECT * FROM spectral_library WHERE release_version = ?";

        template.query(QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, latestSpectralLibraryVersion);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                SpectrumLibraryDetail spectrumLibraryDetail = new SpectrumLibraryDetail();

                spectrumLibraryDetail.setVersion(rs.getString("release_version"));
                spectrumLibraryDetail.setReleaseDate(rs.getDate("release_date"));
                spectrumLibraryDetail.setTaxonomyId(rs.getLong("taxonomy_id"));
                spectrumLibraryDetail.setSpeciesName(rs.getString("species_name"));
                spectrumLibraryDetail.setSpeciesScientificName(rs.getString("species_scientific_name"));
                spectrumLibraryDetail.setNumberOfSpectra(rs.getLong("number_of_spectra"));
                spectrumLibraryDetail.setNumberOfPeptides(rs.getLong("number_of_peptides"));
                spectrumLibraryDetail.setFileSize(rs.getLong("file_size"));
                spectrumLibraryDetail.setFileName(rs.getString("file_name"));

                spectrumLibraryDetails.add(spectrumLibraryDetail);
            }
        });

        return spectrumLibraryDetails;
    }

    @Override
    public List<SpectrumLibraryDetail> getAllSpectrumLibraries() {
        final List<SpectrumLibraryDetail> spectrumLibraryDetails = new ArrayList<SpectrumLibraryDetail>();

        final String QUERY = "SELECT * FROM spectral_library";

        template.query(QUERY, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                SpectrumLibraryDetail spectrumLibraryDetail = new SpectrumLibraryDetail();

                spectrumLibraryDetail.setVersion(rs.getString("release_version"));
                spectrumLibraryDetail.setReleaseDate(rs.getDate("release_date"));
                spectrumLibraryDetail.setTaxonomyId(rs.getLong("taxonomy_id"));
                spectrumLibraryDetail.setSpeciesName(rs.getString("species_name"));
                spectrumLibraryDetail.setSpeciesScientificName(rs.getString("species_scientific_name"));
                spectrumLibraryDetail.setNumberOfSpectra(rs.getLong("number_of_spectra"));
                spectrumLibraryDetail.setNumberOfPeptides(rs.getLong("number_of_peptides"));
                spectrumLibraryDetail.setFileSize(rs.getLong("file_size"));
                spectrumLibraryDetail.setFileName(rs.getString("file_name"));

                spectrumLibraryDetails.add(spectrumLibraryDetail);
            }
        });

        return spectrumLibraryDetails;
    }
}
