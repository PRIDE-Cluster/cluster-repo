package uk.ac.ebi.pride.spectracluster.repo.dao.library;

import uk.ac.ebi.pride.spectracluster.repo.model.SpectrumLibraryDetail;

import java.util.List;

/**
 * Write interface for spectral library details
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface ISpectrumLibraryWriteDao {

    /**
     * Save a list of spectral library details
     *
     * @param spectrumLibraryDetails a given list of spectral library details
     */
    void saveSpectrumLibraries(List<SpectrumLibraryDetail> spectrumLibraryDetails);

    /**
     * Save a single spectral library detail
     *
     * @param spectrumLibraryDetail a given spectral library
     */
    void saveSpectrumLibrary(SpectrumLibraryDetail spectrumLibraryDetail);
}
