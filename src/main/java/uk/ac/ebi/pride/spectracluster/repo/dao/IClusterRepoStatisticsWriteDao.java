package uk.ac.ebi.pride.spectracluster.repo.dao;

import uk.ac.ebi.pride.spectracluster.repo.model.ClusterRepoStatistics;

import java.util.List;

/**
 * Write statistics into the cluster repository
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface IClusterRepoStatisticsWriteDao {

    /**
     * Save a list of statistics into the repository
     *
     * @param statistics a list of given statistics
     */
    void saveStatistics(List<ClusterRepoStatistics> statistics);
}
