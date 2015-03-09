package uk.ac.ebi.pride.spectracluster.repo.dao.cluster;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import uk.ac.ebi.pride.spectracluster.repo.model.*;
import uk.ac.ebi.pride.spectracluster.repo.utils.QueryUtils;
import uk.ac.ebi.pride.spectracluster.repo.utils.paging.Page;
import uk.ac.ebi.pride.spectracluster.repo.utils.paging.PaginationHelper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader class for reading clusters
 *
 * @author Rui Wang
 * @author Jose A Dianes
 * @version $Id$
 */
public class ClusterReader implements IClusterReadDao {

    private final JdbcTemplate template;

    public ClusterReader(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<SpectrumDetail> findSpectra(final List<String> spectrumReferences) {
        final List<SpectrumDetail> spectrumSummaries = new ArrayList<SpectrumDetail>();

        final List<String> concatenateIds = QueryUtils.concatenateIds(spectrumReferences, 500);

        for (String concatenateId : concatenateIds) {
            final String SELECT_QUERY = "SELECT * FROM spectrum WHERE spectrum_ref IN (" + concatenateId + ")";

            template.query(SELECT_QUERY, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    SpectrumDetail spectrumDetail = new SpectrumDetail();

                    spectrumDetail.setId(rs.getLong("spectrum_pk"));
                    spectrumDetail.setReferenceId(rs.getString("spectrum_ref"));
                    spectrumDetail.setAssayId(rs.getLong("assay_fk"));
                    spectrumDetail.setPrecursorMz(rs.getFloat("precursor_mz"));
                    spectrumDetail.setPrecursorCharge(rs.getInt("precursor_charge"));
                    spectrumDetail.setIdentified(rs.getBoolean("is_identified"));

                    spectrumSummaries.add(spectrumDetail);
                }
            });
        }

        return spectrumSummaries;
    }

    @Override
    public List<PSMDetail> findPSMBySpectrumId(final List<Long> spectrumIds) {
        final List<PSMDetail> psmSummaries = new ArrayList<PSMDetail>();

        final List<String> concatenateIds = QueryUtils.concatenateIds(spectrumIds, 500);

        for (String concatenateId : concatenateIds) {
            final String SELECT_QUERY = "SELECT * FROM psm WHERE spectrum_fk IN (" + concatenateId + ")";

            template.query(SELECT_QUERY, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    PSMDetail psmDetail = new PSMDetail();
                    psmDetail.setId(rs.getLong("psm_pk"));
                    psmDetail.setSpectrumId(rs.getLong("spectrum_fk"));
                    psmDetail.setAssayId(rs.getLong("assay_fk"));
                    psmDetail.setArchivePSMId(rs.getString("archive_psm_id"));
                    psmDetail.setSequence(rs.getString("sequence"));
                    psmDetail.setModifications(rs.getString("modifications"));
                    psmDetail.setSearchEngine(rs.getString("search_engine"));
                    psmDetail.setSearchEngineScores(rs.getString("search_engine_scores"));
                    psmDetail.setSearchDatabase(rs.getString("search_database"));
                    psmDetail.setProteinAccession(rs.getString("protein_accession"));
                    psmDetail.setProteinGroup(rs.getString("protein_group"));
                    psmDetail.setProteinName(rs.getString("protein_name"));
                    psmDetail.setStartPosition(rs.getInt("start_position"));
                    psmDetail.setStopPosition(rs.getInt("stop_position"));
                    psmDetail.setPreAminoAcid(rs.getString("pre_amino_acid"));
                    psmDetail.setPostAminoAcid(rs.getString("post_amino_acid"));
                    psmDetail.setDeltaMZ(rs.getFloat("delta_mz"));
                    psmDetail.setQuantificationLabel(rs.getString("quantification_label"));

                    psmSummaries.add(psmDetail);
                }
            });
        }

        return psmSummaries;
    }

    @Override
    public List<AssayDetail> findAssays(final List<Long> assayIds) {

        final List<AssayDetail> assaySummaries = new ArrayList<AssayDetail>();

        List<String> concatenateIds = QueryUtils.concatenateIds(assayIds, 500);

        for (String concatenateId : concatenateIds) {
            final String ASSAY_QUERY = "SELECT * FROM assay WHERE assay_pk IN (" + concatenateId + ")";

            template.query(ASSAY_QUERY, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet rs) throws SQLException {
                    AssayDetail assayDetail = new AssayDetail();

                    assayDetail.setId(rs.getLong("assay_pk"));
                    assayDetail.setAccession(rs.getString("assay_accession"));
                    assayDetail.setProjectAccession(rs.getString("project_accession"));
                    assayDetail.setProjectTitle(rs.getString("project_title"));
                    assayDetail.setAssayTitle(rs.getString("assay_title"));
                    assayDetail.setSpecies(rs.getString("species"));
                    assayDetail.setMultiSpecies(rs.getBoolean("multi_species"));
                    assayDetail.setTaxonomyId(rs.getString("taxonomy_id"));
                    assayDetail.setDisease(rs.getString("disease"));
                    assayDetail.setTissue(rs.getString("tissue"));
                    assayDetail.setSearchEngine(rs.getString("search_engine"));
                    assayDetail.setInstrument(rs.getString("instrument"));
                    assayDetail.setInstrumentType(rs.getString("instrument_type"));
                    assayDetail.setBioMedical(rs.getBoolean("biomedical"));

                    assaySummaries.add(assayDetail);
                }
            });
        }

        return assaySummaries;
    }

    @Override
    public long getNumberOfClusters() {
        final String QUERY = "SELECT count(*) FROM spectrum_cluster";

        return template.queryForObject(QUERY, Long.class);
    }

    @Override
    public Page<Long> getAllClusterIds(int pageNo, int pageSize) {
        final String CLUSTER_QUERY_COUNT = "SELECT count(*) FROM spectrum_cluster";
        final String CLUSTER_QUERY = "SELECT cluster_pk FROM spectrum_cluster";

        return getPaginatedClusterIds(pageNo, pageSize, CLUSTER_QUERY_COUNT, CLUSTER_QUERY);
    }

    @Override
    public Page<Long> getAllClusterIdsByQuality(int pageNo, int pageSize, ClusterQuality lowestClusterQuality) {
        final String CLUSTER_QUERY_COUNT = "SELECT COUNT(*) FROM spectrum_cluster WHERE quality >= " + lowestClusterQuality.getQualityLevel();
        final String CLUSTER_QUERY = "SELECT cluster_pk FROM spectrum_cluster WHERE quality >= " + lowestClusterQuality.getQualityLevel();

        return getPaginatedClusterIds(pageNo, pageSize, CLUSTER_QUERY_COUNT, CLUSTER_QUERY);
    }

    private Page<Long> getPaginatedClusterIds(int pageNo, int pageSize, String countQuery, String clusterQuery) {
        PaginationHelper<Long> ph = new PaginationHelper<Long>();

        return ph.fetchPage(
                template,
                countQuery,
                clusterQuery,
                new Object[]{},
                pageNo,
                pageSize,
                new ParameterizedRowMapper<Long>() {
                    public Long mapRow(ResultSet rs, int i) throws SQLException {
                        return rs.getLong("cluster_pk");
                    }
                }
        );
    }

    @Override
    public Page<ClusterSummary> getAllClusterSummaries(int pageNo, int pageSize) {

        final String CLUSTER_QUERY_COUNT = "SELECT count(*) FROM spectrum_cluster";
        final String CLUSTER_QUERY = "SELECT * FROM spectrum_cluster";

        return getPaginatedClusterSummaries(pageNo, pageSize, CLUSTER_QUERY_COUNT, CLUSTER_QUERY);
    }

    @Override
    public Page<ClusterSummary> getAllClusterSummariesByQuality(int pageNo, int pageSize, ClusterQuality lowestClusterQuality) {
        final String CLUSTER_QUERY_COUNT = "SELECT count(*) FROM spectrum_cluster WHERE quality >= " + lowestClusterQuality.getQualityLevel();
        final String CLUSTER_QUERY = "SELECT * FROM spectrum_cluster WHERE quality >= " + lowestClusterQuality.getQualityLevel();

        return getPaginatedClusterSummaries(pageNo, pageSize, CLUSTER_QUERY_COUNT, CLUSTER_QUERY);
    }

    private Page<ClusterSummary> getPaginatedClusterSummaries(int pageNo, int pageSize, String countQuery, String clusterQuery) {

        PaginationHelper<ClusterSummary> ph = new PaginationHelper<ClusterSummary>();

        return ph.fetchPage(
                template,
                countQuery,
                clusterQuery,
                new Object[]{},
                pageNo,
                pageSize,
                new ParameterizedRowMapper<ClusterSummary>() {
                    public ClusterSummary mapRow(ResultSet rs, int i) throws SQLException {
                        ClusterSummary cluster = new ClusterSummary();

                        cluster.setId(rs.getLong("cluster_pk"));
                        cluster.setUUID(rs.getString("uuid"));
                        cluster.setAveragePrecursorMz(rs.getFloat("avg_precursor_mz"));
                        cluster.setAveragePrecursorCharge(rs.getInt("avg_precursor_charge"));
                        cluster.setConsensusSpectrumMz(rs.getString("consensus_spectrum_mz"));
                        cluster.setConsensusSpectrumIntensity(rs.getString("consensus_spectrum_intensity"));
                        cluster.setNumberOfSpectra(rs.getInt("number_of_spectra"));
                        cluster.setTotalNumberOfSpectra(rs.getInt("total_number_of_spectra"));
                        cluster.setMaxPeptideRatio(rs.getFloat("max_ratio"));
                        cluster.setNumberOfProjects(rs.getInt("number_of_projects"));
                        cluster.setTotalNumberOfProjects(rs.getInt("total_number_of_projects"));
                        cluster.setNumberOfSpecies(rs.getInt("number_of_species"));
                        cluster.setTotalNumberOfSpecies(rs.getInt("total_number_of_species"));
                        cluster.setNumberOfModifications(rs.getInt("number_of_modifications"));
                        cluster.setTotalNumberOfModifications(rs.getInt("total_number_of_modifications"));
                        ClusterQuality quality = ClusterQuality.getClusterQuality(rs.getInt("quality"));
                        cluster.setQuality(quality);
                        cluster.setAnnotation(rs.getString("annotation"));

                        return cluster;
                    }
                }
        );

    }

    @Override
    public ClusterDetail findCluster(final Long clusterId) {
        // read cluster summary
        ClusterSummary cluster = findClusterSummary(clusterId);
        ClusterDetail clusterDetail = new ClusterDetail(cluster);

        if (cluster.getId() != null) {

            // read spectra details
            List<ClusteredSpectrumDetail> spectrumSummaries = findClusteredSpectrumSummaryByClusterId(clusterId);
            clusterDetail.addClusteredSpectrumDetails(spectrumSummaries);

            // read psm details
            List<ClusteredPSMDetail> psmSummaries = findClusteredPSMSummaryByClusterId(clusterId);
            clusterDetail.addClusteredPSMDetails(psmSummaries);

            // collection all the unique assay ids
            List<Long> assayIds = new ArrayList<Long>();
            for (ClusteredPSMDetail clusteredPSMDetail : psmSummaries) {
                assayIds.add(clusteredPSMDetail.getPsmDetail().getAssayId());
            }

            // read assay details
            List<AssayDetail> assaySummaries = findAssays(assayIds);
            clusterDetail.addAssayDetails(assaySummaries);
        }

        return clusterDetail;
    }

    @Override
    public ClusterSummary findClusterSummary(final Long clusterId) {
        final ClusterSummary cluster = new ClusterSummary();

        final String CLUSTER_QUERY = "SELECT * FROM spectrum_cluster WHERE cluster_pk=?";

        template.query(CLUSTER_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                cluster.setId(clusterId);
                cluster.setUUID(rs.getString("uuid"));
                cluster.setAveragePrecursorMz(rs.getFloat("avg_precursor_mz"));
                cluster.setAveragePrecursorCharge(rs.getInt("avg_precursor_charge"));
                cluster.setConsensusSpectrumMz(rs.getString("consensus_spectrum_mz"));
                cluster.setConsensusSpectrumIntensity(rs.getString("consensus_spectrum_intensity"));
                cluster.setNumberOfSpectra(rs.getInt("number_of_spectra"));
                cluster.setTotalNumberOfSpectra(rs.getInt("total_number_of_spectra"));
                cluster.setMaxPeptideRatio(rs.getFloat("max_ratio"));
                cluster.setNumberOfProjects(rs.getInt("number_of_projects"));
                cluster.setTotalNumberOfProjects(rs.getInt("total_number_of_projects"));
                cluster.setNumberOfSpecies(rs.getInt("number_of_species"));
                cluster.setTotalNumberOfSpecies(rs.getInt("total_number_of_species"));
                cluster.setNumberOfModifications(rs.getInt("number_of_modifications"));
                cluster.setTotalNumberOfModifications(rs.getInt("total_number_of_modifications"));
                ClusterQuality quality = ClusterQuality.getClusterQuality(rs.getInt("quality"));
                cluster.setQuality(quality);
                cluster.setAnnotation(rs.getString("annotation"));
            }
        });

        return cluster;
    }

    @Override
    public List<ClusteredSpectrumDetail> findClusteredSpectrumSummaryByClusterId(final Long clusterId) {
        final ArrayList<ClusteredSpectrumDetail> clusteredSpectrumSummaries = new ArrayList<ClusteredSpectrumDetail>();

        final String SPECTRUM_QUERY = "SELECT * FROM cluster_has_spectrum JOIN spectrum ON (spectrum_fk = spectrum_pk) " +
                "WHERE cluster_fk=?";

        template.query(SPECTRUM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ClusteredSpectrumDetail clusteredSpectrumDetail = new ClusteredSpectrumDetail();
                clusteredSpectrumDetail.setClusterId(clusterId);
                clusteredSpectrumDetail.setSpectrumId(rs.getLong("spectrum_fk"));
                clusteredSpectrumDetail.setSimilarityScore(rs.getFloat("similarity"));
                String spectrumRef = rs.getString("spectrum_ref");
                clusteredSpectrumDetail.setReferenceId(spectrumRef);

                SpectrumDetail spectrumDetail = new SpectrumDetail();
                spectrumDetail.setId(rs.getLong("spectrum_pk"));
                spectrumDetail.setReferenceId(spectrumRef);
                spectrumDetail.setAssayId(rs.getLong("assay_fk"));
                spectrumDetail.setPrecursorMz(rs.getFloat("precursor_mz"));
                spectrumDetail.setPrecursorCharge(rs.getInt("precursor_charge"));
                spectrumDetail.setIdentified(rs.getBoolean("is_identified"));

                clusteredSpectrumDetail.setSpectrumDetail(spectrumDetail);
                clusteredSpectrumSummaries.add(clusteredSpectrumDetail);
            }
        });

        return clusteredSpectrumSummaries;
    }

    @Override
    public List<ClusteredPSMDetail> findClusteredPSMSummaryByClusterId(final Long clusterId) {
        final List<ClusteredPSMDetail> clusteredPSMSummaries = new ArrayList<ClusteredPSMDetail>();

        final String PSM_QUERY = "SELECT * FROM cluster_has_psm JOIN psm ON (psm_fk = psm_pk) WHERE cluster_fk=?";

        template.query(PSM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ClusteredPSMDetail clusteredPSMDetail = new ClusteredPSMDetail();
                clusteredPSMDetail.setClusterId(clusterId);
                clusteredPSMDetail.setPsmId(rs.getLong("psm_fk"));
                clusteredPSMDetail.setPsmRatio(rs.getFloat("ratio"));
                clusteredPSMDetail.setRank(rs.getFloat("rank"));
                String sequence = rs.getString("sequence");
                clusteredPSMDetail.setSequence(sequence);
                long spectrumId = rs.getLong("spectrum_fk");
                clusteredPSMDetail.setSpectrumId(spectrumId);

                PSMDetail psmDetail = new PSMDetail();
                psmDetail.setId(rs.getLong("psm_pk"));
                psmDetail.setSpectrumId(spectrumId);
                psmDetail.setAssayId(rs.getLong("assay_fk"));
                psmDetail.setArchivePSMId(rs.getString("archive_psm_id"));
                psmDetail.setSequence(rs.getString("sequence"));
                psmDetail.setModifications(rs.getString("modifications"));
                psmDetail.setStandardisedModifications(rs.getString("modifications_standardised"));
                psmDetail.setSearchEngine(rs.getString("search_engine"));
                psmDetail.setSearchEngineScores(rs.getString("search_engine_scores"));
                psmDetail.setSearchDatabase(rs.getString("search_database"));
                psmDetail.setProteinAccession(rs.getString("protein_accession"));
                psmDetail.setProteinGroup(rs.getString("protein_group"));
                psmDetail.setProteinName(rs.getString("protein_name"));
                psmDetail.setStartPosition(rs.getInt("start_position"));
                psmDetail.setStopPosition(rs.getInt("stop_position"));
                psmDetail.setPreAminoAcid(rs.getString("pre_amino_acid"));
                psmDetail.setPostAminoAcid(rs.getString("post_amino_acid"));
                psmDetail.setDeltaMZ(rs.getFloat("delta_mz"));
                psmDetail.setQuantificationLabel(rs.getString("quantification_label"));
                clusteredPSMDetail.setPsmDetail(psmDetail);

                clusteredPSMSummaries.add(clusteredPSMDetail);
            }
        });

        return clusteredPSMSummaries;
    }

    @Override
    public List<ClusteredPSMDetail> findClusteredPSMSummaryByClusterId(final Long clusterId, final float minimumRanking) {
        final List<ClusteredPSMDetail> clusteredPSMSummaries = new ArrayList<ClusteredPSMDetail>();

        final String PSM_QUERY = "SELECT * FROM cluster_has_psm JOIN psm ON (psm_fk = psm_pk) WHERE cluster_fk=? and rank <= ?";

        template.query(PSM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
                ps.setFloat(2, minimumRanking);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ClusteredPSMDetail clusteredPSMDetail = new ClusteredPSMDetail();
                clusteredPSMDetail.setClusterId(clusterId);
                clusteredPSMDetail.setPsmId(rs.getLong("psm_fk"));
                clusteredPSMDetail.setPsmRatio(rs.getFloat("ratio"));
                clusteredPSMDetail.setRank(rs.getFloat("rank"));
                String sequence = rs.getString("sequence");
                clusteredPSMDetail.setSequence(sequence);
                long spectrumId = rs.getLong("spectrum_fk");
                clusteredPSMDetail.setSpectrumId(spectrumId);

                PSMDetail psmDetail = new PSMDetail();
                psmDetail.setId(rs.getLong("psm_pk"));
                psmDetail.setSpectrumId(spectrumId);
                psmDetail.setAssayId(rs.getLong("assay_fk"));
                psmDetail.setArchivePSMId(rs.getString("archive_psm_id"));
                psmDetail.setSequence(rs.getString("sequence"));
                psmDetail.setModifications(rs.getString("modifications"));
                psmDetail.setStandardisedModifications(rs.getString("modifications_standardised"));
                psmDetail.setSearchEngine(rs.getString("search_engine"));
                psmDetail.setSearchEngineScores(rs.getString("search_engine_scores"));
                psmDetail.setSearchDatabase(rs.getString("search_database"));
                psmDetail.setProteinAccession(rs.getString("protein_accession"));
                psmDetail.setProteinGroup(rs.getString("protein_group"));
                psmDetail.setProteinName(rs.getString("protein_name"));
                psmDetail.setStartPosition(rs.getInt("start_position"));
                psmDetail.setStopPosition(rs.getInt("stop_position"));
                psmDetail.setPreAminoAcid(rs.getString("pre_amino_acid"));
                psmDetail.setPostAminoAcid(rs.getString("post_amino_acid"));
                psmDetail.setDeltaMZ(rs.getFloat("delta_mz"));
                psmDetail.setQuantificationLabel(rs.getString("quantification_label"));
                clusteredPSMDetail.setPsmDetail(psmDetail);

                clusteredPSMSummaries.add(clusteredPSMDetail);
            }
        });

        return clusteredPSMSummaries;
    }

    @Override
    public List<ClusteredPSMDetail> findClusteredPSMSummaryByArchiveId(final String archivePeptideId) {
        final List<ClusteredPSMDetail> clusteredPSMSummaries = new ArrayList<ClusteredPSMDetail>();

        final String PSM_QUERY = "SELECT * FROM cluster_has_psm JOIN psm ON (psm_fk = psm_pk) WHERE archive_psm_id=?";

        template.query(PSM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, archivePeptideId);
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ClusteredPSMDetail clusteredPSMDetail = new ClusteredPSMDetail();
                clusteredPSMDetail.setClusterId(rs.getLong("cluster_fk"));
                clusteredPSMDetail.setPsmId(rs.getLong("psm_fk"));
                clusteredPSMDetail.setPsmRatio(rs.getFloat("ratio"));
                clusteredPSMDetail.setRank(rs.getFloat("rank"));
                String sequence = rs.getString("sequence");
                clusteredPSMDetail.setSequence(sequence);
                long spectrumId = rs.getLong("spectrum_fk");
                clusteredPSMDetail.setSpectrumId(spectrumId);

                PSMDetail psmDetail = new PSMDetail();
                psmDetail.setId(rs.getLong("psm_pk"));
                psmDetail.setSpectrumId(spectrumId);
                psmDetail.setAssayId(rs.getLong("assay_fk"));
                psmDetail.setArchivePSMId(rs.getString("archive_psm_id"));
                psmDetail.setSequence(rs.getString("sequence"));
                psmDetail.setModifications(rs.getString("modifications"));
                psmDetail.setStandardisedModifications(rs.getString("modifications_standardised"));
                psmDetail.setSearchEngine(rs.getString("search_engine"));
                psmDetail.setSearchEngineScores(rs.getString("search_engine_scores"));
                psmDetail.setSearchDatabase(rs.getString("search_database"));
                psmDetail.setProteinAccession(rs.getString("protein_accession"));
                psmDetail.setProteinGroup(rs.getString("protein_group"));
                psmDetail.setProteinName(rs.getString("protein_name"));
                psmDetail.setStartPosition(rs.getInt("start_position"));
                psmDetail.setStopPosition(rs.getInt("stop_position"));
                psmDetail.setPreAminoAcid(rs.getString("pre_amino_acid"));
                psmDetail.setPostAminoAcid(rs.getString("post_amino_acid"));
                psmDetail.setDeltaMZ(rs.getFloat("delta_mz"));
                psmDetail.setQuantificationLabel(rs.getString("quantification_label"));
                clusteredPSMDetail.setPsmDetail(psmDetail);

                clusteredPSMSummaries.add(clusteredPSMDetail);
            }
        });

        return clusteredPSMSummaries;
    }


}
