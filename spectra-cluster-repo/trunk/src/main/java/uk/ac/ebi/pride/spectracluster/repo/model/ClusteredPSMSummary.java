package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class ClusteredPSMSummary {
    private Long clusterId;
    private Long psmId;
    private Long spectrumId;
    private String sequence;
    private float psmRatio;
    private int rank;
    private PSMSummary psmSummary;

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Long getPsmId() {
        return psmId;
    }

    public void setPsmId(Long psmId) {
        this.psmId = psmId;
    }

    public Long getSpectrumId() {
        return spectrumId;
    }

    public void setSpectrumId(Long spectrumId) {
        this.spectrumId = spectrumId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public float getPsmRatio() {
        return psmRatio;
    }

    public void setPsmRatio(float psmRatio) {
        this.psmRatio = psmRatio;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public PSMSummary getPsmSummary() {
        return psmSummary;
    }

    public void setPsmSummary(PSMSummary psmSummary) {
        this.psmSummary = psmSummary;
    }
}
