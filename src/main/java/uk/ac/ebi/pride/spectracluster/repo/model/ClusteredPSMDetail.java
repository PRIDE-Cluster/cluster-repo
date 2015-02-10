package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * Details of a cluster PSM, including its relationship to its cluster and PSM details, such as: sequence
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusteredPSMDetail {
    private Long clusterId; // internal cluster id
    private Long psmId; // internal psm id
    private Long spectrumId; // internal spectrum id
    private String sequence; // peptide sequence
    private float psmRatio; // ratio is the number of distinct psm / the number of spectra
    private float rank;              // rank of the psm by ratio
    private PSMDetail psmDetail; // full psm summary

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

    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    public PSMDetail getPsmDetail() {
        return psmDetail;
    }

    public void setPsmDetail(PSMDetail psmDetail) {
        this.psmDetail = psmDetail;
    }
}
