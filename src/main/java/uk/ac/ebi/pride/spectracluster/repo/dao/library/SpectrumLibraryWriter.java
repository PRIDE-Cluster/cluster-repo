package uk.ac.ebi.pride.spectracluster.repo.dao.library;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import uk.ac.ebi.pride.spectracluster.repo.model.SpectrumLibraryDetail;

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
public class SpectrumLibraryWriter implements ISpectrumLibraryWriteDao {

    private static final Logger logger = LoggerFactory.getLogger(SpectrumLibraryWriter.class);

    private final JdbcTemplate template;

    public SpectrumLibraryWriter(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public void saveSpectrumLibraries(List<SpectrumLibraryDetail> spectrumLibraryDetails) {
        for (SpectrumLibraryDetail spectrumLibraryDetail : spectrumLibraryDetails) {
            saveSpectrumLibrary(spectrumLibraryDetail);
        }
    }

    @Override
    public void saveSpectrumLibrary(SpectrumLibraryDetail spectrumLibraryDetail) {
        logger.debug("Inserting a spectral library detail record");

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(template);

        simpleJdbcInsert.withTableName("spectral_library");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("release_version", spectrumLibraryDetail.getVersion());
        parameters.put("release_date", spectrumLibraryDetail.getReleaseDate());
        parameters.put("taxonomy_id", spectrumLibraryDetail.getTaxonomyId());
        parameters.put("species_scientific_name", spectrumLibraryDetail.getSpeciesScientificName());
        parameters.put("species_name", spectrumLibraryDetail.getSpeciesName());
        parameters.put("number_of_spectra", spectrumLibraryDetail.getNumberOfSpectra());
        parameters.put("number_of_peptides", spectrumLibraryDetail.getNumberOfPeptides());
        parameters.put("file_size", spectrumLibraryDetail.getFileSize());
        parameters.put("file_name", spectrumLibraryDetail.getFileName());

        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        parameterSource.registerSqlType("release_date", Types.DATE);

        simpleJdbcInsert.execute(parameterSource);
    }
}
