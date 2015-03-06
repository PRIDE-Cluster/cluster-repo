package uk.ac.ebi.pride.spectracluster.repo.dao.cluster;


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
     * Get a page of cluster ids above or equal to a given quality limit
     *
     * @param pageNo             page number
     * @param pageSize           the size of the page
     * @param lowestQualityLimit the lowest acceptable quality
     * @return a page of cluster ids
     */
    Page<Long> getAllClusterIdsByQuality(final int pageNo, final int pageSize, final int lowestQualityLimit);

    /**
     * Get a Page of clusters
     *
     * @return A Page of ClusterSummary objects
     */
    Page<ClusterSummary> getAllClusterSummaries(final int pageNo, final int pageSize);

    /**
     * Get a page of clusters above or equal to a given quality limit
     *
     * @param pageNo             page number
     * @param pageSize           the size of the page
     * @param lowestQualityLimit the lowest acceptable quality
     * @return a page of clusters
     */
    Page<ClusterSummary> getAllClusterSummariesByQuality(final int pageNo, final int pageSize, final int lowestQualityLimit);

    /**
     * Find a cluster using a given cluster id
     *
     * @param clusterId cluster id
     * @return cluster
     */
    ClusterDetail findCluster(Long clusterId);

    /**
     * Find a summary of a cluster using a given cluster id
     *
     * @param clusterId cluster id
     * @return cluster
     */
    ClusterSummary findClusterSummary(final Long clusterId);

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
     * Find clustered PSMs using a given cluster id
     *
     * @param clusterId given cluster id
     * @return a list of clustered psms
     */
    List<ClusteredPSMDetail> findClusteredPSMSummaryByClusterId(final Long clusterId);

    /**
     * Find a clustered PSM using a given id from PRIDE Archive
     *
     * @param archivePeptideId pride archive peptide id
     * @return a clustered psm detail
     */
    ClusteredPSMDetail findClusteredPSMSummaryByArchiveId(final String archivePeptideId);

    /**
     * Find clustered spectrum details using a given cluster id
     *
     * @param clusterId given cluster id
     * @return a list of clustered spectrum details
     */
    List<ClusteredSpectrumDetail> findClusteredSpectrumSummaryByClusterId(final Long clusterId);

    /**
     * Find assays using assay ids
     *
     * @param assayIds a list of internal assay ids
     * @return a list of assays
     */
    List<AssayDetail> findAssays(final List<Long> assayIds);

}
