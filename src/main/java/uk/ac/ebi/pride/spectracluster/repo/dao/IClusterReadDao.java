package uk.ac.ebi.pride.spectracluster.repo.dao;


import uk.ac.ebi.pride.spectracluster.repo.model.AssaySummary;
import uk.ac.ebi.pride.spectracluster.repo.model.ClusterSummary;
import uk.ac.ebi.pride.spectracluster.repo.model.PSMSummary;
import uk.ac.ebi.pride.spectracluster.repo.model.SpectrumSummary;

import java.util.List;

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

    /**
     * Find spectra using a list of spectrum references
     * spectrum references are the id from the original database source
     *
     * @param spectrumReferences a list of spectrum references
     * @return
     */
    List<SpectrumSummary> findSpectra(final List<String> spectrumReferences);


    /**
     * Find PSMs using a list of spectrum id
     *
     * @param spectrumIds a list of internal spectrum ids
     * @return a list of PSMs
     */
    List<PSMSummary> findPSMBySpectrumId(final List<Long> spectrumIds);

    /**
     * Find assays using assay ids
     *
     * @param assayIds a list of internal assay ids
     * @return a list of assays
     */
    List<AssaySummary> findAssays(final List<Long> assayIds);

}
