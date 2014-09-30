package uk.ac.ebi.pride.spectracluster.repo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterSummary {
    private Long id;
    private float averagePrecursorMz;
    private float averagePrecursorCharge;
    private String consensusSpectrumMz;
    private String consensusSpectrumIntensity;
    private int numberOfSpectra;
    private float maxPeptideRatio;
    private final List<ClusteredSpectrumSummary> clusteredSpectrumSummaries = new ArrayList<ClusteredSpectrumSummary>();
    private final List<ClusteredPSMSummary> clusteredPSMSummaries = new ArrayList<ClusteredPSMSummary>();
    private final List<AssaySummary> assaySummaries = new ArrayList<AssaySummary>();
    private final Map<String, ClusteredSpectrumSummary> spectrumRefToClusteredSpectrumSummary = new HashMap<String, ClusteredSpectrumSummary>();
    private final Map<String, List<ClusteredPSMSummary>> peptideToClusteredPSMSummary = new HashMap<String, List<ClusteredPSMSummary>>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getAveragePrecursorMz() {
        return averagePrecursorMz;
    }

    public void setAveragePrecursorMz(float averagePrecursorMz) {
        this.averagePrecursorMz = averagePrecursorMz;
    }

    public float getAveragePrecursorCharge() {
        return averagePrecursorCharge;
    }

    public void setAveragePrecursorCharge(float averagePrecursorCharge) {
        this.averagePrecursorCharge = averagePrecursorCharge;
    }

    public String getConsensusSpectrumMz() {
        return consensusSpectrumMz;
    }

    public void setConsensusSpectrumMz(String consensusSpectrumMz) {
        this.consensusSpectrumMz = consensusSpectrumMz;
    }

    public String getConsensusSpectrumIntensity() {
        return consensusSpectrumIntensity;
    }

    public void setConsensusSpectrumIntensity(String consensusSpectrumIntensity) {
        this.consensusSpectrumIntensity = consensusSpectrumIntensity;
    }

    public int getNumberOfSpectra() {
        return numberOfSpectra;
    }

    public void setNumberOfSpectra(int numberOfSpectra) {
        this.numberOfSpectra = numberOfSpectra;
    }

    public float getMaxPeptideRatio() {
        return maxPeptideRatio;
    }

    public void setMaxPeptideRatio(float maxPeptideRatio) {
        this.maxPeptideRatio = maxPeptideRatio;
    }

    public List<ClusteredSpectrumSummary> getClusteredSpectrumSummaries() {
        return clusteredSpectrumSummaries;
    }

    public ClusteredSpectrumSummary getClusteredSpectrumSummary(String spectrumRef) {
        return spectrumRefToClusteredSpectrumSummary.get(spectrumRef);
    }

    public void addClusteredSpectrumSummary(ClusteredSpectrumSummary clusteredSpectrumSummary) {
        clusteredSpectrumSummaries.add(clusteredSpectrumSummary);
        spectrumRefToClusteredSpectrumSummary.put(clusteredSpectrumSummary.getReferenceId(), clusteredSpectrumSummary);
    }

    public void addClusteredSpectrumSummaries(List<ClusteredSpectrumSummary> clusteredSpectrumSummaries) {
        for (ClusteredSpectrumSummary clusteredSpectrumSummary : clusteredSpectrumSummaries) {
            addClusteredSpectrumSummary(clusteredSpectrumSummary);
        }
    }

    public List<ClusteredPSMSummary> getClusteredPSMSummaries() {
        return clusteredPSMSummaries;
    }

    public List<ClusteredPSMSummary> getClusteredPSMSummaries(String sequence) {
        String cleanPeptideSequence = cleanPeptideSequence(sequence);
        return peptideToClusteredPSMSummary.get(cleanPeptideSequence);
    }

    public void addClusteredPSMSummary(ClusteredPSMSummary clusteredPSMSummary) {
        clusteredPSMSummaries.add(clusteredPSMSummary);
        String cleanedSequence = cleanPeptideSequence(clusteredPSMSummary.getSequence());

        List<ClusteredPSMSummary> psmSummaries = peptideToClusteredPSMSummary.get(cleanedSequence);
        if (psmSummaries == null) {
            psmSummaries = new ArrayList<ClusteredPSMSummary>();
            peptideToClusteredPSMSummary.put(cleanedSequence, psmSummaries);
        }

        psmSummaries.add(clusteredPSMSummary);
    }

    public void addClusteredPSMSummaries(List<ClusteredPSMSummary> clusteredPSMSummaries) {
        for (ClusteredPSMSummary clusteredPSMSummary : clusteredPSMSummaries) {
            addClusteredPSMSummary(clusteredPSMSummary);
        }
    }

    public List<AssaySummary> getAssaySummaries() {
        return assaySummaries;
    }

    public void addAssaySummary(AssaySummary assaySummary) {
        assaySummaries.add(assaySummary);
    }

    public void addAssaySummaries(List<AssaySummary> assaySummaries) {
        this.assaySummaries.addAll(assaySummaries);
    }

    private String cleanPeptideSequence(String original) {
        return original.toUpperCase().replaceAll("I", "L");
    }

    @Override
    public String toString() {
        return "ClusterSummary{" +
                "averagePrecursorMz=" + averagePrecursorMz +
                ", averagePrecursorCharge=" + averagePrecursorCharge +
                ", numberOfSpectra=" + numberOfSpectra +
                ", maxPeptideRatio=" + maxPeptideRatio +
                '}';
    }
}
