package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * Summary of a cluster which does not include all the related information, such as: spectrum or PSM
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterSummary {
    private Long id;
    private String uuid;
    private float averagePrecursorMz;
    private int averagePrecursorCharge;
    private String consensusSpectrumMz;
    private String consensusSpectrumIntensity;
    private int numberOfSpectra;
    private int totalNumberOfSpectra;
    private float maxPeptideRatio;
    private int numberOfProjects;
    private int totalNumberOfProjects;
    private int numberOfSpecies;
    private int totalNumberOfSpecies;
    private int numberOfModifications;
    private int totalNumberOfModifications;
    private ClusterQuality quality;
    private String annotation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public float getAveragePrecursorMz() {
        return averagePrecursorMz;
    }

    public void setAveragePrecursorMz(float averagePrecursorMz) {
        this.averagePrecursorMz = averagePrecursorMz;
    }

    public int getAveragePrecursorCharge() {
        return averagePrecursorCharge;
    }

    public void setAveragePrecursorCharge(int averagePrecursorCharge) {
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

    public int getTotalNumberOfSpectra() {
        return totalNumberOfSpectra;
    }

    public void setTotalNumberOfSpectra(int totalNumberOfSpectra) {
        this.totalNumberOfSpectra = totalNumberOfSpectra;
    }

    public float getMaxPeptideRatio() {
        return maxPeptideRatio;
    }

    public void setMaxPeptideRatio(float maxPeptideRatio) {
        this.maxPeptideRatio = maxPeptideRatio;
    }

    public int getNumberOfProjects() {
        return numberOfProjects;
    }

    public void setNumberOfProjects(int numberOfProjects) {
        this.numberOfProjects = numberOfProjects;
    }

    public int getTotalNumberOfProjects() {
        return totalNumberOfProjects;
    }

    public void setTotalNumberOfProjects(int totalNumberOfProjects) {
        this.totalNumberOfProjects = totalNumberOfProjects;
    }

    public int getNumberOfSpecies() {
        return numberOfSpecies;
    }

    public void setNumberOfSpecies(int numberOfSpecies) {
        this.numberOfSpecies = numberOfSpecies;
    }

    public int getTotalNumberOfSpecies() {
        return totalNumberOfSpecies;
    }

    public void setTotalNumberOfSpecies(int totalNumberOfSpecies) {
        this.totalNumberOfSpecies = totalNumberOfSpecies;
    }

    public int getNumberOfModifications() {
        return numberOfModifications;
    }

    public void setNumberOfModifications(int numberOfModifications) {
        this.numberOfModifications = numberOfModifications;
    }

    public int getTotalNumberOfModifications() {
        return totalNumberOfModifications;
    }

    public void setTotalNumberOfModifications(int totalNumberOfModifications) {
        this.totalNumberOfModifications = totalNumberOfModifications;
    }

    public ClusterQuality getQuality() {
        return quality;
    }

    public void setQuality(ClusterQuality quality) {
        this.quality = quality;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    @Override
    public String toString() {
        return "ClusterSummary{" +
                "uuid='" + uuid + '\'' +
                ", averagePrecursorMz=" + averagePrecursorMz +
                ", averagePrecursorCharge=" + averagePrecursorCharge +
                ", numberOfSpectra=" + numberOfSpectra +
                ", maxPeptideRatio=" + maxPeptideRatio +
                ", numberOfProjects=" + numberOfProjects +
                ", quality=" + quality +
                ", annotation=" + annotation +
                '}';
    }
}
