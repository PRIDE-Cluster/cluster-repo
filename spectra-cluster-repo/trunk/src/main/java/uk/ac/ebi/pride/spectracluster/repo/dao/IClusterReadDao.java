package uk.ac.ebi.pride.spectracluster.repo.dao;


import uk.ac.ebi.pride.spectracluster.repo.model.ClusterSummary;

/**
 * Dao interface for reading clusters from data source
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface IClusterReadDao {

    /**
     * Get the total number of clusters
     *
     * @return total number of clusters
     */
    long getNumberOfClusters();

    /**
     * Find a cluster using a given cluster id
     *
     * @param clusterId cluster id
     * @return cluster
     */
    ClusterSummary findCluster(Long clusterId);

}
