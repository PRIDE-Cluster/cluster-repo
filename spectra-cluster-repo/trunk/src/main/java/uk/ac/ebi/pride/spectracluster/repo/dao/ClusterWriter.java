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
import uk.ac.ebi.pride.spectracluster.repo.model.ClusterSummary;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusteredPSMSummary;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusteredSpectrumSummary;
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

    private final JdbcTemplate template;

    public ClusterWriter(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
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
}
