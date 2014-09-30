package uk.ac.ebi.pride.spectracluster.repo.dao;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import uk.ac.ebi.pride.spectracluster.repo.model.*;
import uk.ac.ebi.pride.spectracluster.repo.utils.ClusterUtils;
import uk.ac.ebi.pride.spectracluster.repo.utils.QueryUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Writer class for inserting clusters
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterWriter implements IClusterWriteDao {

    private static final Logger logger = LoggerFactory.getLogger(ClusterWriter.class);

    public static final int MAX_INCREMENT = 1000;

    private final JdbcTemplate template;
    private final DataFieldMaxValueIncrementer spectrumPrimaryKeyIncrementer;
    private final DataFieldMaxValueIncrementer psmPrimaryKeyIncrementer;

    public ClusterWriter(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.spectrumPrimaryKeyIncrementer = new OracleSequenceMaxValueIncrementer(template.getDataSource(), "spectrum_pk_sequence");
        this.psmPrimaryKeyIncrementer = new OracleSequenceMaxValueIncrementer(template.getDataSource(), "psm_pk_sequence");
    }

    @Override
    public void saveClusters(final List<ClusterSummary> clusters) {
        for (ClusterSummary cluster : clusters) {
            saveCluster(cluster);
        }
    }

    @Override
    public void saveCluster(final ClusterSummary cluster) {
        logger.debug("Insert a cluster into database: {}", cluster.toString());

        saveClusterSummary(cluster);

        updateSpectrumIdForClusteredSpectra(cluster);

        validateClusterMappings(cluster);

        ClusterUtils.updateClusteredPSMStatistics(cluster);

        saveClusteredSpectra(cluster.getClusteredSpectrumSummaries());

        saveClusteredPSMs(cluster.getClusteredPSMSummaries());

        float maxRatio = getMaxRatio(cluster.getClusteredPSMSummaries());

        updateMaxRatio(cluster, maxRatio);

    }

    private void updateMaxRatio(final ClusterSummary cluster, final float maxRatio) {
        String UPDATE_QUERY = "update spectrum_cluster set max_ratio = ? where cluster_pk=?";

        template.update(UPDATE_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setFloat(1, maxRatio);
                ps.setLong(2, cluster.getId());
            }
        });
    }

    private float getMaxRatio(List<ClusteredPSMSummary> clusteredPSMSummaries) {
        float maxRatio = 0;

        for (ClusteredPSMSummary clusteredPSMSummary : clusteredPSMSummaries) {
            float ratio = clusteredPSMSummary.getPsmRatio();
            if (ratio > maxRatio)
                maxRatio = ratio;
        }

        return maxRatio;
    }

    private void saveClusterSummary(final ClusterSummary cluster) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(template);

        simpleJdbcInsert.withTableName("spectrum_cluster").usingGeneratedKeyColumns("cluster_pk");

        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("avg_precursor_mz", cluster.getAveragePrecursorMz());
        parameters.put("avg_precursor_charge", cluster.getAveragePrecursorCharge());
        parameters.put("number_of_spectra", cluster.getNumberOfSpectra());
        parameters.put("max_ratio", 0);
        parameters.put("consensus_spectrum_mz", cluster.getConsensusSpectrumMz());
        parameters.put("consensus_spectrum_intensity", cluster.getConsensusSpectrumIntensity());

        MapSqlParameterSource parameterSource = new MapSqlParameterSource(parameters);
        parameterSource.registerSqlType("consensus_spectrum_mz", Types.CLOB);
        parameterSource.registerSqlType("consensus_spectrum_intensity", Types.CLOB);

        Number key = simpleJdbcInsert.executeAndReturnKey(parameterSource);
        long clusterId = key.longValue();
        cluster.setId(clusterId);

        // update cluster id for all the clustered spectra
        List<ClusteredSpectrumSummary> clusteredSpectrumSummaries = cluster.getClusteredSpectrumSummaries();
        for (ClusteredSpectrumSummary clusteredSpectrumSummary : clusteredSpectrumSummaries) {
            clusteredSpectrumSummary.setClusterId(clusterId);
        }
    }

    private void updateSpectrumIdForClusteredSpectra(final ClusterSummary cluster) {
        String SELECT_QUERY = "select spectrum.spectrum_pk, spectrum.spectrum_ref, psm.psm_pk, psm.sequence from spectrum " +
                "join psm on (spectrum.spectrum_pk = psm.spectrum_fk) where spectrum.spectrum_ref in ";

        List<String> queries = concatenateSpectrumReferencesForQuery(cluster.getClusteredSpectrumSummaries(), 500);
        for (final String query : queries) {
            String sql = SELECT_QUERY + "(" + query + ")";
            template.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    long spectrumPk = rs.getLong(1);
                    String spectrumRef = rs.getString(2);
                    long psmPk = rs.getLong(3);
                    String sequence = rs.getString(4);

                    ClusteredSpectrumSummary clusteredSpectrumSummary = cluster.getClusteredSpectrumSummary(spectrumRef);
                    clusteredSpectrumSummary.setSpectrumId(spectrumPk);

                    ClusteredPSMSummary clusteredPSMSummary = new ClusteredPSMSummary();
                    clusteredPSMSummary.setClusterId(cluster.getId());
                    clusteredPSMSummary.setPsmId(psmPk);
                    clusteredPSMSummary.setSpectrumId(spectrumPk);
                    clusteredPSMSummary.setSequence(sequence);
                    cluster.addClusteredPSMSummary(clusteredPSMSummary);
                }
            });
        }
    }

    private void validateClusterMappings(final ClusterSummary cluster) {
        List<String> invalidSpectrumReference = new ArrayList<String>();
        List<ClusteredSpectrumSummary> clusteredSpectrumSummaries = cluster.getClusteredSpectrumSummaries();

        for (ClusteredSpectrumSummary clusteredSpectrumSummary : clusteredSpectrumSummaries) {
            if (clusteredSpectrumSummary.getSpectrumId() == null) {
                invalidSpectrumReference.add(clusteredSpectrumSummary.getReferenceId());
            }
        }

        if (!invalidSpectrumReference.isEmpty()) {
            String message = "Spectrum references not found in database: " + StringUtils.join(invalidSpectrumReference, ",");
            logger.error(message);
            throw new IllegalStateException(message);
        }

        if (clusteredSpectrumSummaries.size() > cluster.getClusteredPSMSummaries().size()) {
            String message = "PSM not found in database for cluster that contains spectrum reference: "
                    + clusteredSpectrumSummaries.get(0).getReferenceId();
            logger.error(message);
            throw new IllegalStateException(message);
        }
    }


    private List<String> concatenateSpectrumReferencesForQuery(final List<ClusteredSpectrumSummary> clusteredSpectra, int limit) {
        List<String> queries = new ArrayList<String>();

        List<List<ClusteredSpectrumSummary>> chunks = QueryUtils.chunks(clusteredSpectra, limit);
        for (List<ClusteredSpectrumSummary> chunk : chunks) {
            String query = "";
            for (ClusteredSpectrumSummary clusteredSpectrumSummary : chunk) {
                query += "'" + clusteredSpectrumSummary.getReferenceId() + "',";
            }
            queries.add(query.substring(0, query.length() - 1));
        }

        return queries;
    }

    private void saveClusteredSpectra(final List<ClusteredSpectrumSummary> clusteredSpectra) {
        String INSERT_QUERY = "INSERT INTO cluster_has_spectrum (cluster_fk, spectrum_fk, similarity) " +
                "VALUES (?, ?, ?)";

        template.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ClusteredSpectrumSummary spectrumSummary = clusteredSpectra.get(i);
                ps.setLong(1, spectrumSummary.getClusterId());
                ps.setLong(2, spectrumSummary.getSpectrumId());
                ps.setFloat(3, spectrumSummary.getSimilarityScore());
            }

            @Override
            public int getBatchSize() {
                return clusteredSpectra.size();
            }
        });
    }

    private void saveClusteredPSMs(final List<ClusteredPSMSummary> clusteredPSMSummaries) {
        String INSERT_QUERY = "INSERT INTO cluster_has_psm (cluster_fk, psm_fk, ratio, rank) " +
                "VALUES (?, ?, ?, ?)";

        template.batchUpdate(INSERT_QUERY, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ClusteredPSMSummary clusteredPSMSummary = clusteredPSMSummaries.get(i);
                ps.setLong(1, clusteredPSMSummary.getClusterId());
                ps.setLong(2, clusteredPSMSummary.getPsmId());
                ps.setFloat(3, clusteredPSMSummary.getPsmRatio());
                ps.setInt(4, clusteredPSMSummary.getRank());
            }

            @Override
            public int getBatchSize() {
                return clusteredPSMSummaries.size();
            }
        });
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
