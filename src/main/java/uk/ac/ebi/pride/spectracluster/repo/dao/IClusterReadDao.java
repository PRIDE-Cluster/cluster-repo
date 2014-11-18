package uk.ac.ebi.pride.spectracluster.repo.dao;


import uk.ac.ebi.pride.spectracluster.repo.model.*;
import uk.ac.ebi.pride.spectracluster.repo.utils.paging.Page;

import java.util.List;

/**
 * Dao interface for reading clusters from data source
 *
 * @author Rui Wang
 * @author Jose A Dianes
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
     * Get a page of cluster ids
     *
     * @param pageNo   page number
     * @param pageSize the size of page
     * @return a page of cluster ids
     */
    Page<Long> getAllClusterIds(final int pageNo, final int pageSize);

    /**
     * Get a Page of clusters
     *
     * @return A Page of ClusterSummary objects
     */
    Page<ClusterSummary> getAllClusterSummaries(final int pageNo, final int pageSize);

    /**
     * Find a cluster using a given cluster id
     *
     * @param clusterId cluster id
     * @return cluster
     */
    ClusterDetail findCluster(Long clusterId);

    /**
     * Find spectra using a list of spectrum references
     * spectrum references are the id from the original database source
     *
     * @param spectrumReferences a list of spectrum references
     * @return
     */
    List<SpectrumDetail> findSpectra(final List<String> spectrumReferences);


    /**
     * Find PSMs using a list of spectrum id
     *
     * @param spectrumIds a list of internal spectrum ids
     * @return a list of PSMs
     */
    List<PSMDetail> findPSMBySpectrumId(final List<Long> spectrumIds);

    /**
     * Find assays using assay ids
     *
     * @param assayIds a list of internal assay ids
     * @return a list of assays
     */
    List<AssayDetail> findAssays(final List<Long> assayIds);

}
