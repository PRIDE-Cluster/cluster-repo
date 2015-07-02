package uk.ac.ebi.pride.spectracluster.repo.dao.cluster;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import uk.ac.ebi.pride.spectracluster.repo.dao.utils.RowMapperFactory;
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

            List<SpectrumDetail> spectrumDetails = template.query(SELECT_QUERY, RowMapperFactory.getSpectrumDetailRowMapper());
            spectrumSummaries.addAll(spectrumDetails);
        }

        return spectrumSummaries;
    }

    @Override
    public List<PSMDetail> findPSMBySpectrumId(final List<Long> spectrumIds) {
        final List<PSMDetail> psmSummaries = new ArrayList<PSMDetail>();

        final List<String> concatenateIds = QueryUtils.concatenateIds(spectrumIds, 500);

        for (String concatenateId : concatenateIds) {
            final String SELECT_QUERY = "SELECT * FROM psm WHERE spectrum_fk IN (" + concatenateId + ")";

            List<PSMDetail> psmDetails = template.query(SELECT_QUERY, RowMapperFactory.getPsmDetailRowMapper());
            psmSummaries.addAll(psmDetails);
        }

        return psmSummaries;
    }

    @Override
    public List<AssayDetail> findAssays(final List<Long> assayIds) {

        final List<AssayDetail> assaySummaries = new ArrayList<AssayDetail>();

        List<String> concatenateIds = QueryUtils.concatenateIds(assayIds, 500);

        for (String concatenateId : concatenateIds) {
            final String ASSAY_QUERY = "SELECT * FROM assay WHERE assay_pk IN (" + concatenateId + ")";

            List<AssayDetail> assayDetails = template.query(ASSAY_QUERY, RowMapperFactory.getAssayDetailRowMapper());
            assaySummaries.addAll(assayDetails);
        }

        return assaySummaries;
    }

    @Override
    public long getNumberOfClusters() {
        final String QUERY = "SELECT count(*) FROM spectrum_cluster";

        return template.queryForObject(QUERY, Long.class);
    }

    @Override
    public long getNumberOfClustersByQuality(final ClusterQuality quality) {
        final String QUERY = "SELECT count(*) FROM spectrum_cluster WHERE quality = " + quality.getQualityLevel();

        return template.queryForObject(QUERY, Long.class);
    }

    @Override
    public long getNumberOfClusteredSpecies() {
        final String QUERY = "SELECT count(distinct species) FROM assay";

        return template.queryForObject(QUERY, Long.class);
    }

    @Override
    public long getNumberOfClusteredProjects() {
        final String QUERY = "SELECT count(distinct project_accession) FROM assay";

        return template.queryForObject(QUERY, Long.class);
    }

    @Override
    public long getNumberOfClusteredAssays() {
        final String QUERY = "SELECT count(distinct assay_accession) FROM assay";

        return template.queryForObject(QUERY, Long.class);
    }

    @Override
    public long getNumberOfClusteredDistinctPeptides() {
        final String QUERY = "SELECT count(distinct sequence) FROM cluster_has_psm join psm on(psm_fk = psm_pk)";

        return template.queryForObject(QUERY, Long.class);
    }

    @Override
    public long getNumberOfClusteredIdentifiedSpectra() {
        final String QUERY = "SELECT count(distinct spectrum_fk) FROM cluster_has_spectrum";

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
        final String CLUSTER_QUERY_COUNT = "SELECT COUNT(*) FROM spectrum_cluster WHERE quality <= " + lowestClusterQuality.getQualityLevel();
        final String CLUSTER_QUERY = "SELECT cluster_pk FROM spectrum_cluster WHERE quality <= " + lowestClusterQuality.getQualityLevel();

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
        final String CLUSTER_QUERY_COUNT = "SELECT count(*) FROM spectrum_cluster WHERE quality <= " + lowestClusterQuality.getQualityLevel();
        final String CLUSTER_QUERY = "SELECT * FROM spectrum_cluster WHERE quality <= " + lowestClusterQuality.getQualityLevel();

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
                RowMapperFactory.getClusterSummaryRowMapper()
        );

    }

    @Override
    public ClusterDetail findClusterByUUID(String uuid) {
        ClusterSummary cluster = findClusterSummaryByUUID(uuid);
        return getClusterDetail(cluster);
    }

    @Override
    public ClusterDetail findCluster(final Long clusterId) {
        // read cluster summary
        ClusterSummary cluster = findClusterSummary(clusterId);
        return getClusterDetail(cluster);
    }

    private ClusterDetail getClusterDetail(ClusterSummary cluster) {
        ClusterDetail clusterDetail = new ClusterDetail(cluster);

        if (cluster.getId() != null) {

            // read spectra details
            List<ClusteredSpectrumDetail> spectrumSummaries = findClusteredSpectrumSummaryByClusterId(cluster.getId());
            clusterDetail.addClusteredSpectrumDetails(spectrumSummaries);

            // read psm details
            List<ClusteredPSMDetail> psmSummaries = findClusteredPSMSummaryByClusterId(cluster.getId());
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
    public ClusterSummary findClusterSummaryByUUID(final String uuid) {
        final String CLUSTER_QUERY = "SELECT * FROM spectrum_cluster WHERE uuid=?";

        List<ClusterSummary> clusterSummaries = template.query(CLUSTER_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, uuid);
            }
        }, RowMapperFactory.getClusterSummaryRowMapper());

        if (clusterSummaries.isEmpty()) {
            return new ClusterSummary();
        } else {
            return clusterSummaries.get(0);
        }
    }

    @Override
    public ClusterSummary findClusterSummary(final Long clusterId) {
        final String CLUSTER_QUERY = "SELECT * FROM spectrum_cluster WHERE cluster_pk=?";

        List<ClusterSummary> clusterSummaries = template.query(CLUSTER_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, RowMapperFactory.getClusterSummaryRowMapper());

        if (clusterSummaries.isEmpty()) {
            return new ClusterSummary();
        } else {
            return clusterSummaries.get(0);
        }
    }

    @Override
    public List<ClusteredSpectrumDetail> findClusteredSpectrumSummaryByClusterId(final Long clusterId) {
        final String SPECTRUM_QUERY = "SELECT * FROM cluster_has_spectrum JOIN spectrum ON (spectrum_fk = spectrum_pk) " +
                "WHERE cluster_fk=?";

        return template.query(SPECTRUM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, RowMapperFactory.getClusteredSpectrumRowMapper());
    }

    @Override
    public List<ClusteredPSMDetail> findClusteredPSMSummaryByClusterId(final Long clusterId) {
        final String PSM_QUERY = "SELECT * FROM cluster_has_psm JOIN psm ON (psm_fk = psm_pk) WHERE cluster_fk=?";

        return template.query(PSM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
            }
        }, RowMapperFactory.getClusteredPSMDetailRowMapper());
    }

    @Override
    public List<ClusteredPSMDetail> findClusteredPSMSummaryByClusterId(final Long clusterId, final float minimumRanking) {
        final String PSM_QUERY = "SELECT * FROM cluster_has_psm JOIN psm ON (psm_fk = psm_pk) WHERE cluster_fk=? and rank <= ?";

        return template.query(PSM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, clusterId);
                ps.setFloat(2, minimumRanking);
            }
        }, RowMapperFactory.getClusteredPSMDetailRowMapper());
    }

    @Override
    public List<ClusteredPSMDetail> findClusteredPSMSummaryByArchiveId(final String archivePeptideId) {
        final String PSM_QUERY = "SELECT * FROM cluster_has_psm JOIN psm ON (psm_fk = psm_pk) WHERE archive_psm_id=?";

        return template.query(PSM_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, archivePeptideId);
            }
        }, RowMapperFactory.getClusteredPSMDetailRowMapper());
    }



}
