package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class ClusteredSpectrumSummary {
    private Long clusterId;
    private Long spectrumId;
    private String referenceId;
    private float similarityScore;
    private SpectrumSummary spectrumSummary;

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Long getSpectrumId() {
        return spectrumId;
    }

    public void setSpectrumId(Long spectrumId) {
        this.spectrumId = spectrumId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public float getSimilarityScore() {
        return similarityScore;
    }

    public void setSimilarityScore(float similarityScore) {
        this.similarityScore = similarityScore;
    }

    public SpectrumSummary getSpectrumSummary() {
        return spectrumSummary;
    }

    public void setSpectrumSummary(SpectrumSummary spectrumSummary) {
        this.spectrumSummary = spectrumSummary;
    }
}
