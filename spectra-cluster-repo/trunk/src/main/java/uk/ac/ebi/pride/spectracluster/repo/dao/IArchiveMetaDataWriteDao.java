package uk.ac.ebi.pride.spectracluster.repo.dao;


import uk.ac.ebi.pride.spectracluster.repo.model.AssaySummary;
import uk.ac.ebi.pride.spectracluster.repo.model.PSMSummary;
import uk.ac.ebi.pride.spectracluster.repo.model.SpectrumSummary;

import java.util.List;

/**
 * DAO interface for writing metadata retrieved from PRIDE Archive
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface IArchiveMetaDataWriteDao {

    /**
     * Save an assay
     *
     * @param assay given assay
     */
    void saveAssay(AssaySummary assay);

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
    void saveSpectra(List<SpectrumSummary> spectra);

    /**
     * Save spectrum
     *
     * @param spectrum a given spectrum
     */
    void saveSpectrum(SpectrumSummary spectrum);

    /**
     * Save a list of PSMs
     *
     * @param psms a list of PSMs
     */
    void savePSMs(List<PSMSummary> psms);

    /**
     * Save a single PSM
     *
     * @param psm given PSM
     */
    void savePSM(PSMSummary psm);
}
