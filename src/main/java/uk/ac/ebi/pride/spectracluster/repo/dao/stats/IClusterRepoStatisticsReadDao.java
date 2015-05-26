package uk.ac.ebi.pride.spectracluster.repo.dao.stats;

import uk.ac.ebi.pride.spectracluster.repo.model.ClusterRepoStatistics;

import java.util.List;

/**
 * Accessing cluster statistics
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface IClusterRepoStatisticsReadDao {

    /**
     * Get general statistics from cluster repository
     *
     * @return a list of statistics
     */
    List<ClusterRepoStatistics> getGeneralStatistics();

    /**
     * Get statistics using a given prefix
     *
     * @return  a list of statistics which starts with the given prefix
     */
    List<ClusterRepoStatistics> getStatisticsByPrefix(String prefix);


}
