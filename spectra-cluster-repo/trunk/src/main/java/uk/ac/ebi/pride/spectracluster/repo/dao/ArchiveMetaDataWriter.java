package uk.ac.ebi.pride.spectracluster.repo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import uk.ac.ebi.pride.spectracluster.repo.model.AssaySummary;
import uk.ac.ebi.pride.spectracluster.repo.model.PSMSummary;
import uk.ac.ebi.pride.spectracluster.repo.model.SpectrumSummary;
import uk.ac.ebi.pride.spectracluster.repo.utils.QueryUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 * Writer class for inserting metadata from PRIDE Archive
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ArchiveMetaDataWriter implements IArchiveMetaDataWriteDao {

    private static final Logger logger = LoggerFactory.getLogger(ArchiveMetaDataWriter.class);

    public static final int MAX_INCREMENT = 1000;

    private final JdbcTemplate template;
    private final DataFieldMaxValueIncrementer spectrumPrimaryKeyIncrementer;
    private final DataFieldMaxValueIncrementer psmPrimaryKeyIncrementer;

    public ArchiveMetaDataWriter(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.spectrumPrimaryKeyIncrementer = new OracleSequenceMaxValueIncrementer(template.getDataSource(), "spectrum_pk_sequence");
        this.psmPrimaryKeyIncrementer = new OracleSequenceMaxValueIncrementer(template.getDataSource(), "psm_pk_sequence");
    }

    @Override
    public void saveAssay(final AssaySummary assay) {
        logger.debug("Insert assay summary into database: {}", assay.getAccession());


        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(template);

        simpleJdbcInsert.withTableName("assay").usingGeneratedKeyColumns("assay_pk");

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("assay_accession", assay.getAccession());
        parameters.put("project_accession", assay.getProjectAccession());

        if (assay.getProjectTitle() != null)
            parameters.put("project_title", assay.getProjectTitle());

        if (assay.getAssayTitle() != null)
            parameters.put("assay_title", assay.getAssayTitle());

        parameters.put("species", assay.getSpecies());
        parameters.put("multi_species", assay.isMultiSpecies());

        if (assay.getTaxonomyId() != null)
            parameters.put("taxonomy_id", assay.getTaxonomyId());

        if (assay.getDisease() != null)
            parameters.put("disease", assay.getDisease());

        if (assay.getTissue() != null)
            parameters.put("tissue", assay.getTissue());

        if (assay.getSearchEngine() != null)
            parameters.put("search_engine", assay.getSearchEngine());

        if (assay.getInstrument() != null)
            parameters.put("instrument", assay.getInstrument());

        parameters.put("biomedical", assay.isBioMedical());

        Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        assay.setId(key.longValue());

    }

    @Override
    public void deleteAssayByProjectAccession(final String projectAccession) {
        logger.debug("Delete project from database: {}", projectAccession);

        String UPDATE_QUERY = "DELETE FROM assay WHERE project_accession = ?";

        template.update(UPDATE_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, projectAccession);
            }
        });
    }

    @Override
    public void saveSpectra(final List<SpectrumSummary> spectra) {
        logger.debug("Insert a list of spectra into database: {}", spectra.size());

        if (spectra.size() == 0)
            return;

        List<List<SpectrumSummary>> chunks = QueryUtils.chunks(spectra, MAX_INCREMENT);

        for (List<SpectrumSummary> chunk : chunks) {
            saveBatchOfSpectra(chunk);
        }
    }

    private void saveBatchOfSpectra(final List<SpectrumSummary> spectra) {
        if (spectra.size() > MAX_INCREMENT)
            throw new IllegalStateException("The number of spectra cannot excceed: " + MAX_INCREMENT);


        final long startingKey = spectrumPrimaryKeyIncrementer.nextLongValue();

        // add primary key
        int count = 0;
        for (SpectrumSummary spectrum : spectra) {
            spectrum.setId(startingKey + count);
            count++;
        }

        saveSpectraWithPrimaryKey(spectra);
    }

    private void saveSpectraWithPrimaryKey(final List<SpectrumSummary> spectra) {
        String INSERT_QUERY = "INSERT INTO spectrum (spectrum_pk, spectrum_ref, assay_fk, " +
                "precursor_mz, precursor_charge, is_identified) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        template.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SpectrumSummary spectrum = spectra.get(i);
                ps.setLong(1, spectrum.getId());
                ps.setString(2, spectrum.getReferenceId());
                ps.setLong(3, spectrum.getAssayId());
                ps.setFloat(4, spectrum.getPrecursorMz());
                ps.setInt(5, spectrum.getPrecursorCharge());
                ps.setBoolean(6, spectrum.isIdentified());
            }

            @Override
            public int getBatchSize() {
                return spectra.size();
            }
        });
    }

    @Override
    public void saveSpectrum(final SpectrumSummary spectrum) {
        logger.debug("Insert a spectrum into database: {}", spectrum.getReferenceId());

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(template);

        simpleJdbcInsert.withTableName("spectrum").usingGeneratedKeyColumns("spectrum_pk");

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("assay_fk", spectrum.getAssayId());
        parameters.put("spectrum_ref", spectrum.getReferenceId());
        parameters.put("precursor_mz", spectrum.getPrecursorMz());
        parameters.put("precursor_charge", spectrum.getPrecursorCharge());
        parameters.put("is_identified", spectrum.isIdentified());

        Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        spectrum.setId(key.longValue());
    }

    @Override
    public void savePSMs(final List<PSMSummary> psms) {
        logger.debug("Insert a list of PSMs into database: {}", psms.size());

        if (psms.size() == 0)
            return;

        List<List<PSMSummary>> chunks = QueryUtils.chunks(psms, MAX_INCREMENT);
        for (List<PSMSummary> chunk : chunks) {
            saveBatchOfPSMs(chunk);
        }
    }

    private void saveBatchOfPSMs(final List<PSMSummary> psms) {
        if (psms.size() > MAX_INCREMENT)
            throw new IllegalStateException("The number of spectra cannot exceed: " + MAX_INCREMENT);


        final long startingKey = psmPrimaryKeyIncrementer.nextLongValue();

        // add primary key
        int count = 0;
        for (PSMSummary psm : psms) {
            psm.setId(startingKey + count);
            count++;
        }

        savePSMsWithPrimaryKey(psms);
    }

    private void savePSMsWithPrimaryKey(final List<PSMSummary> psms) {
        String INSERT_QUERY = "INSERT INTO psm (psm_pk, spectrum_fk, assay_fk, archive_psm_id, sequence, modifications, search_engine, " +
                "search_engine_scores, search_database, protein_accession, protein_group, protein_name, start_position, " +
                "stop_position, pre_amino_acid, post_amino_acid, delta_mz, quantification_label) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        template.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PSMSummary psm = psms.get(i);
                ps.setLong(1, psm.getId());
                ps.setLong(2, psm.getSpectrumId());
                ps.setLong(3, psm.getAssayId());
                ps.setString(4, psm.getArchivePSMId());
                ps.setString(5, psm.getSequence());
                ps.setString(6, psm.getModifications());
                ps.setString(7, psm.getSearchEngine());
                ps.setString(8, psm.getSearchEngineScores());
                ps.setString(9, psm.getSearchDatabase());
                ps.setString(10, psm.getProteinAccession());
                ps.setString(11, psm.getProteinGroup());
                ps.setString(12, psm.getProteinName());
                ps.setInt(13, psm.getStartPosition());
                ps.setInt(14, psm.getStopPosition());
                ps.setString(15, psm.getPreAminoAcid());
                ps.setString(16, psm.getPostAminoAcid());
                ps.setFloat(17, psm.getDeltaMZ());
                ps.setString(18, psm.getQuantificationLabel());
            }

            @Override
            public int getBatchSize() {
                return psms.size();
            }
        });
    }


    @Override
    public void savePSM(final PSMSummary psm) {
        logger.debug("Insert a PSM into database: {}", psm.getArchivePSMId());


        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(template);

        simpleJdbcInsert.withTableName("psm").usingGeneratedKeyColumns("psm_pk");

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("spectrum_fk", psm.getSpectrumId());
        parameters.put("assay_fk", psm.getAssayId());
        parameters.put("archive_psm_id", psm.getArchivePSMId());
        parameters.put("sequence", psm.getSequence());

        if (psm.getModifications() != null)
            parameters.put("modifications", psm.getModifications());

        if (psm.getSearchEngine() != null)
            parameters.put("search_engine", psm.getSearchEngine());

        if (psm.getSearchEngineScores() != null)
            parameters.put("search_engine_scores", psm.getSearchEngineScores());

        if (psm.getSearchDatabase() != null)
            parameters.put("search_database", psm.getSearchDatabase());

        if (psm.getProteinAccession() != null)
            parameters.put("protein_accession", psm.getProteinAccession());

        if (psm.getProteinGroup() != null)
            parameters.put("protein_group", psm.getProteinGroup());

        if (psm.getProteinName() != null)
            parameters.put("protein_name", psm.getProteinName());

        parameters.put("start_position", psm.getStartPosition());
        parameters.put("stop_position", psm.getStopPosition());

        if (psm.getPreAminoAcid() != null)
            parameters.put("pre_amino_acid", psm.getPreAminoAcid());

        if (psm.getPostAminoAcid() != null)
            parameters.put("post_amino_acid", psm.getPostAminoAcid());

        parameters.put("delta_mz", psm.getDeltaMZ());

        if (psm.getQuantificationLabel() != null)
            parameters.put("quantification_label", psm.getQuantificationLabel());

        Number key = simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        psm.setId(key.longValue());

    }


}
