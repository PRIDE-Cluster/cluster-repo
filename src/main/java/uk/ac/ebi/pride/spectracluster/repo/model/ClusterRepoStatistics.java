package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * Statistics for the cluster repository
 *
 * @author Rui Wang
 * @version $Id$
 */
public class ClusterRepoStatistics {

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
