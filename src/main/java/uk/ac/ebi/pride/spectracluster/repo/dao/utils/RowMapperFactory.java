package uk.ac.ebi.pride.spectracluster.repo.dao.utils;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import uk.ac.ebi.pride.spectracluster.repo.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * Factory class for generating row mappers
 *
 * @author Rui Wang
 * @version $Id$
 */
public final class RowMapperFactory {

    private static final ClusteredPSMDetailRowMapper clusteredPSMDetailRowMapper = new ClusteredPSMDetailRowMapper();
    private static final ClusteredPSMReportRowMapper clusterRowMapper   = new ClusteredPSMReportRowMapper();
    private static final ClusteredSpectrumRowMapper clusteredSpectrumRowMapper = new ClusteredSpectrumRowMapper();
    private static final ClusterSummaryRowMapper clusterSummaryRowMapper = new ClusterSummaryRowMapper();
    private static final AssayDetailRowMapper assayDetailRowMapper = new AssayDetailRowMapper();
    private static final PSMDetailRowMapper psmDetailRowMapper = new PSMDetailRowMapper();
    private static final SpectrumDetailRowMapper spectrumDetailRowMapper = new SpectrumDetailRowMapper();
    private static final SpectrumLibraryDetailRowMapper spectrumLibraryDetailRowMapper = new SpectrumLibraryDetailRowMapper();
    private static final ClusterRepoStatisticsRowMapper clusterRepoStatisticsRowMapper = new ClusterRepoStatisticsRowMapper();
    private static final AssayReportRowMapper assayReportRowMapper = new AssayReportRowMapper();

    private RowMapperFactory(){}

    public static ClusteredPSMDetailRowMapper getClusteredPSMDetailRowMapper() {
        return clusteredPSMDetailRowMapper;
    }

    public static ClusteredSpectrumRowMapper getClusteredSpectrumRowMapper() {
        return clusteredSpectrumRowMapper;
    }

    public static ClusterSummaryRowMapper getClusterSummaryRowMapper() {
        return clusterSummaryRowMapper;
    }

    public static AssayDetailRowMapper getAssayDetailRowMapper() {
        return assayDetailRowMapper;
    }

    public static PSMDetailRowMapper getPsmDetailRowMapper() {
        return psmDetailRowMapper;
    }

    public static SpectrumDetailRowMapper getSpectrumDetailRowMapper() {
        return spectrumDetailRowMapper;
    }

    public static SpectrumLibraryDetailRowMapper getSpectrumLibraryDetailRowMapper() {
        return spectrumLibraryDetailRowMapper;
    }

    public static ClusterRepoStatisticsRowMapper getClusterRepoStatisticsRowMapper() {
        return clusterRepoStatisticsRowMapper;
    }

    public static ClusteredPSMReportRowMapper getClusteredPSMReportRowMapper() {
        return clusterRowMapper;
    }

    public static AssayReportRowMapper getAssayReportRowMapper() {
        return assayReportRowMapper;
    }

    public static class  AssayReportRowMapper implements RowMapper<AssayReport>{

        @Override
        public AssayReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            AssayReport assayReport = new AssayReport();
            assayReport.setId(rs.getLong("assay_pk"));
            assayReport.setAccession(rs.getString("assay_accession"));
            assayReport.setProjectAccession(rs.getString("project_accession"));
            assayReport.setTaxonomyId(rs.getString("taxonomy_id"));
            assayReport.setSpecies(rs.getString("species"));
            assayReport.setMultiSpecies(rs.getBoolean("multi_species"));
            return assayReport;
        }
    }

    private static class ClusteredPSMDetailRowMapper implements RowMapper<ClusteredPSMDetail> {

        @Override
        public ClusteredPSMDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
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

            return clusteredPSMDetail;
        }
    }

    private static class ClusteredSpectrumRowMapper implements RowMapper<ClusteredSpectrumDetail> {

        @Override
        public ClusteredSpectrumDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClusteredSpectrumDetail clusteredSpectrumDetail = new ClusteredSpectrumDetail();

            clusteredSpectrumDetail.setClusterId(rs.getLong("cluster_fk"));
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

            return clusteredSpectrumDetail;
        }
    }

    private static class ClusterSummaryRowMapper implements RowMapper<ClusterSummary> {

        @Override
        public ClusterSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClusterSummary cluster = new ClusterSummary();

            cluster.setId(rs.getLong("cluster_pk"));
            cluster.setUUID(rs.getString("uuid"));
            cluster.setAveragePrecursorMz(rs.getFloat("avg_precursor_mz"));
            cluster.setAveragePrecursorCharge(rs.getInt("avg_precursor_charge"));
            cluster.setConsensusSpectrumMz(rs.getString("consensus_spectrum_mz"));
            cluster.setConsensusSpectrumIntensity(rs.getString("consensus_spectrum_intensity"));
            cluster.setNumberOfSpectra(rs.getInt("number_of_spectra"));
            cluster.setTotalNumberOfSpectra(rs.getInt("total_number_of_spectra"));
            cluster.setNumberOfPSMs(rs.getInt("number_of_psms"));
            cluster.setTotalNumberOfPSMs(rs.getInt("total_number_of_psms"));
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

    private static class ClusteredPSMReportRowMapper implements RowMapper<ClusteredPSMReport>{

        @Override
        public ClusteredPSMReport mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClusteredPSMReport clusteredPSM = new ClusteredPSMReport();
            clusteredPSM.setClusterId(rs.getLong("cluster_pk"));
            clusteredPSM.setNumberOfSpectra(rs.getInt("number_of_spectra"));
            clusteredPSM.setPsmId(rs.getLong("psm_pk"));
            clusteredPSM.setAssayID(rs.getLong("assay_fk"));
            clusteredPSM.setModifications(rs.getString("modifications"));
            clusteredPSM.setProteinAccession(rs.getString("protein_accession"));
            clusteredPSM.setDeltaMZ(rs.getFloat("delta_mz"));
            clusteredPSM.setClusterNumberProjects(rs.getInt("number_of_projects"));
            clusteredPSM.setClusterNumberPSMs(rs.getInt("number_of_psms"));
            clusteredPSM.setClusterNumberSpectra(rs.getInt("number_of_spectra"));
            clusteredPSM.setSequence(rs.getString("sequence"));
            clusteredPSM.setClusterAvgMz(rs.getFloat("avg_precursor_mz"));
            clusteredPSM.setClusterAvgCharge(rs.getFloat("avg_precursor_charge"));
            ClusterQuality quality = ClusterQuality.getClusterQuality(rs.getInt("quality"));
            clusteredPSM.setQuality(quality);


            return clusteredPSM;
        }
    }

    private static class AssayDetailRowMapper implements RowMapper<AssayDetail> {

        @Override
        public AssayDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
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

            return assayDetail;
        }
    }

    private static class PSMDetailRowMapper implements RowMapper<PSMDetail> {

        @Override
        public PSMDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
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

            return psmDetail;
        }
    }

    private static class SpectrumDetailRowMapper implements RowMapper<SpectrumDetail> {

        @Override
        public SpectrumDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            SpectrumDetail spectrumDetail = new SpectrumDetail();

            spectrumDetail.setId(rs.getLong("spectrum_pk"));
            spectrumDetail.setReferenceId(rs.getString("spectrum_ref"));
            spectrumDetail.setAssayId(rs.getLong("assay_fk"));
            spectrumDetail.setPrecursorMz(rs.getFloat("precursor_mz"));
            spectrumDetail.setPrecursorCharge(rs.getInt("precursor_charge"));
            spectrumDetail.setIdentified(rs.getBoolean("is_identified"));

            return spectrumDetail;
        }
    }

    private static class SpectrumLibraryDetailRowMapper implements RowMapper<SpectrumLibraryDetail> {

        @Override
        public SpectrumLibraryDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
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

            return spectrumLibraryDetail;
        }
    }

    private static class ClusterRepoStatisticsRowMapper implements RowMapper<ClusterRepoStatistics> {

        @Override
        public ClusterRepoStatistics mapRow(ResultSet rs, int rowNum) throws SQLException {
            ClusterRepoStatistics statistics = new ClusterRepoStatistics();

            statistics.setName(rs.getString("name"));
            statistics.setValue(rs.getLong("value"));

            return statistics;
        }
    }
}
