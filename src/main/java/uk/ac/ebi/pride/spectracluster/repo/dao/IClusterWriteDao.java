package uk.ac.ebi.pride.spectracluster.repo.dao;


import uk.ac.ebi.pride.spectracluster.repo.model.*;

import java.util.List;

/**
 * DAO interface for writing clusters into a repository
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface IClusterWriteDao {

    /**
     * Save a list of clusters
     *
     * @param clusters a list of clusters
     */
    void saveClusters(List<ClusterDetail> clusters);

    /**
     * Save a single cluster
     *
     * @param cluster given cluster
     */
    void saveCluster(ClusterDetail cluster);

    /**
     * Save an assay
     *
     * @param assay given assay
     */
    void saveAssay(AssayDetail assay);

    /**
     * Delete all the assay and their related data that belong to a given project accession
     *
     * @param projectAccession project accession
     */
    void deleteAssayByProjectAccession(String projectAccession);

    /**
     * Save a list of spectra
     *
     * @param spectra a list of spectra
     */
    void saveSpectra(List<SpectrumDetail> spectra);

    /**
     * Save spectrum
     *
     * @param spectrum a given spectrum
     */
    void saveSpectrum(SpectrumDetail spectrum);

    /**
     * Save a list of PSMs
     *
     * @param psms a list of PSMs
     */
    void savePSMs(List<PSMDetail> psms);

    /**
     * Save a single PSM
     *
     * @param psm given PSM
     */
    void savePSM(PSMDetail psm);
}
