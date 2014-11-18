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
    private final List<ClusteredSpectrumDetail> clusteredSpectrumSummaries = new ArrayList<ClusteredSpectrumDetail>();
    private final List<ClusteredPSMDetail> clusteredPSMSummaries = new ArrayList<ClusteredPSMDetail>();
    private final List<AssayDetail> assaySummaries = new ArrayList<AssayDetail>();
    private final Map<String, ClusteredSpectrumDetail> spectrumRefToClusteredSpectrumSummary = new HashMap<String, ClusteredSpectrumDetail>();
    private final Map<String, List<ClusteredPSMDetail>> peptideToClusteredPSMSummary = new HashMap<String, List<ClusteredPSMDetail>>();

    public ClusterDetail() {
    }

    public ClusterDetail(ClusterSummary clusterSummary) {
        setId(clusterSummary.getId());
        setAveragePrecursorCharge(clusterSummary.getAveragePrecursorCharge());
        setAveragePrecursorMz(clusterSummary.getAveragePrecursorMz());
        setConsensusSpectrumIntensity(clusterSummary.getConsensusSpectrumIntensity());
        setConsensusSpectrumMz(clusterSummary.getConsensusSpectrumMz());
        setMaxPeptideRatio(clusterSummary.getMaxPeptideRatio());
        setNumberOfSpectra(clusterSummary.getNumberOfSpectra());
    }

    public List<ClusteredSpectrumDetail> getClusteredSpectrumSummaries() {
        return clusteredSpectrumSummaries;
    }

    public ClusteredSpectrumDetail getClusteredSpectrumSummary(String spectrumRef) {
        return spectrumRefToClusteredSpectrumSummary.get(spectrumRef);
    }

    public void addClusteredSpectrumSummary(ClusteredSpectrumDetail clusteredSpectrumDetail) {
        clusteredSpectrumSummaries.add(clusteredSpectrumDetail);
        spectrumRefToClusteredSpectrumSummary.put(clusteredSpectrumDetail.getReferenceId(), clusteredSpectrumDetail);
    }

    public void addClusteredSpectrumSummaries(List<ClusteredSpectrumDetail> clusteredSpectrumSummaries) {
        for (ClusteredSpectrumDetail clusteredSpectrumDetail : clusteredSpectrumSummaries) {
            addClusteredSpectrumSummary(clusteredSpectrumDetail);
        }
    }

    public List<ClusteredPSMDetail> getClusteredPSMSummaries() {
        return clusteredPSMSummaries;
    }

    public List<ClusteredPSMDetail> getClusteredPSMSummaries(String sequence) {
        String cleanPeptideSequence = cleanPeptideSequence(sequence);
        return peptideToClusteredPSMSummary.get(cleanPeptideSequence);
    }

    public void addClusteredPSMSummary(ClusteredPSMDetail clusteredPSMDetail) {
        clusteredPSMSummaries.add(clusteredPSMDetail);
        String cleanedSequence = cleanPeptideSequence(clusteredPSMDetail.getSequence());

        List<ClusteredPSMDetail> psmSummaries = peptideToClusteredPSMSummary.get(cleanedSequence);
        if (psmSummaries == null) {
            psmSummaries = new ArrayList<ClusteredPSMDetail>();
            peptideToClusteredPSMSummary.put(cleanedSequence, psmSummaries);
        }

        psmSummaries.add(clusteredPSMDetail);
    }

    public void addClusteredPSMSummaries(List<ClusteredPSMDetail> clusteredPSMSummaries) {
        for (ClusteredPSMDetail clusteredPSMDetail : clusteredPSMSummaries) {
            addClusteredPSMSummary(clusteredPSMDetail);
        }
    }

    public List<AssayDetail> getAssaySummaries() {
        return assaySummaries;
    }

    public void addAssaySummary(AssayDetail assayDetail) {
        assaySummaries.add(assayDetail);
    }

    public void addAssaySummaries(List<AssayDetail> assaySummaries) {
        this.assaySummaries.addAll(assaySummaries);
    }

    private String cleanPeptideSequence(String original) {
        return original.toUpperCase().replaceAll("I", "L");
    }
}
