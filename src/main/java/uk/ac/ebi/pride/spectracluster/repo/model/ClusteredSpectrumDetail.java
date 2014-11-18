package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * Details of a cluster spectrum, including its relationship to its cluster and spectrum precursor information
 *
 * NOTE: we don't store the peak list of the spectrum
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusteredSpectrumDetail {
    private Long clusterId;
    private Long spectrumId;
    private String referenceId;
    private float similarityScore;
    private SpectrumDetail spectrumDetail;

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

    public SpectrumDetail getSpectrumDetail() {
        return spectrumDetail;
    }

    public void setSpectrumDetail(SpectrumDetail spectrumDetail) {
        this.spectrumDetail = spectrumDetail;
    }
}
