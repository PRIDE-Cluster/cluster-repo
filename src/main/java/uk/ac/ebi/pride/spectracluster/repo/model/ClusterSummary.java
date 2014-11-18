package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * Summary of a cluster which does not include all the related information, such as: spectrum or PSM
 *
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
