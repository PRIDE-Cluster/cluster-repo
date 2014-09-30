package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class PSMSummary {
    private Long id;
    private Long spectrumId;
    private Long assayId;
    private String archivePSMId;
    private String sequence;
    private String modifications;
    private String searchEngine;
    private String searchEngineScores;
    private String searchDatabase;
    private String proteinAccession;
    private String proteinGroup;
    private String proteinName;
    private int startPosition = -1;
    private int stopPosition = -1;
    private String preAminoAcid;
    private String postAminoAcid;
    private float deltaMZ;
    private String quantificationLabel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpectrumId() {
        return spectrumId;
    }

    public void setSpectrumId(Long spectrumId) {
        this.spectrumId = spectrumId;
    }

    public Long getAssayId() {
        return assayId;
    }

    public void setAssayId(Long assayId) {
        this.assayId = assayId;
    }

    public String getArchivePSMId() {
        return archivePSMId;
    }

    public void setArchivePSMId(String archivePSMId) {
        this.archivePSMId = archivePSMId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getModifications() {
        return modifications;
    }

    public void setModifications(String modifications) {
        this.modifications = modifications;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getSearchEngineScores() {
        return searchEngineScores;
    }

    public void setSearchEngineScores(String searchEngineScores) {
        this.searchEngineScores = searchEngineScores;
    }

    public String getSearchDatabase() {
        return searchDatabase;
    }

    public void setSearchDatabase(String searchDatabase) {
        this.searchDatabase = searchDatabase;
    }

    public String getProteinAccession() {
        return proteinAccession;
    }

    public void setProteinAccession(String proteinAccession) {
        this.proteinAccession = proteinAccession;
    }

    public String getProteinGroup() {
        return proteinGroup;
    }

    public void setProteinGroup(String proteinGroup) {
        this.proteinGroup = proteinGroup;
    }

    public String getProteinName() {
        return proteinName;
    }

    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getStopPosition() {
        return stopPosition;
    }

    public void setStopPosition(int stopPosition) {
        this.stopPosition = stopPosition;
    }

    public String getPreAminoAcid() {
        return preAminoAcid;
    }

    public void setPreAminoAcid(String preAminoAcid) {
        this.preAminoAcid = preAminoAcid;
    }

    public String getPostAminoAcid() {
        return postAminoAcid;
    }

    public void setPostAminoAcid(String postAminoAcid) {
        this.postAminoAcid = postAminoAcid;
    }

    public float getDeltaMZ() {
        return deltaMZ;
    }

    public void setDeltaMZ(float deltaMZ) {
        this.deltaMZ = deltaMZ;
    }

    public String getQuantificationLabel() {
        return quantificationLabel;
    }

    public void setQuantificationLabel(String quantificationLabel) {
        this.quantificationLabel = quantificationLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PSMSummary)) return false;

        PSMSummary that = (PSMSummary) o;

        if (!archivePSMId.equals(that.archivePSMId)) return false;
        if (assayId != null ? !assayId.equals(that.assayId) : that.assayId != null) return false;
        if (modifications != null ? !modifications.equals(that.modifications) : that.modifications != null)
            return false;
        if (!sequence.equals(that.sequence)) return false;
        if (spectrumId != null ? !spectrumId.equals(that.spectrumId) : that.spectrumId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = spectrumId != null ? spectrumId.hashCode() : 0;
        result = 31 * result + (assayId != null ? assayId.hashCode() : 0);
        result = 31 * result + archivePSMId.hashCode();
        result = 31 * result + sequence.hashCode();
        result = 31 * result + (modifications != null ? modifications.hashCode() : 0);
        return result;
    }
}
