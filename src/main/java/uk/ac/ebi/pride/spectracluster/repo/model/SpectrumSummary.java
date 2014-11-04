package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class SpectrumSummary {
    private Long id;
    private Long assayId;
    private String referenceId;
    private float precursorMz;
    private int precursorCharge;
    private boolean identified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssayId() {
        return assayId;
    }

    public void setAssayId(Long assayId) {
        this.assayId = assayId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public float getPrecursorMz() {
        return precursorMz;
    }

    public void setPrecursorMz(float precursorMz) {
        this.precursorMz = precursorMz;
    }

    public int getPrecursorCharge() {
        return precursorCharge;
    }

    public void setPrecursorCharge(int precursorCharge) {
        this.precursorCharge = precursorCharge;
    }

    public boolean isIdentified() {
        return identified;
    }

    public void setIdentified(boolean identified) {
        this.identified = identified;
    }
}
