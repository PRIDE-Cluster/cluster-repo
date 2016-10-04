package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * Statistics for the cluster repository
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterRepoStatistics {

    public static final String NUMBER_OF_CLUSTERS = "Number of clusters";
    public static final String NUMBER_OF_HIGH_QUALITY_CLUSTERS = "Number of high quality clusters";
    public static final String NUMBER_OF_MEDIUM_QUALITY_CLUSTERS = "Number of medium quality clusters";
    public static final String NUMBER_OF_LOW_QUALITY_CLUSTERS = "Number of low quality clusters";
    public static final String NUMBER_OF_SPECIES = "Number of species";
    public static final String NUMBER_OF_PROJECTS = "Number of projects";
    public static final String NUMBER_OF_ASSAYS = "Number of assays";
    public static final String NUMBER_OF_DISTINCT_PEPTIDES = "Number of distinct peptides";
    public static final String NUMBER_OF_IDENTIFIED_SPECTRA = "Number of identified spectra";
    public static final String NUMBER_OF_CLUSTERS_PER_SPECIES = "Number of clusters per species";
    public static final String NUMBER_OF_UNIQUE_PEPTIDES_PER_SPECIES = "Number of unique peptides per species";
    public static final String OVERLAPPING_UNIQUE_PEPTIDES_ON_SPECIES = "Overlapping unique peptides on species";
    public static final String ALL_STATISTICS = "";

    private String name;
    private Long value;

    public ClusterRepoStatistics() {
    }

    public ClusterRepoStatistics(String name, Long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClusterRepoStatistics)) return false;

        ClusterRepoStatistics that = (ClusterRepoStatistics) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClusterRepoStatistics{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
