package uk.ac.ebi.pride.spectracluster.repo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A complete representation of a cluster, including psm details, spectrum details and assay details
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterDetail extends ClusterSummary{
    private final List<ClusteredSpectrumDetail> clusteredSpectrumDetails = new ArrayList<ClusteredSpectrumDetail>();
    private final List<ClusteredPSMDetail> clusteredPSMDetails = new ArrayList<ClusteredPSMDetail>();
    private final List<AssayDetail> assayDetails = new ArrayList<AssayDetail>();
    private final Map<String, ClusteredSpectrumDetail> spectrumRefToClusteredSpectrumDetail = new HashMap<String, ClusteredSpectrumDetail>();
    private final Map<String, List<ClusteredPSMDetail>> peptideToClusteredPSMDetail = new HashMap<String, List<ClusteredPSMDetail>>();

    public ClusterDetail() {
    }

    public ClusterDetail(ClusterSummary clusterSummary) {
        setId(clusterSummary.getId());
        setUUID(clusterSummary.getUUID());
        setAveragePrecursorCharge(clusterSummary.getAveragePrecursorCharge());
        setAveragePrecursorMz(clusterSummary.getAveragePrecursorMz());
        setConsensusSpectrumIntensity(clusterSummary.getConsensusSpectrumIntensity());
        setConsensusSpectrumMz(clusterSummary.getConsensusSpectrumMz());
        setMaxPeptideRatio(clusterSummary.getMaxPeptideRatio());
        setNumberOfSpectra(clusterSummary.getNumberOfSpectra());
        setTotalNumberOfSpectra(clusterSummary.getTotalNumberOfSpectra());
        setNumberOfProjects(clusterSummary.getNumberOfProjects());
        setTotalNumberOfProjects(clusterSummary.getTotalNumberOfProjects());
        setNumberOfSpecies(clusterSummary.getNumberOfSpecies());
        setTotalNumberOfSpecies(clusterSummary.getTotalNumberOfSpecies());
        setNumberOfModifications(clusterSummary.getNumberOfModifications());
        setTotalNumberOfModifications(clusterSummary.getTotalNumberOfModifications());
        setQuality(clusterSummary.getQuality());
        setAnnotation(clusterSummary.getAnnotation());
    }

    public List<ClusteredSpectrumDetail> getClusteredSpectrumDetails() {
        return clusteredSpectrumDetails;
    }

    public ClusteredSpectrumDetail getClusteredSpectrumDetail(String spectrumRef) {
        return spectrumRefToClusteredSpectrumDetail.get(spectrumRef);
    }

    public void addClusteredSpectrumDetail(ClusteredSpectrumDetail clusteredSpectrumDetail) {
        clusteredSpectrumDetails.add(clusteredSpectrumDetail);
        spectrumRefToClusteredSpectrumDetail.put(clusteredSpectrumDetail.getReferenceId(), clusteredSpectrumDetail);
    }

    public void addClusteredSpectrumDetails(List<ClusteredSpectrumDetail> clusteredSpectrumSummaries) {
        for (ClusteredSpectrumDetail clusteredSpectrumDetail : clusteredSpectrumSummaries) {
            addClusteredSpectrumDetail(clusteredSpectrumDetail);
        }
    }

    public List<ClusteredPSMDetail> getClusteredPSMDetails() {
        return clusteredPSMDetails;
    }

    public List<ClusteredPSMDetail> getClusteredPSMDetails(String sequence) {
        String cleanPeptideSequence = cleanPeptideSequence(sequence);
        return peptideToClusteredPSMDetail.get(cleanPeptideSequence);
    }

    public ClusteredPSMDetail getClusteredPSMDetail(Long spectrumId) {
        for (ClusteredPSMDetail clusteredPSMDetail : clusteredPSMDetails) {
            if (clusteredPSMDetail.getSpectrumId().equals(spectrumId)) {
                return clusteredPSMDetail;
            }
        }

        return null;
    }

    public void addClusteredPSMDetail(ClusteredPSMDetail clusteredPSMDetail) {
        clusteredPSMDetails.add(clusteredPSMDetail);
        String cleanedSequence = cleanPeptideSequence(clusteredPSMDetail.getSequence());

        List<ClusteredPSMDetail> psmSummaries = peptideToClusteredPSMDetail.get(cleanedSequence);
        if (psmSummaries == null) {
            psmSummaries = new ArrayList<ClusteredPSMDetail>();
            peptideToClusteredPSMDetail.put(cleanedSequence, psmSummaries);
        }

        psmSummaries.add(clusteredPSMDetail);
    }

    public void addClusteredPSMDetails(List<ClusteredPSMDetail> clusteredPSMSummaries) {
        for (ClusteredPSMDetail clusteredPSMDetail : clusteredPSMSummaries) {
            addClusteredPSMDetail(clusteredPSMDetail);
        }
    }

    public List<AssayDetail> getAssayDetails() {
        return assayDetails;
    }

    public AssayDetail getAssayDetail(Long assayId) {
        for (AssayDetail assayDetail : assayDetails) {
            if (assayDetail.getId().equals(assayId)) {
                return assayDetail;
            }
        }

        return null;
    }

    public void addAssayDetail(AssayDetail assayDetail) {
        assayDetails.add(assayDetail);
    }

    public void addAssayDetails(List<AssayDetail> assaySummaries) {
        assayDetails.addAll(assaySummaries);
    }

    private String cleanPeptideSequence(String original) {
        return original.toUpperCase().replaceAll("I", "L");
    }


}
