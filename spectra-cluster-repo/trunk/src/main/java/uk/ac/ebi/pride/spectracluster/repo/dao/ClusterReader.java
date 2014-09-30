package uk.ac.ebi.pride.spectracluster.repo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import uk.ac.ebi.pride.spectracluster.repo.model.*;
import uk.ac.ebi.pride.spectracluster.repo.utils.QueryUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Reader class for reading clusters
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterReader implements IClusterReadDao {

    private final JdbcTemplate template;

    public ClusterReader(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<SpectrumSummary> findSpectra(final List<String> spectrumReferences) {
        final List<SpectrumSummary> spectrumSummaries = new ArrayList<SpectrumSummary>();

        final List<String> concatenateIds = QueryUtils.concatenateIds(spectrumReferences, 500);

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

        return spectrumSummaries;
    }

    @Override
    public List<PSMSummary> findPSMBySpectrumId(final List<Long> spectrumIds) {
        final List<PSMSummary> psmSummaries = new ArrayList<PSMSummary>();

        final List<String> concatenateIds = QueryUtils.concatenateIds(spectrumIds, 500);

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

        return psmSummaries;
    }

    @Override
    public List<AssaySummary> findAssays(final List<Long> assayIds) {
        final HashSet<Long> ids = new HashSet<Long>(assayIds);

        final List<AssaySummary> assaySummaries = new ArrayList<AssaySummary>();

        String concatenateIds = QueryUtils.concatenateIds(ids);

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

    @Override
    public long getNumberOfClusters() {
        final String QUERY = "select count(*) from spectrum_cluster";

        return template.queryForObject(QUERY, Long.class);
    }

    @Override
    public ClusterSummary findCluster(final Long clusterId) {
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
            List<Long> assayIds = new ArrayList<Long>();
            for (ClusteredPSMSummary clusteredPSMSummary : psmSummaries) {
                assayIds.add(clusteredPSMSummary.getPsmSummary().getAssayId());
            }

            // read assay details
            List<AssaySummary> assaySummaries = findAssays(assayIds);
            cluster.addAssaySummaries(assaySummaries);
        }

        return cluster;
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


}
