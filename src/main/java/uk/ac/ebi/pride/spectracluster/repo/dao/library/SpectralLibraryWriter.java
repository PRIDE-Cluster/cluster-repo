package uk.ac.ebi.pride.spectracluster.repo.dao.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import uk.ac.ebi.pride.spectracluster.repo.model.SpectralLibraryDetail;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Writer implementation for inserting spectral library details
 *
 * @author Rui Wang
 * @version $Id$
 */
public class SpectralLibraryWriter implements ISpectralLibraryWriteDao {

    private static final Logger logger = LoggerFactory.getLogger(SpectralLibraryWriter.class);

    private final JdbcTemplate template;

    public SpectralLibraryWriter(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveSpectralLibraries(List<SpectralLibraryDetail> spectralLibraryDetails) {
        for (SpectralLibraryDetail spectralLibraryDetail : spectralLibraryDetails) {
            saveSpectralLibrary(spectralLibraryDetail);
        }
    }

    @Override
    public void saveSpectralLibrary(SpectralLibraryDetail spectralLibraryDetail) {
        logger.debug("Inserting a spectral library detail record");

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(template);

        simpleJdbcInsert.withTableName("spectral_library");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("version", spectralLibraryDetail.getVersion());
        parameters.put("release_date", spectralLibraryDetail.getReleaseDate());
        parameters.put("taxonomy_id", spectralLibraryDetail.getTaxonomyId());
        parameters.put("species_scientific_name", spectralLibraryDetail.getSpeciesScientificName());
        parameters.put("species_name", spectralLibraryDetail.getSpeciesName());
        parameters.put("number_of_spectra", spectralLibraryDetail.getNumberOfSpectra());
        parameters.put("file_size", spectralLibraryDetail.getFileSize());
        parameters.put("file_name", spectralLibraryDetail.getFileName());

        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        parameterSource.registerSqlType("release_date", Types.DATE);

        simpleJdbcInsert.execute(parameterSource);
    }
}
