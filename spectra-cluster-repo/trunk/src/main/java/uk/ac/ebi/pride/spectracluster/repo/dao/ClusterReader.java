package uk.ac.ebi.pride.spectracluster.repo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import uk.ac.ebi.pride.spectracluster.repo.exception.ClusterReadException;
import uk.ac.ebi.pride.spectracluster.repo.model.*;
import uk.ac.ebi.pride.spectracluster.repo.utils.CollectionUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Reader class for reading clusters
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterReader implements IClusterReadDao {

    private static final Logger logger = LoggerFactory.getLogger(ClusterReader.class);

    private final JdbcTemplate template;
    private final TransactionTemplate transactionTemplate;

    public ClusterReader(DataSourceTransactionManager transactionManager) {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.template = new JdbcTemplate(transactionManager.getDataSource());
    }

    public List<SpectrumSummary> findSpectra(final List<String> spectrumReferences) {
        final List<SpectrumSummary> spectrumSummaries = new ArrayList<SpectrumSummary>();

        final List<String> concatenateIds = concatenateIds(spectrumReferences, 500);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    for (String concatenateId : concatenateIds) {
                        final String SELECT_QUERY = "select * from spectrum where spectrum_ref in (" + concatenateId + ")";

                        template.query(SELECT_QUERY, new RowCallbackHandler() {
                            @Override
                            public void processRow(ResultSet rs) throws SQLException {
                                SpectrumSummary spectrumSummary = new SpectrumSummary();

                                spectrumSummary.setId(rs.getLong("spectrum_pk"));
                                spectrumSummary.setReferenceId(rs.getString("spectrum_ref"));
                                spectrumSummary.setAssayId(rs.getLong("assay_fk"));
                                spectrumSummary.setPrecursorMz(rs.getFloat("precursor_mz"));
                                spectrumSummary.setPrecursorCharge(rs.getInt("precursor_charge"));
                                spectrumSummary.setIdentified(rs.getBoolean("is_identified"));

                                spectrumSummaries.add(spectrumSummary);
                            }
                        });
                    }
                } catch (Exception ex) {
                    String message = "Error while reading spectra";
                    logger.error(message);
                    throw new ClusterReadException(message, ex);
                }
            }
        });

        return spectrumSummaries;
    }

    public List<PSMSummary> findPSMBySpectrumId(final List<Long> spectrumIds) {
        final List<PSMSummary> psmSummaries = new ArrayList<PSMSummary>();

        final List<String> concatenateIds = concatenateIds(spectrumIds, 500);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    for (String concatenateId : concatenateIds) {
                        final String SELECT_QUERY = "select * from psm where spectrum_fk in (" + concatenateId + ")";

                        template.query(SELECT_QUERY, new RowCallbackHandler() {
                            @Override
                            public void processRow(ResultSet rs) throws SQLException {
                                PSMSummary psmSummary = new PSMSummary();
                                psmSummary.setId(rs.getLong("psm_pk"));
                                psmSummary.setSpectrumId(rs.getLong("spectrum_fk"));
                                psmSummary.setAssayId(rs.getLong("assay_fk"));
                                psmSummary.setArchivePSMId(rs.getString("archive_psm_id"));
                                psmSummary.setSequence(rs.getString("sequence"));
                                psmSummary.setModifications(rs.getString("modifications"));
                                psmSummary.setSearchEngine(rs.getString("search_engine"));
                                psmSummary.setSearchEngineScores(rs.getString("search_engine_scores"));
                                psmSummary.setSearchDatabase(rs.getString("search_database"));
                                psmSummary.setProteinAccession(rs.getString("protein_accession"));
                                psmSummary.setProteinGroup(rs.getString("protein_group"));
                                psmSummary.setProteinName(rs.getString("protein_name"));
                                psmSummary.setStartPosition(rs.getInt("start_position"));
                                psmSummary.setStopPosition(rs.getInt("stop_position"));
                                psmSummary.setPreAminoAcid(rs.getString("pre_amino_acid"));
                                psmSummary.setPostAminoAcid(rs.getString("post_amino_acid"));
                                psmSummary.setDeltaMZ(rs.getFloat("delta_mz"));
                                psmSummary.setQuantificationLabel(rs.getString("quantification_label"));

                                psmSummaries.add(psmSummary);
                            }
                        });
                    }
                } catch (Exception ex) {
                    String message = "Error while reading psms";
                    logger.error(message);
                    throw new ClusterReadException(message, ex);
                }
            }
        });

        return psmSummaries;
    }

    public List<AssaySummary> findAssays(final List<Long> assayIds) {
        final HashSet<Long> ids = new HashSet<Long>(assayIds);

        List<AssaySummary> assaySummaries = transactionTemplate.execute(new TransactionCallback<List<AssaySummary>>() {

            @Override
            public List<AssaySummary> doInTransaction(TransactionStatus status) {
                try {
                    return findAssaySummaries(ids);
                } catch (Exception ex) {
                    String message = "Error while counting the number of clusters";
                    logger.error(message);
                    throw new ClusterReadException(message, ex);
                }
            }
        });

        return assaySummaries;
    }

    @Override
    public long getNumberOfClusters() {
        final String QUERY = "select count(*) from spectrum_cluster";

        Long count = transactionTemplate.execute(new TransactionCallback<Long>() {

            @Override
            public Long doInTransaction(TransactionStatus status) {
                try {
                    return template.queryForObject(QUERY, Long.class);
                } catch (Exception ex) {
                    String message = "Error while counting the number of clusters";
                    logger.error(message);
                    throw new ClusterReadException(message, ex);
                }
            }
        });

        return count;
    }

    @Override
    public ClusterSummary findCluster(final Long clusterId) {

        ClusterSummary clusterSummary = transactionTemplate.execute(new TransactionCallback<ClusterSummary>() {
            @Override
            public ClusterSummary doInTransaction(TransactionStatus status) {
                try {
                    // read cluster summary
                    ClusterSummary cluster = findClusterSummary(clusterId);

                    if (cluster.getId() != null) {

                        // read spectra details
                        List<ClusteredSpectrumSummary> spectrumSummaries = findClusteredSpectrumSummaries(clusterId);
                        cluster.addClusteredSpectrumSummaries(spectrumSummaries);

                        // read psm details
                        List<ClusteredPSMSummary> psmSummaries = findClusteredPSMSummaries(clusterId);
                        cluster.addClusteredPSMSummaries(psmSummaries);

                        // collection all the unique assay ids
                        Set<Long> assayIds = new HashSet<Long>();
                        for (ClusteredPSMSummary clusteredPSMSummary : psmSummaries) {
                            assayIds.add(clusteredPSMSummary.getPsmSummary().getAssayId());
                        }

                        // read assay details
                        List<AssaySummary> assaySummaries = findAssaySummaries(assayIds);
                        cluster.addAssaySummaries(assaySummaries);
                    }

                    return cluster;

                } catch (Exception ex) {
                    String message = "Error while reading cluster: " + clusterId;
                    logger.error(message);
                    throw new ClusterReadException(message, ex);
                }
            }
        });

        return clusterSummary;
    }

    private ClusterSummary findClusterSummary(final Long clusterId) {
        final ClusterSummary cluster = new ClusterSummary();

        final String CLUSTER_QUERY = "select * from spectrum_cluster where cluster_pk=?";

        template.query(CLUSTER_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                cluster.setId(clusterId);
                cluster.setAveragePrecursorMz(rs.getFloat("avg_precursor_mz"));
                cluster.setAveragePrecursorCharge(rs.getFloat("avg_precursor_charge"));
                cluster.setConsensusSpectrumMz(rs.getString("consensus_spectrum_mz"));
                cluster.setConsensusSpectrumIntensity(rs.getString("consensus_spectrum_intensity"));
                cluster.setNumberOfSpectra(rs.getInt("number_of_spectra"));
                cluster.setMaxPeptideRatio(rs.getFloat("max_ratio"));
            }
        });

        return cluster;
    }

    private List<ClusteredSpectrumSummary> findClusteredSpectrumSummaries(final Long clusterId) {
        final ArrayList<ClusteredSpectrumSummary> clusteredSpectrumSummaries = new ArrayList<ClusteredSpectrumSummary>();

        final String SPECTRUM_QUERY = "select * from cluster_has_spectrum join spectrum on (spectrum_fk = spectrum_pk) " +
                "where cluster_fk=?";

        template.query(SPECTRUM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ClusteredSpectrumSummary clusteredSpectrumSummary = new ClusteredSpectrumSummary();
                clusteredSpectrumSummary.setClusterId(clusterId);
                clusteredSpectrumSummary.setSpectrumId(rs.getLong("spectrum_fk"));
                clusteredSpectrumSummary.setSimilarityScore(rs.getFloat("similarity"));
                String spectrumRef = rs.getString("spectrum_ref");
                clusteredSpectrumSummary.setReferenceId(spectrumRef);

                SpectrumSummary spectrumSummary = new SpectrumSummary();
                spectrumSummary.setId(rs.getLong("spectrum_pk"));
                spectrumSummary.setReferenceId(spectrumRef);
                spectrumSummary.setAssayId(rs.getLong("assay_fk"));
                spectrumSummary.setPrecursorMz(rs.getFloat("precursor_mz"));
                spectrumSummary.setPrecursorCharge(rs.getInt("precursor_charge"));
                spectrumSummary.setIdentified(rs.getBoolean("is_identified"));

                clusteredSpectrumSummary.setSpectrumSummary(spectrumSummary);
                clusteredSpectrumSummaries.add(clusteredSpectrumSummary);
            }
        });

        return clusteredSpectrumSummaries;
    }

    private List<ClusteredPSMSummary> findClusteredPSMSummaries(final Long clusterId) {
        final ArrayList<ClusteredPSMSummary> clusteredPSMSummaries = new ArrayList<ClusteredPSMSummary>();

        final String PSM_QUERY = "select * from cluster_has_psm join psm on (psm_fk = psm_pk) where cluster_fk=?";

        template.query(PSM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ClusteredPSMSummary clusteredPSMSummary = new ClusteredPSMSummary();
                clusteredPSMSummary.setClusterId(clusterId);
                clusteredPSMSummary.setPsmId(rs.getLong("psm_fk"));
                clusteredPSMSummary.setPsmRatio(rs.getFloat("ratio"));
                clusteredPSMSummary.setRank(rs.getInt("rank"));
                String sequence = rs.getString("sequence");
                clusteredPSMSummary.setSequence(sequence);

                PSMSummary psmSummary = new PSMSummary();
                psmSummary.setId(rs.getLong("psm_pk"));
                psmSummary.setSpectrumId(rs.getLong("spectrum_fk"));
                psmSummary.setAssayId(rs.getLong("assay_fk"));
                psmSummary.setArchivePSMId(rs.getString("archive_psm_id"));
                psmSummary.setSequence(rs.getString("sequence"));
                psmSummary.setModifications(rs.getString("modifications"));
                psmSummary.setSearchEngine(rs.getString("search_engine"));
                psmSummary.setSearchEngineScores(rs.getString("search_engine_scores"));
                psmSummary.setSearchDatabase(rs.getString("search_database"));
                psmSummary.setProteinAccession(rs.getString("protein_accession"));
                psmSummary.setProteinGroup(rs.getString("protein_group"));
                psmSummary.setProteinName(rs.getString("protein_name"));
                psmSummary.setStartPosition(rs.getInt("start_position"));
                psmSummary.setStopPosition(rs.getInt("stop_position"));
                psmSummary.setPreAminoAcid(rs.getString("pre_amino_acid"));
                psmSummary.setPostAminoAcid(rs.getString("post_amino_acid"));
                psmSummary.setDeltaMZ(rs.getFloat("delta_mz"));
                psmSummary.setQuantificationLabel(rs.getString("quantification_label"));
                clusteredPSMSummary.setPsmSummary(psmSummary);

                clusteredPSMSummaries.add(clusteredPSMSummary);
            }
        });

        return clusteredPSMSummaries;
    }

    private List<AssaySummary> findAssaySummaries(final Collection<Long> assayIds) {
        final List<AssaySummary> assaySummaries = new ArrayList<AssaySummary>();

        String concatenateIds = concatenateIds(assayIds);

        final String ASSAY_QUERY = "select * from assay where assay_pk in (" + concatenateIds + ")";

        template.query(ASSAY_QUERY, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                AssaySummary assaySummary = new AssaySummary();

                assaySummary.setId(rs.getLong("assay_pk"));
                assaySummary.setAccession(rs.getString("assay_accession"));
                assaySummary.setProjectAccession(rs.getString("project_accession"));
                assaySummary.setProjectTitle(rs.getString("project_title"));
                assaySummary.setAssayTitle(rs.getString("assay_title"));
                assaySummary.setSpecies(rs.getString("species"));
                assaySummary.setMultiSpecies(rs.getBoolean("multi_species"));
                assaySummary.setTaxonomyId(rs.getString("taxonomy_id"));
                assaySummary.setDisease(rs.getString("disease"));
                assaySummary.setTissue(rs.getString("tissue"));
                assaySummary.setSearchEngine(rs.getString("search_engine"));
                assaySummary.setInstrument(rs.getString("instrument"));
                assaySummary.setInstrumentType(rs.getString("instrument_type"));
                assaySummary.setBioMedical(rs.getBoolean("biomedical"));

                assaySummaries.add(assaySummary);
            }
        });

        return assaySummaries;
    }

    private String concatenateIds(final Collection ids) {
        String concatIds = "";

        for (Object id : ids) {
            concatIds += "'" + id.toString() + "',";
        }

        return concatIds.substring(0, concatIds.length() - 1);
    }

    private List<String> concatenateIds(final List ids, int limit) {
        List<String> queries = new ArrayList<String>();

        List<List> chunks = CollectionUtils.chunks(ids, limit);
        for (List chunk : chunks) {
            String query = "";
            for (Object id : chunk) {
                query += "'" + id.toString() + "',";
            }
            queries.add(query.substring(0, query.length() - 1));
        }

        return queries;
    }
}
