package uk.ac.ebi.pride.spectracluster.repo.model;

/**
 * Predefined quality levels
 *
 * @author Rui Wang
 * @version $Id$
 */
public enum ClusterQuality {
    HIGH (1),
    MEDIUM (2),
    LOW (3);

    private int qualityLevel;

    ClusterQuality(int qualityLevel) {
        this.qualityLevel = qualityLevel;
    }

    public int getQualityLevel() {
        return qualityLevel;
    }

    public static ClusterQuality getClusterQuality(int level) {
        for (ClusterQuality clusterQuality : ClusterQuality.values()) {
            if (clusterQuality.getQualityLevel() == level) {
                return clusterQuality;
            }
        }

        return null;
    }
}
