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
     * Get the total number of clusters of a given quality
     *
     * @param quality given cluster quality
     * @return total number of clusters of a given quality
     */
    long getNumberOfClustersByQuality(ClusterQuality quality);

    /**
     * Get the total number of clustered species
     *
     * @return total number clustered species
     */
    long getNumberOfClusteredSpecies();

    /**
     * Get the total number of clustered projects
     *
     * @return total number of clustered projects
     */
    long getNumberOfClusteredProjects();

    /**
     * Get the total number of clustered assays
     *
     * @return total number of clustered assays
     */
    long getNumberOfClusteredAssays();

    /**
     * Get the total number of clustered unique peptide sequences
     *
     * @return total number of clustered unique peptide sequences
     */
    long getNumberOfClusteredDistinctPeptides();

    /**
     * Get the total number of clustered identified spectra
     *
     * @return total number of clustered spectra
     */
    long getNumberOfClusteredIdentifiedSpectra();

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
     * @param pageNo               page number
     * @param pageSize             the size of the page
     * @param lowestClusterQuality the lowest acceptable quality
     * @return a page of cluster ids
     */
    Page<Long> getAllClusterIdsByQuality(final int pageNo, final int pageSize, final ClusterQuality lowestClusterQuality);

    /**
     * Get a Page of clusters
     *
     * @return A Page of ClusterSummary objects
     */
    Page<ClusterSummary> getAllClusterSummaries(final int pageNo, final int pageSize);

    /**
     * Get a page of clusters above or equal to a given quality limit
     *
     * @param pageNo               page number
     * @param pageSize             the size of the page
     * @param lowestClusterQuality the lowest acceptable quality
     * @return a page of clusters
     */
    Page<ClusterSummary> getAllClusterSummariesByQuality(final int pageNo, final int pageSize, final ClusterQuality lowestClusterQuality);

    /**
     * Find a cluster using a given cluster id
     *
     * @param clusterId cluster id
     * @return cluster
     */
    ClusterDetail findCluster(Long clusterId);

    /**
     * Find a cluster using a unique UUID
     * @param uuid  UUID
     * @return cluster
     */
    ClusterDetail findClusterByUUID(String uuid);

    /**
     * Find a summary of a cluster using a given cluster id
     *
     * @param clusterId cluster id
     * @return cluster
     */
    ClusterSummary findClusterSummary(final Long clusterId);

    /**
     * Find a summary of a cluster using its unique UUID
     * @param uuid  UUID
     * @return  cluster
     */
    ClusterSummary findClusterSummaryByUUID(final String uuid);

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
     * Find clustered PSMs that above a given ratio for a cluster
     *
     * @param clusterId      cluster id
     * @param minimumRanking the minimum ranking for the PSM
     * @return a list of psm details
     */
    List<ClusteredPSMDetail> findClusteredPSMSummaryByClusterId(final Long clusterId, final float minimumRanking);

    /**
     * Find a list of clustered PSM using a given id from PRIDE Archive
     *
     * @param archivePeptideId pride archive peptide id
     * @return a clustered psm detail
     */
    List<ClusteredPSMDetail> findClusteredPSMSummaryByArchiveId(final String archivePeptideId);

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
